package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.common.Constants;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.common.Utils.OrderNumberUtil;
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
    public boolean addOrderInfo(ConllectionInfoVo conllectionInfoVo, Integer userid) {
        OrderInfo orderInfo = new OrderInfo();
        String s = OrderNumberUtil.generateOrderNumber(userid, conllectionInfoVo.getId());
        System.err.println(s);
        orderInfo.setUserId(userid)
                .setOrderNo(s)
                .setStatus(Constants.payOrderStatus.NO_PAY)
                .setProductImg(conllectionInfoVo.getPath())
                .setProductId(conllectionInfoVo.getId())
                .setProductName(conllectionInfoVo.getName())
                .setProductPrice(conllectionInfoVo.getPrice())
                .setSeckillPrice(conllectionInfoVo.getPrice());
        int insert = orderInfoMapper.insert(orderInfo);
        if (insert >0) return true;
        return false;
    }

    @Override
    public OrderInfoVo selectOrderInfoByNumber(String orderNumber) {
        QueryWrapper<OrderInfo> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("order_no", orderNumber);
        OrderInfo orderInfo = orderInfoMapper.selectOne(orderWrapper);
        OrderInfoVo orderInfoVo = BeanCopyUtils.convertTo(orderInfo, OrderInfoVo ::new);
        return orderInfoVo;
    }

    @Override
    public boolean updateOrderStatus(String orderNumber, Integer status) {

        return false;
    }
}
