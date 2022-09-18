package com.heima.wemedia.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自媒体子账号信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WmSubUser extends Model<WmSubUser> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 主账号ID
     */
    private Integer parentId;

    /**
     * 子账号ID
     */
    private Integer childrenId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
