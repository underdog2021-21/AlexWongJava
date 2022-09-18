package com.heima.wemedia.controller;

import com.heima.common.dtos.PageResult;
import com.heima.common.dtos.ResponseResult;
import com.heima.model.media.dtos.WmNewsDto;
import com.heima.model.media.dtos.WmNewsPageReqDto;
import com.heima.model.media.dtos.WmNewsResultDTO;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author Lenovo
 * @Date 2022/9/6 10:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    @PostMapping("/page")
    public PageResult<WmNewsResultDTO> findByPage(@RequestBody WmNewsPageReqDto dto) {
        return wmNewsService.findByPage(dto);
    }

    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody WmNewsDto dto) {
        wmNewsService.submit(dto);
        return ResponseResult.ok();
    }


    /**
     * @param id
     * @return WmNewsResultDTO
     * @Description: 根据主键id查询文章
     * @Author wangzifeng
     * @CreateTime 2022/9/6 17:51
     */
    @GetMapping("/one/{id}")
    public ResponseResult<WmNewsResultDTO> fingById(@PathVariable("id") Integer id) {
        return ResponseResult.ok(wmNewsService.findById(id));
    }

    @DeleteMapping("/del_news/{id}")
    public ResponseResult deleteById(@PathVariable("id") Integer id) {
        wmNewsService.removeById(id);
        return ResponseResult.ok();
    }

    @PutMapping("/down_or_up")
    public ResponseResult downOrUp(@RequestBody WmNewsDto dto) {
        wmNewsService.downOrUp(dto);
        return ResponseResult.ok();
    }

}
