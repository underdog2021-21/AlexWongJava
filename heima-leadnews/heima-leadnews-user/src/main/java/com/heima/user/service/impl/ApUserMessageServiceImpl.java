package com.heima.user.service.impl;

import com.heima.user.entity.ApUserMessage;
import com.heima.user.mapper.ApUserMessageMapper;
import com.heima.user.service.ApUserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * APP用户消息通知信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Service
public class ApUserMessageServiceImpl extends ServiceImpl<ApUserMessageMapper, ApUserMessage> implements ApUserMessageService {

}
