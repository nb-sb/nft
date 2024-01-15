package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.common.Constants;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.common.Utils.TimeUtils;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.OrderInfoVo;
import com.nft.domain.order.model.entity.OrderEntity;
import com.nft.domain.order.respository.IOrderInfoRespository;
import com.nft.infrastructure.dao.OrderInfoMapper;
import com.nft.infrastructure.po.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Log4j2
@AllArgsConstructor
public class IOrderInfoImpl implements IOrderInfoRespository {
    private  final OrderInfoMapper orderInfoMapper;

    @Override
    public boolean creat(OrderEntity orderEntity) {
        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setOrderNo(orderEntity.getOrderNo());
        orderInfo.setUserId(orderEntity.getUserId());
        orderInfo.setProductId(orderEntity.getProductId());
        orderInfo.setProductImg(orderEntity.getProductImg());
        orderInfo.setProductName(orderEntity.getProductName());
        orderInfo.setProductPrice(orderEntity.getProductPrice());
        orderInfo.setSeckillPrice(orderEntity.getSeckillPrice());
        orderInfo.setStatus(orderEntity.getStatus());
        orderInfo.setPayDate(orderEntity.getPayDate());
        orderInfo.setInitDate(orderEntity.getInitDate());

        int insert = orderInfoMapper.insert(orderInfo);
        return insert > 0;
    }

    @Override
    public OrderEntity selectOrderInfoByNumber(String orderNumber) {
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderInfo::getOrderNo, orderNumber).select();
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        OrderEntity order = BeanCopyUtils.convertTo(orderInfo, OrderEntity ::new);
        return order;
    }



    //    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public boolean save(OrderEntity orderEntity) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderEntity.getId());
        orderInfo.setOrderNo(orderEntity.getOrderNo());
        orderInfo.setUserId(orderEntity.getUserId());
        orderInfo.setProductId(orderEntity.getProductId());
        orderInfo.setProductImg(orderEntity.getProductImg());
        orderInfo.setProductName(orderEntity.getProductName());
        orderInfo.setProductPrice(orderEntity.getProductPrice());
        orderInfo.setSeckillPrice(orderEntity.getSeckillPrice());
        orderInfo.setStatus(orderEntity.getStatus());
        orderInfo.setInitDate(orderEntity.getInitDate());
        orderInfo.setStatus(orderEntity.getStatus());
        try {
            String data = Constants.DATE_FORMAT.format(new Date());
            Date parse = Constants.DATE_FORMAT.parse(data);
            orderInfo.setPayDate(parse);
        } catch (Exception e) {
            log.error(e);
        }
        int update = orderInfoMapper.updateById(orderInfo);
        return update > 0;
    }


    @Override
    public Integer getOrderStatus(String orderNumber) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper();
        wrapper.eq(OrderInfo::getOrderNo, orderNumber)
                .select(OrderInfo::getStatus);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        return orderInfo.getStatus();
    }


    @Override
    public List<OrderInfoVo> selectOrderInfoByUser(Integer userId, Integer collectionId, Integer orderStatus) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("product_id", collectionId)
                .eq("status", orderStatus);
        List<OrderInfo> orderInfos = orderInfoMapper.selectList(queryWrapper);
        List<OrderInfoVo> orderInfoVo = BeanCopyUtils.convertListTo(orderInfos, OrderInfoVo ::new);
        return orderInfoVo;
    }

    @Override
    public Integer selectOrderStatusByUser(Integer userId, String orderNumber) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("order_no", orderNumber);
        OrderInfo orderInfos = orderInfoMapper.selectOne(queryWrapper);
        return orderInfos.getStatus();
    }
}
