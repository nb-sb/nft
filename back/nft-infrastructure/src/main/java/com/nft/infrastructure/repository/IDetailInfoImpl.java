package com.nft.infrastructure.repository;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.nft.model.vo.DetailInfoVo;
import com.nft.domain.nft.repository.IDetailInfoRespository;
import com.nft.infrastructure.dao.DetailInfoMapper;
import com.nft.infrastructure.fisco.model.bo.DetailStorageAddDetailInputBO;
import com.nft.infrastructure.fisco.raw.DetailStorage;
import com.nft.infrastructure.fisco.service.DetailStorageService;
import com.nft.infrastructure.po.DetailInfo;
import com.nft.infrastructure.po.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Var;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.stereotype.Repository;

import java.io.PipedReader;

@Repository
@Log4j2
@AllArgsConstructor
public class IDetailInfoImpl implements IDetailInfoRespository {
    private final DetailInfoMapper detailInfoMapper;
    private final DetailStorageService detailStorageService;
    @Override
    public boolean addDetailInfo(DetailInfoVo detailInfoVo) {
        DetailInfo detailInfo = BeanCopyUtils.convertTo(detailInfoVo, DetailInfo ::new);
        int insert = detailInfoMapper.insert(detailInfo);
        return insert>0;
    }

    @Override
    public boolean addDeailInfoByFisco(DetailInfoVo detailInfoVo) {
        DetailStorageAddDetailInputBO inputBO = new DetailStorageAddDetailInputBO();
        inputBO.set_hash(detailInfoVo.getHash());
        inputBO.set_type(String.valueOf(detailInfoVo.getType()));
        inputBO.set_target_address(detailInfoVo.getTargetAddress());
        inputBO.set_transfer_address(detailInfoVo.getTransferAddress());
        inputBO.set_digital_collection_id(detailInfoVo.getDigitalCollectionId());
        try {
            TransactionResponse res = detailStorageService.addDetail(inputBO);

            if (res.getValues() == null) {
                log.error(res.getReceiptMessages());
                return false;
            }
            log.info("添加到区块链流水表信息："+res.getValues()+" detailInfoVo : "+detailInfoVo);
            JSONArray jsonArray = JSONUtil.parseArray(res.getValues());
            return (boolean) jsonArray.get(0);
        }catch (Exception e){
            log.error("添加到区块链流水表错误！"+e.getMessage());
            return false;
        }

    }
}
