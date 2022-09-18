package com.heima.user.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP用户反馈信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApUserFeedback extends Model<ApUserFeedback> {

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
     * 发送人昵称
     */
    private String userName;

    /**
     * 私信内容
     */
    private String content;

    /**
     * 反馈图片,多张逗号分隔
     */
    private String images;

    /**
     * 是否阅读
     */
    private Boolean isSolve;

    private String solveNote;

    /**
     * 阅读时间
     */
    private Date solvedTime;

    /**
     * 创建时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
