package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.model.media.dtos.WmUserDto;
import com.heima.wemedia.entity.WmUser;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 自媒体用户信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Slf4j
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {


    @Override
    public Integer saveWmUser(WmUserDto wmUserDto) {
        //先根据apUserId(表中字段ap_user_id)查询自媒体用户,判断用户是否存在
        LambdaQueryWrapper<WmUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmUser::getApUserId, wmUserDto.getApUserId());
        WmUser wmUser = getOne(lambdaQueryWrapper);
        //如果该自媒体用户不存在,则创建一个新用户
        if (wmUser == null) {
            wmUser  = new WmUser();
            wmUser.setApUserId(wmUserDto.getApUserId());
            wmUser.setStatus(wmUserDto.getStatus());
            wmUser.setCreatedTime(new Date());
        }
        //如果该用户存在,则更新即可
        wmUser.setName(wmUserDto.getName());
        wmUser.setPassword(wmUserDto.getPassword());
        wmUser.setPhone(wmUserDto.getPhone());
        boolean b = saveOrUpdate(wmUser);
        if (!b) {
            log.error("新增自媒体用户失败,apUserId={"+wmUserDto.getApUserId()+"}");
        }
        return wmUser.getId();
    }
}
