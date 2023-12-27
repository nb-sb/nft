package com.nft.domain.nft.repository;

public interface IOwnerShipRespository {
    //添加用户所属藏品
    void addUserConllection();
    //添加用户所属藏品到区块链中
    boolean addUserConllectionByFisco(String address,String hash) ;
    //获取用于所属藏品 By address

}
