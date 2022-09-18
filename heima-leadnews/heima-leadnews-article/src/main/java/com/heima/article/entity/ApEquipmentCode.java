package com.heima.article.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP设备码信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApEquipmentCode extends Model<ApEquipmentCode> {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 用户ID
     */
    private Integer equipmentId;

    /**
     * 设备码
     */
    private String code;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
