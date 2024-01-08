package com.nft.common.ElasticSearch;

import com.nft.common.Utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Configuration
@Log4j2
@AllArgsConstructor
public class InitDoc{
    private final ElasticsearchRestTemplate elasticTemplate;
    private final ElasticSearchUtils elasticSearchUtils;

    @Autowired
    public void InitDocs() {
//        /获取索引对象
        IndexOperations indexOperations = elasticTemplate.indexOps(UserOrderSimpleES.class);
        indexOperations.delete();
        //判断是否存贮索引
        boolean exists = indexOperations.exists();
        if (!exists) {
            //根据这个class对象创建索引
            try {
                log.info("es 创建索引中。。。");
                indexOperations.create();
                UserOrderSimpleES userOrderSimpleES = new UserOrderSimpleES();
                userOrderSimpleES.setUserId(0);
                userOrderSimpleES.setOrderNo("0");
                userOrderSimpleES.setProductName("init");
                Date current = TimeUtils.getCurrent();
                userOrderSimpleES.setInitDate(current);
                userOrderSimpleES.setProductImg("/init.png");
                userOrderSimpleES.setStatus(1);
                userOrderSimpleES.setProductPrice(BigDecimal.valueOf(9.99));
                elasticSearchUtils.insert(userOrderSimpleES);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

    }


}
