package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.wemedia.entity.WmNewsMaterial;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsMaterialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自媒体图文引用素材信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Service
public class WmNewsMaterialServiceImpl extends ServiceImpl<WmNewsMaterialMapper, WmNewsMaterial> implements WmNewsMaterialService {
    @Autowired
    private WmNewsMaterialService wmNewsMaterialService;


    @Override
    public void saveBatchNewsAndMaterial(Integer wmNewsId, List<Integer> ids, int type) {

        List<WmNewsMaterial> list = new ArrayList<>();
        for (Integer id : ids) {
            WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
            wmNewsMaterial.setMaterialId(id);
            wmNewsMaterial.setNewsId(wmNewsId);
            wmNewsMaterial.setType(type);
            list.add(wmNewsMaterial);
        }
        saveBatch(list);
    }


    @Override
    public void removeByNewsId(Integer id) {

        LambdaQueryWrapper<WmNewsMaterial> wmNewsMaterialLambdaQueryWrapper = new LambdaQueryWrapper<>();
        wmNewsMaterialLambdaQueryWrapper.eq(WmNewsMaterial::getNewsId, id);
        // WmNewsMaterial newsMaterial = wmNewsMaterialService.getOne(wmNewsMaterialLambdaQueryWrapper);
        //Integer newsMaterialId = newsMaterial.getId();
        //wmNewsMaterialService.removeById(newsMaterialId);
        remove(wmNewsMaterialLambdaQueryWrapper);
    }

}
