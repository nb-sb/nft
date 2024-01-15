package com.nft.domain.nft.repository;

import com.nft.domain.nft.model.entity.OwnerShipEntity;
import com.nft.domain.nft.model.vo.OwnerShipVo;

import java.util.List;
/**
* @author: 戏人看戏
* @Date: 2024/1/7 16:31
* @Description: 用户所属藏品的操作
*/
public interface IOwnerShipRespository {
    //添加用户所属藏品
    boolean creat(OwnerShipEntity ownerShipEntity);
    //添加用户所属藏品到区块链中
    boolean addUserConllectionByFisco(String address,String hash) ;

    List selectOWnerShipInfoByFisco(String address, String hash);

    OwnerShipVo selectOWnerShipInfo(String address, String hash);

    OwnerShipVo getMyConllection(Integer id, String fromAddress);

    //转移藏品
    boolean transferCollection(String fromAddress ,String toAddress,Integer id);
    //转移藏品
    boolean transferCollectionByFisco(String privatekey ,String hash,String toAddress,Integer id);
    //获取用于所属藏品 By address

}
