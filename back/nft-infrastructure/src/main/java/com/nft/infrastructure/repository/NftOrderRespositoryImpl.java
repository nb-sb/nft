package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.nft.model.vo.OrderInfoVo;
import com.nft.domain.order.model.vo.UserOrderSimpleVo;
import com.nft.domain.order.respository.INftOrderRespository;
import com.nft.infrastructure.dao.OrderInfoMapper;
import com.nft.infrastructure.po.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Log4j2
@AllArgsConstructor
public class NftOrderRespositoryImpl implements INftOrderRespository {

    private final OrderInfoMapper orderInfoMapper;



    @Override
    public List<OrderInfoVo> selectAllOrder(Page page) {
        Page<OrderInfo> orderInfoPage = new Page<>(page.getCurrent(),page.getSize());
        page.setOptimizeCountSql(true);
        Page<OrderInfo> orderInfos = orderInfoMapper.selectPage(orderInfoPage, null);
        List<OrderInfo> records = orderInfos.getRecords();
        if (records.size() == 0) {
            return null;
        }
        List<OrderInfoVo> orderInfoVos = BeanCopyUtils.convertListTo(records, OrderInfoVo ::new);
        return orderInfoVos;
    }

    @Override
    public List<UserOrderSimpleVo> getOrder(Integer userId) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper();
        // 商家图片，未删除，可用状态
        wrapper.eq(OrderInfo::getUserId,userId)
                .select(OrderInfo::getOrderNo,
                        OrderInfo::getProductName,
                        OrderInfo::getProductImg,
                        OrderInfo::getInitDate,
                        OrderInfo::getProductPrice); // 只查询指定字段
        List<OrderInfo> orderInfos = orderInfoMapper.selectList(wrapper);
        if (orderInfos.size() == 0) return null;
        List<UserOrderSimpleVo> orderInfoVos = BeanCopyUtils.convertListTo(orderInfos, UserOrderSimpleVo ::new);
        return orderInfoVos;
    }

    @Override
    public List<OrderInfoVo> getOrder(Integer userId, String orderId) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("order_no", orderId);
        List<OrderInfo> orderInfos = orderInfoMapper.selectList(queryWrapper);
        if (orderInfos.size() == 0) return null;
        List<OrderInfoVo> orderInfoVos = BeanCopyUtils.convertListTo(orderInfos, OrderInfoVo ::new);
        return orderInfoVos;
    }


}
