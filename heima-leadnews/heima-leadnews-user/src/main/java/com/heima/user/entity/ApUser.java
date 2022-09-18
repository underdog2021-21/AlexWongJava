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
 * APP用户信息表
 * </p>
 *
 * @author HM
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApUser extends Model<ApUser> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码,Bcrypt加密
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String image;

    /**
     * 0 男
            1 女
            2 未知
     */
    private Integer sex;

    /**
     * 0 未
            1 是
     */
    private Boolean isCertification;

    /**
     * 是否身份认证
     */
    private Boolean isIdentityAuthentication;

    /**
     * 0 锁定           1正常
     */
    private Boolean status;

    /**
     * 0 普通用户
            1 自媒体人
            2 大V
     */
    private Integer flag;

    /**
     * 注册时间
     */
    private Date createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
