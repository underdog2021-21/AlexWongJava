package com.heima.user.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP用户文章列表
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApUserArticleList extends Model<ApUserArticleList> {

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
     * 频道ID
     */
    private Integer channelId;

    /**
     * 动态ID
     */
    private Integer articleId;

    /**
     * 是否展示
     */
    private Boolean isShow;

    /**
     * 推荐时间
     */
    private Date recommendTime;

    /**
     * 是否阅读
     */
    private Boolean isRead;

    /**
     * 推荐算法
     */
    private Integer strategyId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
