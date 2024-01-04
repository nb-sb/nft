package com.nft.infrastructure.repository;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.common.Constants;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.common.Utils.TimeUtils;
import com.nft.domain.nft.model.req.AddUserConllection2MysqlReq;
import com.nft.domain.nft.model.vo.OwnerShipVo;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.infrastructure.dao.OwnerShipMapper;
import com.nft.infrastructure.fisco.model.bo.OwnershipStorageAddOwnershipInputBO;
import com.nft.infrastructure.fisco.model.bo.OwnershipStorageSelectInfoInputBO;
import com.nft.infrastructure.fisco.model.bo.OwnershipStorageTransferInputBO;
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
            log.info("用户绑定藏品的结果为："+response.getValues()+"address: "+address+" , hash : "+hash);
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

    @Override
    public OwnerShipVo selectOWnerShipInfo(String address, String hash) {
        QueryWrapper<OwnerShip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", address).eq("hash", hash);
        OwnerShip ownerShip = ownerShipMapper.selectOne(queryWrapper);
        OwnerShipVo ownerShipVo = BeanCopyUtils.convertTo(ownerShip, OwnerShipVo::new);
        return ownerShipVo;
    }

    @Override
    public OwnerShipVo getMyConllection(Integer id,String fromAddress) {
        QueryWrapper<OwnerShip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", fromAddress).eq("id", id);
        OwnerShip ownerShip = ownerShipMapper.selectOne(queryWrapper);
        OwnerShipVo ownerShipVo = BeanCopyUtils.convertTo(ownerShip, OwnerShipVo::new);
        return ownerShipVo;
    }

    /**
     * @Des 转移藏品的方法
     * @Date 2023/1/2 18：30
     * @Param fromAddress 转移者地址
     * @Param toAddress 接受者地址
     * @Param id 该藏品所属用户表中的id
     * @Return
     */
    @Override
    public boolean transferCollection(String fromAddress, String toAddress, Integer id) {
        OwnerShip ownerShip = new OwnerShip();
        ownerShip.setAddress(toAddress)
                .setTime(TimeUtils.getCurrent())
                .setType(Constants.CollectionOwnerShipType.transfer);
        UpdateWrapper<OwnerShip> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).eq("address", fromAddress);
        int update = ownerShipMapper.update(ownerShip, updateWrapper);
        if (update>0) {
            return true;
        }
        return false;
    }



    @Override
    public boolean transferCollectionByFisco(String privatekey, String hash,String toAddress, Integer id) {
        // TODO: 2024/1/3 设置调用者

        OwnershipStorageTransferInputBO inputBO = new OwnershipStorageTransferInputBO();
        inputBO.set_hash(hash);
        inputBO.setTarget_address(toAddress);
        try {
            TransactionResponse res = ownershipStorageService.transfer(inputBO, privatekey);
            System.err.println(res.getValues());
            if (res.getValues() == null) {
                log.error(res.getReceiptMessages());
                return false;
            }
            log.info("转移藏品："+res.getValues()+" 输入记录 : privatekey :"+privatekey+"  toaddress : "+toAddress);
            JSONArray jsonArray = JSONUtil.parseArray(res.getValues());
            return (boolean) jsonArray.get(0);
        } catch (Exception e) {
            System.out.println(e);
        }

//        System.err.println("qqq : "+qqq);
        return false;
    }
}
