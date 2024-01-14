package com.nft.domain.nft.service.impl;

import com.nft.domain.nft.service.INftInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Service
@Log4j2
@AllArgsConstructor
public class NftInfoService implements INftInfoService {

//    private final ElasticsearchRestTemplate elasticTemplate ;
//    @PostConstruct
//    public void init() {
//        //获取索引对象
//        IndexOperations indexOperations = elasticTemplate.indexOps(ConllectionInfoVo.class);
//        //判断是否存贮索引
//        boolean exists = indexOperations.exists();
//        if (!exists) {
//            //根据这个class对象创建索引
//            try {
//                indexOperations.create();
//                System.out.println("构建索引成功");
//                //获取映射
//                Document mapping = indexOperations.createMapping(ConllectionInfoVo.class);
//                //将映射放入索引
//                indexOperations.putMapping(mapping);
//            } catch (Exception e) {
//                System.err.println(e);
//            }
//        }
//    }


}
