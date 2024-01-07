package com.nft.domain.nft.service.impl;

import com.nft.common.APIException;
import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.common.Utils.TimeUtils;
import com.nft.domain.nft.model.req.TransferReq;
import com.nft.domain.nft.model.vo.DetailInfoVo;
import com.nft.domain.nft.model.vo.OwnerShipVo;
import com.nft.domain.nft.repository.IDetailInfoRespository;
import com.nft.domain.nft.repository.IOwnerShipRespository;
import com.nft.domain.nft.service.INftTransferService;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.vo.UserVo;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@Service
@AllArgsConstructor
public class NftTransferService implements INftTransferService {
    private final Token2User token2User;
    private final IOwnerShipRespository iOwnerShipRespository;
    private final IDetailInfoRespository iDetailInfoRespository;
    @Override
    @Transactional
    public Result transferCollection(TransferReq transferReq, HttpServletRequest httpServletRequest) {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        String privatekey = userOne.getPrivatekey();
        String fromAddress = userOne.getAddress();
        String toAddress = transferReq.getToAddress();
        Integer id = transferReq.getId(); //所属藏品id
        if (StringUtil.equals(fromAddress, toAddress)) {
            return Result.error("不能将藏品转移给自己");
        }
        // TODO: 2024/1/4 判断toAddress是否存在
        //验证是否是自己的藏品
        OwnerShipVo myConllection = iOwnerShipRespository.getMyConllection(id, fromAddress);
        if (myConllection == null) {
            //返回 藏品不是你的
            return Result.error("该藏品不是你的，或藏品不存在");
        }
        boolean b1 = iOwnerShipRespository.transferCollectionByFisco(privatekey,myConllection.getHash(), toAddress, id);
        if (!b1) {
            log.error("区块链中：藏品转移错误");
            throw new APIException(Constants.ResponseCode.NO_UPDATE, "区块链中：藏品转移错误");
        }
        //更新数据库中所属数据
        boolean b = iOwnerShipRespository.transferCollection(fromAddress, toAddress, id);
        if (!b) {
            log.error("更新流水表进行记录错误: "+fromAddress+","+ toAddress+","+id);

        }
//        调用区块链中转移方法 -- 合约中自动添加流水表 //使用所有者进行调用
        //更新流水表进行记录
        DetailInfoVo detailInfoVo = new DetailInfoVo();
        detailInfoVo.setTime(TimeUtils.getCurrent())
                .setHash(myConllection.getHash())
                .setTransferAddress(myConllection.getAddress())
                .setTargetAddress(toAddress)
                .setDigitalCollectionId(myConllection.getDigitalCollectionId())
                .setType(Constants.CollectionOwnerShipType.transfer);
        boolean b2 = iDetailInfoRespository.addDetailInfo(detailInfoVo);
        if (!b2) {
            log.error("更新流水表进行记录错误: "+detailInfoVo);
        }
        return Result.success("藏品转移成功！");
    }
}
