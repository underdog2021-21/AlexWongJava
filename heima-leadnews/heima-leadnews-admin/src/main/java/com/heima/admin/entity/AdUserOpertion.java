package com.heima.admin.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 管理员操作行为信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdUserOpertion extends Model<AdUserOpertion> {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 登录设备ID
     */
    private Integer equipmentId;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 登录地址
     */
    private String address;

    /**
     * 操作类型
     */
    private Integer type;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 登录时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
