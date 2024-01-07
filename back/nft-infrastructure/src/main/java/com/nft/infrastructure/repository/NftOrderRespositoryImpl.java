package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.nft.model.vo.OrderInfoVo;
import com.nft.domain.order.respository.INftOrderRespository;
import com.nft.infrastructure.dao.OrderInfoMapper;
import com.nft.infrastructure.po.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.time.temporal.ValueRange;
import java.util.List;

@Repository
@Log4j2
@AllArgsConstructor
public class NftOrderRespositoryImpl implements INftOrderRespository {

    private final OrderInfoMapper orderInfoMapper;



    @Override
    public List<OrderInfoVo> selectAllOrder(Page page) {
        Page<OrderInfo> orderInfoPage = new Page<>(page.getCurrent(),page.getSize());
        Page<OrderInfo> orderInfos = orderInfoMapper.selectPage(orderInfoPage, null);
        List<OrderInfo> records = orderInfos.getRecords();
        if (records.size() == 0) {
            return null;
        }
        List<OrderInfoVo> orderInfoVos = BeanCopyUtils.convertListTo(records, OrderInfoVo ::new);
        return orderInfoVos;
    }


}
