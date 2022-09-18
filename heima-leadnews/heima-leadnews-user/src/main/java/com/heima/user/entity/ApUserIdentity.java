package com.heima.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP身份认证信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApUserIdentity extends Model<ApUserIdentity> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号ID
     */
    private Integer userId;

    private String name;

    /**
     * 资源名称
     */
    private String idno;

    /**
     * 正面照片
     */
    private String fontImage;

    /**
     * 背面照片
     */
    private String backImage;

    /**
     * 手持照片
     */
    private String holdImage;

    /**
     * 行业
     */
    private String industry;

    /**
     * 状态
            0 创建中
            1 待审核
            2 审核失败
            9 审核通过
     */
    private Integer status;

    /**
     * 拒绝原因
     */
    private String reason;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 提交时间
     */
    private Date submitedTime;

    /**
     * 更新时间
     */
    private Date updatedTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
