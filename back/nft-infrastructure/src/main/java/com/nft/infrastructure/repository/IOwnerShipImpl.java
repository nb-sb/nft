package com.nft.infrastructure.repository;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.nft.domain.nft.model.req.AddUserConllection2MysqlReq;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.infrastructure.dao.OwnerShipMapper;
import com.nft.infrastructure.fisco.model.bo.OwnershipStorageAddOwnershipInputBO;
import com.nft.infrastructure.fisco.model.bo.OwnershipStorageSelectInfoInputBO;
import com.nft.infrastructure.fisco.service.OwnershipStorageService;
import com.nft.infrastructure.po.OwnerShip;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
@Log4j2
public class IOwnerShipImpl implements IOwnerShipRespository {
    private final OwnershipStorageService ownershipStorageService;
    private final OwnerShipMapper ownerShipMapper;

    @Override
    public boolean addUserConllection(AddUserConllection2MysqlReq req, String userAddress) {
        OwnerShip ownerShip = new OwnerShip();


        ownerShip.setAddress(userAddress)
                .setTime(req.getTime())
                .setType(req.getType())
                .setHash(req.getHash())
                .setDigitalCollectionId(req.getDigital_collection_id());
        int insert = ownerShipMapper.insert(ownerShip);
        return insert > 0;
    }

    @Override
    public boolean addUserConllectionByFisco(String address,String hash)  {
        OwnershipStorageAddOwnershipInputBO inputBO = new OwnershipStorageAddOwnershipInputBO();
        inputBO.set_address(address);
        inputBO.set_hash(hash);
        try {
            TransactionResponse response = ownershipStorageService.addOwnership(inputBO);
            System.err.println(response);
            if (response.getValues() == null) {
                log.error(response.getReceiptMessages());
                return false;
            }
            log.info("用户绑定藏品的结果为："+response.getValues());
            JSONArray jsonArray = JSONUtil.parseArray(response.getValues());
            return (boolean) jsonArray.get(0);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public List selectOWnerShipInfoByFisco(String address, String hash)  {
        OwnershipStorageSelectInfoInputBO input = new OwnershipStorageSelectInfoInputBO();
        input.set_address(address);
        input.set_hash(hash);
        try {
            TransactionResponse x = ownershipStorageService.selectInfo(input);
            return JSONUtil.parseArray(x.getValues());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }
}
