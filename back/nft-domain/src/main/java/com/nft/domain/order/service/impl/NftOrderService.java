package com.nft.domain.order.service.impl;


import com.nft.common.ElasticSearch.ElasticSearchUtils;
import com.nft.common.Result;
import com.nft.domain.order.model.res.OrderRes;
import com.nft.domain.order.model.vo.UserOrderSimpleVo;
import com.nft.domain.order.respository.INftOrderRespository;
import com.nft.domain.order.respository.IOrderInfoRespository;
import com.nft.domain.order.service.INftOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;


@Log4j2
@Service
@AllArgsConstructor
public class NftOrderService implements INftOrderService {

    private final INftOrderRespository iNftOrderRespository;
    private final IOrderInfoRespository iOrderInfoRespository;


    //查询用户订单状态
    private Integer getUserOrderStatus(Integer userId, String orderNumber) {
        return iOrderInfoRespository.selectOrderStatusByUser(userId, orderNumber);
    }


}
