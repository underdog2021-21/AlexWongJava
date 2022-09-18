package com.heima.wemedia.service;

import com.heima.wemedia.entity.WmNewsMaterial;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自媒体图文引用素材信息表 服务类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
public interface WmNewsMaterialService extends IService<WmNewsMaterial> {

    /**
     * @Description: 根据文章id（news_id）id删除
     * @param id
     * @return void
     * @Author wangzifeng
     * @CreateTime 2022/9/6 12:20
     */
    void removeByNewsId(Integer id);

    /**
     * @Description: 批量保存文章id和素材id
     * @param wmNewsId
     * @param ids
     * @param type
     * @return void
     * @Author wangzifeng
     * @CreateTime 2022/9/6 12:19
     */
    void saveBatchNewsAndMaterial(Integer wmNewsId, List<Integer> ids, int type);
}
