package com.heima.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 热文章表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApHotArticles extends Model<ApHotArticles> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer entryId;

    /**
     * 频道ID
     */
    private Integer tagId;

    /**
     * 频道名称
     */
    private String tagName;

    /**
     * 热度评分
     */
    private Integer score;

    /**
     * 文章ID
     */
    private Long articleId;

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
    private Integer countyId;

    /**
     * 是否阅读
     */
    private Integer isRead;

    private Date releaseDate;

    /**
     * 创建时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
