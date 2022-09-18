package com.heima.article.listener;


import com.heima.article.entity.ApArticle;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.ApAuthorService;
import com.heima.common.constants.message.WmNewsUpDownConstants;
import com.heima.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.heima.common.constants.message.WmNewsUpDownConstants.WM_NEWS_UP_OR_DOWN_TOPIC;

@Component
@Slf4j
public class WmNewsUpDownListener {
    @Autowired
    private ApArticleService apArticleService;

    @KafkaListener(topics = WM_NEWS_UP_OR_DOWN_TOPIC)
    public void receiveMsg(ConsumerRecord<String, String> record) {
        //jdk8新特性,判断是否存在
        Optional<ConsumerRecord<String, String>> optional = Optional.ofNullable(record);
        if (optional.isPresent()) {
            String value = record.value();
            log.info("article服务接收自媒体文章上下架消息，value={}",value);
            Map<String, Long> map = JsonUtils.toMap(value, String.class, Long.class);
            apArticleService.upOrDown(map);
        }
    }
}
