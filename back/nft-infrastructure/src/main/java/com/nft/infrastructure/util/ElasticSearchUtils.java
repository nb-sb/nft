package com.nft.infrastructure.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
* @author: 戏人看戏
* @Date: 2023/12/21 14:20
* @Description: es 工具类 网上没找到比较好用的只能自己写一个基于elasticTemplate的工具类
*/
@Repository
public class ElasticSearchUtils {
    @Autowired
    ElasticsearchRestTemplate elasticTemplate;
    //分页查询
    public <T> List<T> searchByPage(Page<T> page, Class<T> clazz) {
        int pageNumber = (int) (page.getCurrent() - 1);
        int pageSize = (int) page.getSize();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))//使用id进行倒叙排序先展示最新出的藏品
                .withPageable(pageable)
                .build();
        SearchHits<T> search = elasticTemplate.search(query, clazz);
        ArrayList<T> ts = new ArrayList<>();
        for (SearchHit<T> searchHit : search.getSearchHits()) {
//            System.out.println("searchHit : "+searchHit.getContent());
            //查询到对应的类信息后加入到列表中
            ts.add(searchHit.getContent());
        }
        return ts;
    }
    //分页查询
    public <T> List<T> searchByPage(Page<T> page, String param,Class<T> clazz) {
        int pageNumber = (int) (page.getCurrent() - 1);
        int pageSize = (int) page.getSize();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(param,"mid"))
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))//使用id进行倒叙排序先展示最新出的藏品
                .withPageable(pageable)
                .build();
        SearchHits<T> search = elasticTemplate.search(query, clazz);
        ArrayList<T> ts = new ArrayList<>();
        for (SearchHit<T> searchHit : search.getSearchHits()) {
//            System.out.println("searchHit : "+searchHit.getContent());
            //查询到对应的类信息后加入到列表中
            ts.add(searchHit.getContent());
        }
        return ts;
    }
    public long getTotalCount(Class<?> clazz) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .build();

        return elasticTemplate.count(query, clazz);
    }
    //插入内容
    public <T> void insert(T entity) {
        Class<T> clazz = (Class<T>) entity.getClass();
        T save = elasticTemplate.save(entity);
        System.out.println(save);
    }
    public <T> void insertList(List<T> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return;
        }

        Class<T> clazz = (Class<T>) entityList.get(0).getClass();
        //2.构建批量插入
        List<IndexQuery> queries = new ArrayList<>();

        for (T t : entityList) {
            IndexQuery indexQuery = new IndexQuery();
            //设置ID，如果id相同会覆盖之前的数据
//            indexQuery.setId(t.getId());
            //插入的实体类
            indexQuery.setObject(t);
            //索引
//            indexQuery.setIndexName("abnormal_number_account");
            queries.add(indexQuery);
        }
        elasticTemplate.bulkIndex(queries, clazz);
//        for (T entity : entityList) {
//            insert(entity);
//        }
    }
}
