package com.heima.common.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

@Setter
public class PageRequestDto {
    @ApiModelProperty(value="每页显示条数",required = true)
    protected Long size;
    @ApiModelProperty(value="当前页",required = true)
    protected Long page;

    public Long getSize() {
        if (this.size == null || this.size <= 0 || this.size > 100) {
            setSize(10L);
        }
        return size;
    }

    public Long getPage() {
        if (this.page == null || this.page <= 0) {
            setPage(1L);
        }
        return page;
    }
}