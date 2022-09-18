package com.heima.admin.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 页面功能信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdFunction extends Model<AdFunction> {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 功能名称
     */
    private String name;

    /**
     * 功能代码
     */
    private String code;

    /**
     * 父功能
     */
    private Integer parentId;

    /**
     * 登录时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
