package com.heima.user.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP用户消息通知信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApUserMessage extends Model<ApUserMessage> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 发送人ID
     */
    private Integer senderId;

    /**
     * 发送人昵称
     */
    private String senderName;

    /**
     * 私信内容
     */
    private String content;

    /**
     * 消息类型
            0 关注
            1 取消关注
            2 点赞文章
            3 取消点赞文章
            4 转发文章
            5 收藏文章
            6 点赞评论
            7 审核通过评论
            8 私信通知
            9 评论通知
            10 分享通知
            
            100 身份证审核通过
            101 身份证审核拒绝
            102 实名认证通过
            103 实名认证失败
            104 自媒体人祝贺
            105 异常登录通知
            106 反馈回复
            107 转发通知
     */
    private Integer type;

    /**
     * 是否阅读
     */
    private Boolean isRead;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 阅读时间
     */
    private Date readTime;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
