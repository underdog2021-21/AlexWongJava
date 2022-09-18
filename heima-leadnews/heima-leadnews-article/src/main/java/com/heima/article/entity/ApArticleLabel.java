package com.heima.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章标签信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApArticleLabel extends Model<ApArticleLabel> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long articleId;

    /**
     * 标签ID
     */
    private Integer labelId;

    /**
     * 排序
     */
    private Integer count;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
