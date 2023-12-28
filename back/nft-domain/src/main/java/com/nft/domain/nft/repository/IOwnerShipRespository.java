package com.nft.domain.nft.repository;

import com.nft.domain.nft.model.req.AddUserConllection2MysqlReq;

import java.util.List;

public interface IOwnerShipRespository {
    //添加用户所属藏品
    boolean addUserConllection(AddUserConllection2MysqlReq sellInfoVo, String UserAddress);
    //添加用户所属藏品到区块链中
    boolean addUserConllectionByFisco(String address,String hash) ;

    List selectOWnerShipInfoByFisco(String address, String hash);

    void selectOWnerShipInfo(String address, String hash);
    //获取用于所属藏品 By address

}
