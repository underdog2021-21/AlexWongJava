package com.heima.model.admin.dtos;

import com.heima.common.dtos.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChannelDto extends PageRequestDto {

    private Integer id;
    /**
     * 频道名称
     */
    @ApiModelProperty("频道名称")
    private String name;

    /**
     * 频道描述
     */
    private String description;

    /**
     * 是否默认频道
     */
    private Boolean isDefault;

    /**
     * 是否启用 0- 不启用 1-启用
     */
    private Boolean status;

    /**
     * 默认排序
     */
    private Integer ord;
}