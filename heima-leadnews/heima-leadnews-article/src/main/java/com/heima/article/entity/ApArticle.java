package com.heima.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章信息表，存储已发布的文章
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApArticle extends Model<ApArticle> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER) //雪花算法，创建表时未写主键生成策略，要写这个主见注解（mybatisplus）
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章作者的ID
     */
    private Integer authorId;

    /**
     * 作者昵称
     */
    private String authorName;

    /**
     * 文章所属频道ID
     */
    private Integer channelId;

    /**
     * 频道名称
     */
    private String channelName;

    /**
     * 文章布局
     * 0 无图文章
     * 1 单图文章
     * 2 多图文章
     */
    private Integer layout;

    /**
     * 文章标记
     * 0 普通文章
     * 1 热点文章
     * 2 置顶文章
     * 3 精品文章
     * 4 大V 文章
     */
    private Integer flag;

    /**
     * 文章图片
     * 多张逗号分隔
     */
    private String images;

    /**
     * 文章标签最多3个 逗号分隔
     */
    private String labels;

    /**
     * 点赞数量
     */
    private Long likes;

    /**
     * 收藏数量
     */
    private Long collection;

    /**
     * 评论数量
     */
    private Long comments;

    /**
     * 阅读数量
     */
    private Long views;

    /**
     * 省市
     */
    private Integer provinceId;

    /**
     * 市区
     */
    private Integer cityId;

    /**
     * 区县
     */
    private Integer regionId;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 来源
     */
    private Integer origin;

    /**
     * 是否可以评论1-是 0否
     */
    private Boolean isComment;

    /**
     * 是否可以转发1-是 0否
     */
    private Boolean isForward;

    /**
     * 是否已经下架1-是 0否
     */
    private Boolean isDown;

    /**
     * 是否已经删除1-是 0-否
     */
    private Boolean isDelete;

    /**
     * 自媒体新闻id
     */
    private Integer wmNewsId;

    /**
     * 静态页面路径
     */
    private String staticUrl;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
