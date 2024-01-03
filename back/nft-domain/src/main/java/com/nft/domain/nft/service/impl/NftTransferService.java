package com.nft.domain.nft.service.impl;

import com.nft.common.Constants;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Utils.TimeUtils;
import com.nft.domain.nft.model.req.TransferReq;
import com.nft.domain.nft.model.vo.DetailInfoVo;
import com.nft.domain.nft.model.vo.OwnerShipVo;
import com.nft.domain.nft.repository.IDetailInfoRespository;
import com.nft.domain.nft.repository.INftSellRespository;
import com.nft.domain.nft.repository.IOrderInfoRespository;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.domain.nft.service.INftTransferService;
import com.nft.domain.support.Token2User;
import com.nft.domain.support.ipfs.IpfsService;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Log4j2
@Service
@AllArgsConstructor
public class NftTransferService implements INftTransferService {
    private final INftSellRespository iNftSellRespository;
    private final Token2User token2User;
    private final IpfsService ipfsService;
    private final IUserInfoRepository iUserInfoRepository;
    private final IOrderInfoRespository iOrderInfoRespository;
    private final IOwnerShipRespository iOwnerShipRespository;
    private final IDetailInfoRespository iDetailInfoRespository;
    @Override
    public void transferCollection(TransferReq transferReq,HttpServletRequest httpServletRequest) {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return;
        String privatekey = userOne.getPrivatekey();
        String fromAddress = userOne.getAddress();
        String toAddress = transferReq.getToAddress();
        Integer id = transferReq.getId(); //所属藏品id
        //验证是否是自己的藏品
        OwnerShipVo myConllection = iOwnerShipRespository.getMyConllection(id, fromAddress);
        if (myConllection == null) {
            //返回 藏品不是你的
            System.err.println("藏品不存在");
            return;
        }
        //更新数据库中所属数据
        boolean b = iOwnerShipRespository.transferCollection(fromAddress, toAddress, id);
        System.out.println("更新藏品数据" + b);
//        调用区块链中转移方法 -- 合约中自动添加流水表 //使用所有者进行调用
        iOwnerShipRespository.transferCollectionByFisco(privatekey, toAddress, id);
        //更新流水表进行记录
//        DetailInfoVo detailInfoVo = new DetailInfoVo();
//        detailInfoVo.setTime(TimeUtils.getCurrent())
//                .setHash(myConllection.getHash())
//                .setTransferAddress(myConllection.getAddress())
//                .setTargetAddress(toAddress)
//                .setDigitalCollectionId(myConllection.getDigitalCollectionId())
//                .setType(Constants.CollectionOwnerShipType.transfer);
//        iDetailInfoRespository.addDetailInfo(detailInfoVo);


    }
}
