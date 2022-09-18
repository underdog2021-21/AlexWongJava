package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.admin.entity.AdChannel;
import com.heima.admin.mapper.AdChannelMapper;
import com.heima.admin.service.AdChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.dtos.PageResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.model.admin.dtos.ChannelDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 频道信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-01
 */
@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel> implements AdChannelService {

    /**
     * @Description: 分页查询
     * @param dto
     * @return PageResult<ChannelDto>
     * @Author wangzifeng
     * @CreateTime 2022/9/4 9:23
     */
    @Override
    public PageResult<ChannelDto> findByPage(ChannelDto dto) {
        //设置分页条件(泛型写AdChannel代表当前分页是对这张表进行分页)
        Page<AdChannel> adChannelPage = new Page<>(dto.getPage(),dto.getSize());
        //设置查询条件(这里的泛型代表该查询是针对那张表,就写那张表对应的实体类)
        LambdaQueryWrapper<AdChannel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(dto.getName()),AdChannel::getName,dto.getName());
        //分页查询
        IPage<AdChannel> page = page(adChannelPage, lambdaQueryWrapper);
        List<AdChannel> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            //用枚举异常防止message写死
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //对象转换(BeanUtils.copyWithCollection(谁被拷贝?,拷贝成谁?))
        List<ChannelDto> list= BeanHelper.copyWithCollection(records,ChannelDto.class);
        //构造返回对象
        PageResult<ChannelDto> channelDtoPageResult = new PageResult<>(
                dto.getPage(),
                dto.getSize(),
                adChannelPage.getTotal(),
                list);

        return channelDtoPageResult;
    }
}
