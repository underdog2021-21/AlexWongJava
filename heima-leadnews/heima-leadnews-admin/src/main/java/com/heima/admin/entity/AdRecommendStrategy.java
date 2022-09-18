package com.heima.admin.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 推荐策略信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdRecommendStrategy extends Model<AdRecommendStrategy> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 策略名称
     */
    private String name;

    /**
     * 策略描述
     */
    private String description;

    /**
     * 是否有效
     */
    private Boolean isEnable;

    /**
     * 分组ID
     */
    private Integer groupId;

    /**
     * 创建时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
