package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.OrderInfoVo;
import com.nft.domain.nft.repository.IOrderInfoRespository;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.infrastructure.dao.OrderInfoMapper;
import com.nft.infrastructure.po.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Repository
@Log4j2
@AllArgsConstructor
public class IOrderInfoImpl implements IOrderInfoRespository {
    private  final OrderInfoMapper orderInfoMapper;
    @Override
    public void addOrderInfo(ConllectionInfoVo conllectionInfoVo, Integer userid) {

    }

    @Override
    public OrderInfoVo selectOrderInfoByNumber(String orderNumber) {
        QueryWrapper<OrderInfo> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("order_no", orderNumber);
        OrderInfo orderInfo = orderInfoMapper.selectOne(orderWrapper);
        OrderInfoVo orderInfoVo = BeanCopyUtils.convertTo(orderInfo, OrderInfoVo ::new);
        return orderInfoVo;
    }
}
