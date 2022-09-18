package com.heima.wemedia.controller;

import com.heima.common.dtos.PageResult;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.media.dtos.WmMaterialDto;
import com.heima.wemedia.entity.WmMaterial;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Lenovo
 * @Date 2022/9/5 14:30
 * @Version 1.0
 */
@RestController
public class MaterialController {
    @Autowired
    private WmMaterialService wmMaterialService;

    @PostMapping("/api/v1/material/upload_picture")
    public ResponseResult<String> upload(MultipartFile multipartFile) {

        return ResponseResult.ok(wmMaterialService.upload(multipartFile));
    }

    @PostMapping("/api/v1/material/list")
    public PageResult<WmMaterial> findByPage(@RequestBody WmMaterialDto dto) {
        return wmMaterialService.findByPage(dto);
    }

    @DeleteMapping("/api/v1/material/del_picture/{id}")
    public ResponseResult deleteById(@PathVariable("id") Integer id) {
        wmMaterialService.removeById(id);
        return ResponseResult.ok();
    }

}
