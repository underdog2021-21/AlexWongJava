package com.heima.user.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP用户私信信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApUserLetter extends Model<ApUserLetter> {

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
