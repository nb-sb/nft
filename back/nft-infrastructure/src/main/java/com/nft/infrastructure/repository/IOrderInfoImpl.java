package com.nft.infrastructure.repository;

import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.repository.IOrderInfoRespository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Repository
@Log4j2
@AllArgsConstructor
public class IOrderInfoImpl implements IOrderInfoRespository {
    @Override
    public void addOrderInfo(ConllectionInfoVo conllectionInfoVo, Integer userid) {

    }
}
