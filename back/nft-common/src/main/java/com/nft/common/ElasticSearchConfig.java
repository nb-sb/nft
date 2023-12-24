//package com.nft.common;
//
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ElasticSearchConfig {
//
////    public static final RequestOptions COMMON_OPTIONS;
////
////    static {
////        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
////        COMMON_OPTIONS = builder.build();
////    }
////    @Bean
////    public RestHighLevelClient esRestClient() {
//////        RestClientBuilder builder = null;
//////        builder = RestClient.builder(new HttpHost("47.117.169.254", 9200, "http"));
//////        RestHighLevelClient client =  new RestHighLevelClient(builder);
////        RestHighLevelClient client = new RestHighLevelClient(
////                RestClient.builder(
////                        new HttpHost("47.117.169.254", 9200, "http")));
////        //没有集群不用指定多个
//////                        new HttpHost("localhost", 9201, "http"))
////        return client;
////    }
//    @Bean
//    public RestHighLevelClient restClient() {
//        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(new HttpHost("47.117.169.254", 9200)));
//        return restClient;
//    }
//
//}
