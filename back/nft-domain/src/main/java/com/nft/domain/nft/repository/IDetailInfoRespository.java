package com.nft.domain.nft.repository;

import com.nft.domain.nft.model.vo.DetailInfoVo;

public interface IDetailInfoRespository {
    boolean addDetailInfo(DetailInfoVo detailInfoVo);

    boolean addDeailInfoByFisco(DetailInfoVo detailInfoVo1);
}
