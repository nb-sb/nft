package com.nft.domain.nft.repository;

import com.nft.domain.nft.model.req.AddUserConllection2MysqlReq;
import com.nft.domain.nft.model.vo.OwnerShipVo;

import java.util.List;

public interface IOwnerShipRespository {
    //添加用户所属藏品
    boolean addUserConllection(AddUserConllection2MysqlReq sellInfoVo, String UserAddress);
    //添加用户所属藏品到区块链中
    boolean addUserConllectionByFisco(String address,String hash) ;

    List selectOWnerShipInfoByFisco(String address, String hash);

    void selectOWnerShipInfo(String address, String hash);

    OwnerShipVo getMyConllection(Integer id, String fromAddress);

    //转移藏品
    boolean transferCollection(String fromAddress ,String toAddress,Integer id);
    //转移藏品
    boolean transferCollectionByFisco(String privatekey ,String toAddress,Integer id);
    //获取用于所属藏品 By address

}
