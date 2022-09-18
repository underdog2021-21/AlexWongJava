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
 * 自媒体图文数据统计表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WmNewsStatistics extends Model<WmNewsStatistics> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 主账号ID
     */
    private Integer userId;

    /**
     * 子账号ID
     */
    private Integer article;

    /**
     * 阅读量
     */
    private Integer readCount;

    /**
     * 评论量
     */
    private Integer comment;

    /**
     * 关注量
     */
    private Integer follow;

    /**
     * 收藏量
     */
    private Integer collection;

    /**
     * 转发量
     */
    private Integer forward;

    /**
     * 点赞量
     */
    private Integer likes;

    /**
     * 不喜欢
     */
    private Integer unlikes;

    /**
     * 取消关注量
     */
    private Integer unfollow;

    private String burst;

    /**
     * 创建时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
