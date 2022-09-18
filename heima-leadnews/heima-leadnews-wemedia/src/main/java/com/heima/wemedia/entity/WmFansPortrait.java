package com.heima.wemedia.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自媒体粉丝画像信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WmFansPortrait extends Model<WmFansPortrait> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 账号ID
     */
    private Integer userId;

    /**
     * 画像项目
     */
    private String name;

    /**
     * 资源名称
     */
    private String value;

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
