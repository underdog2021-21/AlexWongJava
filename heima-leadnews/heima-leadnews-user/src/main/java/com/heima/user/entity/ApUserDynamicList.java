package com.heima.user.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP用户动态列表
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApUserDynamicList extends Model<ApUserDynamicList> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 动态ID
     */
    private Integer dynamicId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
