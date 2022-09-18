package com.heima.user.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
public class ApUserInfo extends Model<ApUserInfo> {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private Integer id;

    private Integer userId;

    /**
     * 真是姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idno;

    /**
     * 公司
     */
    private String company;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 年龄
     */
    private Integer age;

    private Date birthday;

    /**
     * 个人格言
     */
    private String introduction;

    /**
     * 归属地
     */
    private String location;

    /**
     * 粉丝数量
     */
    private Integer fans;

    /**
     * 关注数量
     */
    private Integer follows;

    /**
     * 是否允许推荐我给好友
     */
    private Boolean isRecommendMe;

    /**
     * 是否允许给我推荐好友
     */
    private Boolean isRecommendFriend;

    /**
     * 是否分享页面显示头像
     */
    private Boolean isDisplayImage;

    /**
     * 更新时间
     */
    private Date updatedTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
