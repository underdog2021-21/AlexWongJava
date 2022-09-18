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
 * APP收藏信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApCollection extends Model<ApCollection> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Integer apUserId;

    /**
     * 设备号
     */
    private String equipmentId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 收藏内容类型
            0文章
            1动态
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date collectionTime;

    /**
     * 发布时间
     */
    private Date publishedTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
