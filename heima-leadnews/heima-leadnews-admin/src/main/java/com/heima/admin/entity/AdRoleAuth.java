package com.heima.admin.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色权限信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdRoleAuth extends Model<AdRoleAuth> {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 资源类型
            0 菜单
            1 功能
     */
    private Boolean type;

    /**
     * 资源ID
     */
    private Integer entryId;

    /**
     * 登录时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
