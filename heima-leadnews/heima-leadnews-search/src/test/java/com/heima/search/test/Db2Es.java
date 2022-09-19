package com.heima.search.test;

import com.heima.article.client.ArticleFeign;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.search.service.ApUserSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Db2Es {

    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private ApUserSearchService apUserSearchService;

    @Test
    public void testDb2Es(){

        int page=1,size=3;
        while(true) {
//        远程调用article服务，获取文章数据
            List<ArticleDto> articleDtoList = articleFeign.findByPage(page, size);
            if(CollectionUtils.isEmpty(articleDtoList)){
                break;
            }
            System.out.println("#######page=="+page);
//        调用方法导入Es
            apUserSearchService.creatIndexBatch(articleDtoList);
            if(articleDtoList.size()<size){
                break;
            }
            page++;
        }
    }
}

