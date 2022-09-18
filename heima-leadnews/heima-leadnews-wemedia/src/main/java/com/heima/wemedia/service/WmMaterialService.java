package com.heima.wemedia.service;

import com.heima.common.dtos.PageResult;
import com.heima.model.media.dtos.WmMaterialDto;
import com.heima.wemedia.entity.WmMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 自媒体图文素材信息表 服务类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
public interface WmMaterialService extends IService<WmMaterial> {

    String upload(MultipartFile multipartFile);

    PageResult<WmMaterial> findByPage(WmMaterialDto dto);
}
