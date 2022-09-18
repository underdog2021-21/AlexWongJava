package com.heima.admin.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单资源信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdMenu extends Model<AdMenu> {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单代码
     */
    private String code;

    /**
     * 父菜单
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
