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
 * APP文章作者信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApAuthor extends Model<ApAuthor> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作者名称
     */
    private String name;

    /**
     * 0 爬取数据
            1 签约合作商
            2 平台自媒体人
            
     */
    private Integer type;

    /**
     * 社交账号ID
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 自媒体账号
     */
    private Integer wmUserId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
