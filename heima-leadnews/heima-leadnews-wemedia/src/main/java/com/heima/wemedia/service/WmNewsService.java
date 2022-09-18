package com.heima.wemedia.service;

import com.heima.common.dtos.PageResult;
import com.heima.model.media.dtos.WmNewsDto;
import com.heima.model.media.dtos.WmNewsPageReqDto;
import com.heima.model.media.dtos.WmNewsResultDTO;
import com.heima.wemedia.entity.WmNews;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 自媒体图文内容信息表 服务类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
public interface WmNewsService extends IService<WmNews> {

    PageResult<WmNewsResultDTO> findByPage(WmNewsPageReqDto dto);

    void submit(WmNewsDto dto);

    /**
     * @Description: 根据主键id查询文章
     * @param id
     * @return WmNewsResultDTO
     * @Author wangzifeng
     * @CreateTime 2022/9/6 17:51
     */
    WmNewsResultDTO findById(Integer id);

    /**
     * 自媒体文章上下架
     * @param dto
     */
    void downOrUp(WmNewsDto dto);

}
