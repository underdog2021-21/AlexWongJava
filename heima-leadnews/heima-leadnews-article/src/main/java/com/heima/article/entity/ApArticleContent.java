package com.heima.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP已发布文章内容表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApArticleContent extends Model<ApArticleContent> {

private static final long serialVersionUID=1L;

    /**
     * 文章ID
     */
    @TableId(value = "article_id",type = IdType.INPUT)
    private Long articleId;

    /**
     * 文章内容
     */
    private String content;


    @Override
    protected Serializable pkVal() {
        return this.articleId;
    }

}
