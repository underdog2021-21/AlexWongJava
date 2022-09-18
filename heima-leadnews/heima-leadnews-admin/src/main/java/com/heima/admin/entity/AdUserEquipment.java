package com.heima.admin.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 管理员设备信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdUserEquipment extends Model<AdUserEquipment> {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 0PC
            1ANDROID
            2IOS
            3PAD
            9 其他
     */
    private Boolean type;

    /**
     * 设备版本
     */
    private String version;

    /**
     * 设备系统
     */
    private String sys;

    /**
     * 设备唯一号，MD5加密
     */
    private String no;

    /**
     * 登录时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
