package com.heima.admin.controller.v1;

import com.heima.admin.entity.AdChannel;
import com.heima.admin.service.AdChannelService;
import com.heima.common.dtos.PageResult;
import com.heima.common.dtos.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.util.BeanHelper;
import com.heima.model.admin.dtos.ChannelDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author Lenovo
 * @Date 2022/9/1 16:42
 * @Version 1.0
 */

@RequestMapping("/api/v1/channel")
@RestController
@Api(tags = "频道管理",description = "频道管理API")
public class AdChannelController {
    @Autowired
    private AdChannelService adChannelService;


    //接受前端数据泛型用dto
    @PostMapping("/list")
    @ApiOperation(value = "根据名称分页查询频道列表")
    @ApiImplicitParam(name = "dto", value = "频道请求对象", required = true, dataType = "ChannelDto")
    public PageResult<ChannelDto> findByPage(@RequestBody ChannelDto channelDto) {
        return adChannelService.findByPage(channelDto);
    }

    @PostMapping("/save")
    public ResponseResult save(@RequestBody ChannelDto channelDto) {
        if (StringUtils.isBlank((channelDto.getName()))) {
            throw new LeadException(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        AdChannel adChannel = BeanHelper.copyProperties(channelDto, AdChannel.class);
        adChannel.setCreatedTime(new Date());
        adChannelService.save(adChannel);
        return ResponseResult.ok();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteById(@PathVariable("id") Integer id) {
        adChannelService.removeById(id);
        return ResponseResult.ok();
    }

    @PutMapping("/update")
    public ResponseResult update(@RequestBody ChannelDto channelDto) {
        AdChannel adChannel = BeanHelper.copyProperties(channelDto, AdChannel.class);
        adChannelService.updateById(adChannel);
        return ResponseResult.ok();
    }
    /**
     * @Description: 下拉框
     * @return ResponseResult<List<AdChannel>>
     * @Author wangzifeng
     * @CreateTime 2022/9/6 10:20
     */
    @GetMapping("/channels")
    public ResponseResult<List<AdChannel>> channels(){

        return ResponseResult.ok(adChannelService.list());
    }
}
