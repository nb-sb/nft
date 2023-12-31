package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.common.Constants;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.common.Utils.TimeUtils;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.OrderInfoVo;
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
    public boolean addOrderInfo(ConllectionInfoVo conllectionInfoVo, Integer userid,String orderNo,Date time) {
        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setUserId(userid)
                .setOrderNo(orderNo)
                .setStatus(Constants.payOrderStatus.NO_PAY)
                .setProductImg(conllectionInfoVo.getPath())
                .setProductId(conllectionInfoVo.getId())
                .setProductName(conllectionInfoVo.getName())
                .setProductPrice(conllectionInfoVo.getPrice())
                .setSeckillPrice(conllectionInfoVo.getPrice())
                .setInitDate(time);
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



    //    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public boolean setPayOrderStatus(String orderNumber, Integer status) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(status);
        try {
            String data = Constants.DATE_FORMAT.format(new Date());
            Date parse = Constants.DATE_FORMAT.parse(data);
            orderInfo.setPayDate(parse);
        } catch (Exception e) {
            log.error(e);
        }
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNumber);
        int update = orderInfoMapper.update(orderInfo, wrapper);
        if (update>0) return true;
        return false;
    }

    @Override
    public boolean setOrderStatus(String orderNumber, Integer status) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(status);
        UpdateWrapper<OrderInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_no", orderNumber);
        int update = orderInfoMapper.update(orderInfo, updateWrapper);
        if (update>0) return true;
        return false;
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
    public OrderInfoVo getOrder(String orderNumber) {
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNumber);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        OrderInfoVo orderInfoVo = BeanCopyUtils.convertTo(orderInfo, OrderInfoVo ::new);
        return orderInfoVo;
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
