package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.article.client.ArticleFeign;
import com.heima.common.dtos.PageResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.model.article.dtos.AuthorDto;
import com.heima.model.media.dtos.WmUserDto;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.dtos.UserAuthDto;
import com.heima.user.entity.ApUser;
import com.heima.user.entity.ApUserAuth;
import com.heima.user.mapper.ApUserAuthMapper;
import com.heima.user.service.ApUserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.user.service.ApUserService;
import com.heima.wemedia.client.WemediaFeign;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * APP实名认证信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Service
@Slf4j
public class ApUserAuthServiceImpl extends ServiceImpl<ApUserAuthMapper, ApUserAuth> implements ApUserAuthService {

    @Autowired
    private ApUserService apUserService;
    @Autowired
    private WemediaFeign wemediaFeign;
    @Autowired
    private ArticleFeign articleFeign;

    /**
     * @Description: 分页查询
     * @param authDto
     * @return PageResult<UserAuthDto>
     * @Author wangzifeng
     * @CreateTime 2022/9/4 13:46
     */
    @Override
    public PageResult<UserAuthDto> findByPage(AuthDto authDto) {
        //设置分页条件
        Long page = authDto.getPage();
        Long size = authDto.getSize();
        //查哪张表,就把哪张表对应的实体类写进泛型
        Page<ApUserAuth> iPage1 = new Page<>(page, size);
        //设置查询条件
        //查哪张表,就把哪张表对应的实体类写进泛型
        LambdaQueryWrapper<ApUserAuth> queryWrapper = new LambdaQueryWrapper<>();
        //根据接口,要看status
        queryWrapper.eq(authDto.getStatus() != null, ApUserAuth::getStatus, authDto.getStatus());
        //分页查询
        IPage<ApUserAuth> iPage2 = page(iPage1, queryWrapper);
        if (iPage2 == null || CollectionUtils.isEmpty(iPage2.getRecords())) {
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //构造返回对象
        List<ApUserAuth> records = iPage2.getRecords();
        //注意方法别选错
        List<UserAuthDto> userAuthDtos = BeanHelper.copyWithCollection(records, UserAuthDto.class);
        //四个参数,记得total是查询之后的IPage<ApUserAuth>对象里获得的
        return new PageResult<>(page, size, iPage2.getTotal(), userAuthDtos);

    }

    /**
     * @param authDto
     * @param status
     * @return void
     * @Description: 用户审核, 同时操作多张表, 要注意开启分布式事务注解@GlobalTransaction
     * @Author wangzifeng
     * @CreateTime 2022/9/4 13:46
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void authUser(AuthDto authDto, int status) {

        ApUserAuth apUserAuth = new ApUserAuth();
        //设置主键id,状态
        apUserAuth.setId(authDto.getId());
        apUserAuth.setStatus(status);
        //status=2,审核不通过
        if (status == 2) {
            //将驳回信息(前端输入)设置为拒绝原因
            apUserAuth.setReason(authDto.getMsg());
        }
        //status!=2,则根据审核主键id修改用户审核状态
        boolean b = updateById(apUserAuth);
        //修改失败
        if (!b) {
            log.error("修改认证状态失败");
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
        if (status == 2) {
            log.info("用户和审核不通过,直接结束,auth_id={}", authDto.getId());
            return;
        }
        /*
        审核通过,根据数据库表ap_user_auth与ap_user,wm_user与ap_user均为一对一的关系
        创建自媒体用户时，需要判断ap_user是否在自媒体用户表(wm_user)中已经存在
        其中wm_user表中ap_user_id为ap_user表中id,ap_user_auth表中user_id为ap_user表的id
        则可以根据获取ap_user_auth的主键id获取user_id,即wm_user表中的ap_user_id
        进而判断ap_user是否在自媒体用户表(wm_user)中已经存在(判断的步骤在调用的自媒体服务接口方法运行)
        */
        //根据ap_user_auth的主键id,获取apUserAuth的对象
        apUserAuth = getById(apUserAuth.getId());
        //获取ap_user_id(ap_user_auth表中的user_id)
        Integer apUserId = apUserAuth.getUserId();
        //根据user_id获取ap_user表中对应的对象
        ApUser apUser = apUserService.getById(apUserId);
        //远程调用自媒体服务,创建自媒体用户,赋属性
        WmUserDto wmUserDto = new WmUserDto();
        wmUserDto.setApUserId(apUserId);
        wmUserDto.setName(apUser.getName());
        wmUserDto.setPassword(apUser.getPassword());
        wmUserDto.setPhone(apUser.getPhone());
        wmUserDto.setStatus(9);
        try {
            Integer saveWmUser = wemediaFeign.saveWmUser(wmUserDto);

        } catch (Exception e) {
            log.error("远程调用wemedia，创建自媒体用户，失败");
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }

        //更新文章作者数据库
        //创建文章作者,远程调用文章服务
        AuthorDto authorDto = new AuthorDto();
        authorDto.setUserId(apUserId);
        authorDto.setName(apUser.getName());
        //设置类型,2-自媒体用户(article库中ap_author表)
        authorDto.setType(2);
        authorDto.setWmUserId(wmUserDto.getId());
        try {
            articleFeign.saveArticleAuthor(authorDto);

        } catch (Exception e) {
            log.error("远程调用article，创建作者，失败");
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }
        //操作user库中ap_user表
        //修改用户表的flag字段,根据user_id 改成 1-自媒体人
        apUser = new ApUser();
        //定位对应该ap_user_id(ap_user表里的id)的对象
        apUser.setId(apUserId);
        apUser.setFlag(1);
        //操作ap_user表,所以用apUserService
        boolean b1 = apUserService.updateById(apUser);
        if (!b1) {
            log.error("修改 apUser 的flag失败，apUserId={}", apUserId);
            throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
        }

        //测试分布式事务
        //System.out.println(1/0);
    }
}
