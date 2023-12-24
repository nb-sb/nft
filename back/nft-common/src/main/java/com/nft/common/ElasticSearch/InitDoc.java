//package com.nft.common.ElasticSearch;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.IndexOperations;
//
//import javax.annotation.Resource;
//
//@Configuration
//public class InitDoc {
//    @Autowired
//    private ElasticsearchRestTemplate elasticTemplate;
//    @Autowired
//    public InitDoc() {
////        /获取索引对象
//        IndexOperations indexOperations = elasticTemplate.indexOps(ConllectionInfoVo.class);
//        indexOperations.delete();
//        //判断是否存贮索引
//        boolean exists = indexOperations.exists();
//        if (!exists) {
//            //根据这个class对象创建索引
//            try {
//                indexOperations.create();
//            } catch (Exception e) {
//                System.err.println(e);
//            }
//        }
//    }
//}
