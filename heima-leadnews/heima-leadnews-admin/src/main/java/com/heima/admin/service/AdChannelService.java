package com.heima.admin.service;

import com.heima.admin.entity.AdChannel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dtos.PageResult;
import com.heima.model.admin.dtos.ChannelDto;

/**
 * <p>
 * 频道信息表 服务类
 * </p>
 *
 * @author HM
 * @since 2022-09-01
 */
public interface AdChannelService extends IService<AdChannel> {

    PageResult<ChannelDto> findByPage(ChannelDto dto);
}
