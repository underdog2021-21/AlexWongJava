package com.heima.wemedia.entity;

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
 * 自媒体图文内容信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WmNews extends Model<WmNews> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 自媒体用户ID
     */
    private Integer wmUserId;

    /**
     * 标题
     */
    private String title;

    /**
     * 图文内容
     */
    private String content;

    /**
     * 文章布局
            0 无图文章
            1 单图文章
            3 多图文章
     */
    private Integer type;

    /**
     * 图文频道ID
     */
    private Integer channelId;

    private String labels;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 提交时间
     */
    private Date submitedTime;

    /**
     * 当前状态
 0 草稿
 1 提交（待审核）
2 审核失败
 3 待人工审核
 4 人工审核通过
 ,待发布 8 审核通过（待发布）
9 已发布
     */
    private Integer status;

    /**
     * 定时发布时间，不定时则为空
     */
    private Date publishTime;

    /**
     * 拒绝理由
     */
    private String reason;

    /**
     * 发布库文章ID
     */
    private Long articleId;

    /**
     * 封面图片信息   //图片用逗号分隔
     */
    private String images;

    /**
     * 上下架状态 0- 下架 1-上架
     */
    private Boolean enable;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
