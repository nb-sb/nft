package com.nft.infrastructure.repository;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.infrastructure.fisco.model.bo.OwnershipStorageAddOwnershipInputBO;
import com.nft.infrastructure.fisco.service.OwnershipStorageService;
import com.nft.infrastructure.fisco.service.SellStroageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
@Log4j2
public class IOwnerShipImpl implements IOwnerShipRespository {
    private final OwnershipStorageService ownershipStorageService;
    @Override
    public void addUserConllection() {

    }

    @Override
    public boolean addUserConllectionByFisco(String address,String hash)  {
        OwnershipStorageAddOwnershipInputBO inputBO = new OwnershipStorageAddOwnershipInputBO();
        inputBO.set_address(address);
        inputBO.set_hash(hash);
        try {
            TransactionResponse response = ownershipStorageService.addOwnership(inputBO);
            if (response.getValues() == null) {
                log.error(response.getReceiptMessages());
                return false;
            }
            log.info("出售作品添加到区块链的结果为："+response.getValues());
            JSONArray jsonArray = JSONUtil.parseArray(response.getValues());
            return (boolean) jsonArray.get(0);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
