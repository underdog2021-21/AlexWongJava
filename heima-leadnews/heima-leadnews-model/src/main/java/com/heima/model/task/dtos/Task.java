package com.heima.model.task.dtos;

import lombok.Data;

@Data
public class Task {
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 类型
     */
    private Integer taskType;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 执行时间
     */
    private long executeTime;

    /**
     * task参数
     */
    private byte[] parameters;
}
