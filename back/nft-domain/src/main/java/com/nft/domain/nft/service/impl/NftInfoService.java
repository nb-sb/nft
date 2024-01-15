package com.nft.domain.nft.service.impl;

import com.nft.domain.apply.model.entity.SubmitSellEntity;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.nft.model.entity.SellInfoEntity;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.repository.ISellInfoRespository;
import com.nft.domain.nft.service.INftInfoService;
import com.nft.domain.nftSort.repository.INftRelationshipRespository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.search.aggregations.metrics.InternalSum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@Log4j2
@AllArgsConstructor
public class NftInfoService implements INftInfoService {
    private final ISellInfoRespository iSellInfoRespository;
    private final ISubmitCacheRespository iSubmitCacheRespository;
    private final INftRelationshipRespository iNftRelationshipRespository;
    @Override
    public ConllectionInfoVo selectByCollectId(Integer id) {
        SellInfoEntity sellInfoEntity = iSellInfoRespository.selectSellInfoById(id);

        SubmitSellEntity submitSellEntity = iSubmitCacheRespository.selectById(id);

        ConllectionInfoVo conllectionInfoVo = new ConllectionInfoVo();
        conllectionInfoVo.setPath(submitSellEntity.getPath());
        conllectionInfoVo.setPresent(submitSellEntity.getPresent());
        conllectionInfoVo.setName(submitSellEntity.getName());
        conllectionInfoVo.setPrice(submitSellEntity.getPrice());

        conllectionInfoVo.setId(sellInfoEntity.getId());
        conllectionInfoVo.setHash(sellInfoEntity.getHash());
        conllectionInfoVo.setAmount(sellInfoEntity.getAmount());
        conllectionInfoVo.setRemain(sellInfoEntity.getRemain());
        conllectionInfoVo.setAuther(sellInfoEntity.getAuther());
        conllectionInfoVo.setStatus(sellInfoEntity.getStatus());
        conllectionInfoVo.setIpfsHash(sellInfoEntity.getIpfsHash());
        //查询藏品mid
        Integer mid = iNftRelationshipRespository.loadMid(id);
        conllectionInfoVo.setMid(BigDecimal.valueOf(mid));
        return conllectionInfoVo;
    }

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
