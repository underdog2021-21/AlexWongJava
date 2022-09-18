package com.heima.wemedia.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自媒体子账号权限信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WmUserAuth extends Model<WmUserAuth> {

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
     * 资源类型
            0 菜单
            1 频道
            2 按钮
     */
    private Integer type;

    /**
     * 资源名称
     */
    private String name;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
