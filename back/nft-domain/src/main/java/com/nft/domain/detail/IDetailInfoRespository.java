package com.nft.domain.detail;

import com.nft.domain.nft.model.vo.DetailInfoVo;
/**
* @author: 戏人看戏
* @Date: 2024/1/7 16:32
* @Description: 交易流水表存贮操作
*/
public interface IDetailInfoRespository {
    boolean addDetailInfo(DetailInfoVo detailInfoVo);

    boolean addDeailInfoByFisco(DetailInfoVo detailInfoVo1);
}
