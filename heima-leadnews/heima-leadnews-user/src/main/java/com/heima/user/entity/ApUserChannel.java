package com.heima.user.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP用户频道信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApUserChannel extends Model<ApUserChannel> {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 用户ID
     */
    private Integer channelId;

    /**
     * 文章ID
     */
    private Integer userId;

    private String name;

    /**
     * 频道排序
     */
    private Integer ord;

    /**
     * 登录时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
