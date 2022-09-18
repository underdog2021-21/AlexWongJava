package com.heima.user.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP用户设备信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApUserEquipment extends Model<ApUserEquipment> {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 设备ID
     */
    private Integer equipmentId;

    /**
     * 上次使用时间
     */
    private Date lastTime;

    /**
     * 登录时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
