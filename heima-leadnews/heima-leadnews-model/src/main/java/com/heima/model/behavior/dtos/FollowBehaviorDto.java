package com.heima.model.behavior.dtos;

import lombok.Data;

@Data
public class FollowBehaviorDto {
    /**
     * 设备ID
     */
    String equipmentId;
    /**
     * 文章id
     */
    Long articleId;
    /**
     * 关注的id
     */
    Integer followId;
    /**
     * 用户id
     */
    Integer userId;
}