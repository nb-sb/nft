package com.nft.app.process.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.app.process.order.dto.OrderByStatusQuery;
import com.nft.common.ElasticSearch.ElasticSearchUtils;
import com.nft.common.ElasticSearch.UserOrderSimpleES;
import com.nft.common.PageRequest;
import com.nft.common.Result;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.nft.model.vo.OrderInfoVo;
import com.nft.domain.order.model.res.OrderRes;
import com.nft.domain.order.model.vo.UserOrderSimpleVo;
import com.nft.domain.order.respository.INftOrderRespository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
@Log4j2
@Service
@AllArgsConstructor
public class OrderQueryService {
    private final INftOrderRespository iNftOrderRespository;
    private final ElasticSearchUtils elasticSearchUtils;
    //查询所有订单信息
    public Result pageList(PageRequest pageRequest) {
        List<OrderInfoVo> orderInfoVos = iNftOrderRespository.selectAllOrder(
                new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize()));
        if (orderInfoVos != null) {
            return OrderRes.success(orderInfoVos);
        }
        return OrderRes.success("success");
    }
    //查询自己的订单
    public Result getOrder(Integer userId) {
        //查询es
        try {
            List<UserOrderSimpleES> userOrderSimpleES = elasticSearchUtils.searchUserOrder(userId, UserOrderSimpleES.class);
            if (userOrderSimpleES.size() > 0) return OrderRes.success(userOrderSimpleES);
        } catch (Exception e) {
            log.error(e);
        }
        List<UserOrderSimpleVo> orderInfoVos =  iNftOrderRespository.getOrder(userId);
        if (orderInfoVos.size() == 0) return OrderRes.success(null);
        //添加至es
        try {
            List<UserOrderSimpleES> orderInfoVos2 = BeanCopyUtils.convertListTo(orderInfoVos, UserOrderSimpleES::new);
            for (UserOrderSimpleES orderSimpleES : orderInfoVos2) {
                orderSimpleES.setUserId(userId);
            }
            elasticSearchUtils.insertList(orderInfoVos2);
        } catch (Exception e) {
            log.error(e);
        }
        return OrderRes.success(orderInfoVos);
    }
    //查询用户给指定订单信息
    public Result getOrder(Integer userId, String orderId) {
        List<OrderInfoVo> orderInfoVos =  iNftOrderRespository.getOrder(userId,orderId);
        return OrderRes.success(orderInfoVos);
    }
    //查询用户指定状态订单
    public Result getOrderByStatus(OrderByStatusQuery  query) {
        List<UserOrderSimpleVo> orderInfoVos =  iNftOrderRespository.getOrderByStatus(query.getUserId(),query.getPayOrderStatus());
        return OrderRes.success(orderInfoVos);
    }
}
