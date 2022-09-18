package com.heima.wemedia.delay;

import com.heima.common.delayTask.RedisDelayedQueueUtil;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.model.media.dtos.WmNewsResultDTO;
import com.heima.wemedia.entity.WmNews;
import com.heima.wemedia.service.WmNewsAuditService;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisDelayQueue {
    @Autowired
    private RedisDelayedQueueUtil redisDelayedQueueUtil;
    @Autowired
    private WmNewsAuditService wmNewsAuditService;
    @PostConstruct
    public void runTask() {
        //周期性执行任务的线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        //执行任务,周期的到延迟队列获取元素,Rate固定频次,timetask实现了runnable,初始化无延迟,每秒钟执行1次
        scheduledExecutorService.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //从延迟队列获取元素
                try {
                    Object delayQueue = redisDelayedQueueUtil.getDelayQueue("wm.news.pub");
                    Integer wmNewsId = Integer.valueOf(delayQueue.toString());
                    //调用方法,发布文章
                    WmNews wmNews = new WmNews();
                    wmNews.setId(wmNewsId);
                    wmNewsAuditService.publishWmNews(wmNews,2);
                } catch (Exception e) {
                    log.error("延迟队列获取元素失败");
                    e.printStackTrace();
                    throw new LeadException(AppHttpCodeEnum.SERVER_ERROR);
                }
            }
        }, 0,1 , TimeUnit.SECONDS );
        //        存放一个无用元素，用来激活微服务和redis队列的监听
        redisDelayedQueueUtil.addQueue(0,1,TimeUnit.SECONDS,"wm.news.pub");
    }
}
