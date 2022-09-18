package com.heima.wemedia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自媒体图文引用素材信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WmNewsMaterial extends Model<WmNewsMaterial> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 素材ID
     */
    private Integer materialId;

    /**
     * 图文ID
     */
    private Integer newsId;

    /**
     * 引用类型
            0 内容引用
            1 主图引用
     */
    private Integer type;

    /**
     * 引用排序
     */
    private Integer ord;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
