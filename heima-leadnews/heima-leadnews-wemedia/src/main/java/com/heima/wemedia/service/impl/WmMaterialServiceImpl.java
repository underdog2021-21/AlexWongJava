package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.common.dtos.PageResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.exception.LeadException;
import com.heima.common.threadlocal.UserThreadLocalUtils;
import com.heima.common.util.BeanHelper;
import com.heima.file.service.MinioService;
import com.heima.model.media.dtos.WmMaterialDto;
import com.heima.wemedia.entity.WmMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 自媒体图文素材信息表 服务实现类
 * </p>
 *
 * @author HM
 * @since 2022-09-04
 */
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private MinioService minioService;
    @Override
    public String upload(MultipartFile multipartFile) {
        Integer userId =UserThreadLocalUtils.getUserId();
        //获取源文件名字123.jpg
        String originalFilename = multipartFile.getOriginalFilename();
        //获取文件后缀
        String suffix = StringUtils.substringAfter(originalFilename, ".");
        //创建新的名字
        //原 123.jpg 新 UUID.jpg
        String newName = UUID.randomUUID().toString()+"."+suffix;
        //把文件上传到minio
        try {
            String url = minioService.uploadImgFile(null, newName, multipartFile.getInputStream());
            //保存数据到素材表
            WmMaterial wmMaterial = new WmMaterial();
            wmMaterial.setIsCollection(false);
            wmMaterial.setUserId(userId);
            wmMaterial.setUrl(url);
            wmMaterial.setType(0);
            wmMaterial.setCreatedTime(new Date());
            save(wmMaterial);
            return url;
        } catch (IOException e) {
            log.error("上传失败");
            e.printStackTrace();
            throw new LeadException(AppHttpCodeEnum.UPLOAD_ERROR);
        }
    }

    /**
     * @param dto
     * @return PageResult<WmMaterialDto>
     * @Description: 分页查询:
     * 1.建一个页对象
     * 2.传入前端返回的当前页和页面大小
     * 3.写查询条件
     * 4.分页查询
     * 5.对象转换,返回
     * @Author wangzifeng
     * @CreateTime 2022/9/5 14:50
     */
    @Override
    public PageResult<WmMaterial> findByPage(WmMaterialDto dto) {
        //之前写了拦截器,把userId放在了ThreadLocal里,在这里取出来做查询条件
        Integer userId = UserThreadLocalUtils.getUserId();
        Page<WmMaterial> wmMaterialPage = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //如果前端传参数 是0 那就全部查询 如果是1 就查收藏的数据(看看是否已收藏)
        lambdaQueryWrapper
                //要对用户进行识别,否则所有用户收藏素材均可以被其他人修改
                //应该每个人只能看到自己的收藏和上传的素材
                .eq(WmMaterial::getUserId, userId)
                .eq(dto.getIsCollection() != 0, WmMaterial::getIsCollection, dto.getIsCollection());
        IPage<WmMaterial> page = page(wmMaterialPage, lambdaQueryWrapper);
        List<WmMaterial> records = page.getRecords();
        if (page == null || CollectionUtils.isEmpty(records)) {
            throw new LeadException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        PageResult<WmMaterial> pageResult = new PageResult<>(dto.getPage(), dto.getSize(), page.getTotal(), records);
        return pageResult;
    }
}
