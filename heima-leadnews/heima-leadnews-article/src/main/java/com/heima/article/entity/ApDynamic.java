package com.heima.article.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP用户动态信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApDynamic extends Model<ApDynamic> {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 文章作者的ID
     */
    private Integer userId;

    /**
     * 作者昵称
     */
    private String userName;

    /**
     * 频道名称
     */
    private String content;

    /**
     * 是否转发
     */
    private Boolean isForward;

    /**
     * 转发文章ID
     */
    private Long articleId;

    /**
     * 转发文章标题
     */
    private String articelTitle;

    /**
     * 转发文章图片
     */
    private String articleImage;

    /**
     * 布局标识
            0 无图动态
            1 单图动态
            2 多图动态
            3 转发动态
     */
    private Integer layout;

    /**
     * 文章图片
            多张逗号分隔
     */
    private String images;

    /**
     * 点赞数量
     */
    private Integer likes;

    /**
     * 收藏数量
     */
    private Integer collection;

    /**
     * 评论数量
     */
    private Integer comment;

    /**
     * 阅读数量
     */
    private Integer views;

    /**
     * 创建时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
