package com.nft.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.domain.nft.repository.INftMetasRespository;
import com.nft.infrastructure.dao.NftMetasMapper;
import com.nft.infrastructure.po.NftMetas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class INftMetasImpl implements INftMetasRespository {


    @Autowired
    NftMetasMapper nftMetasMapper;
    @Override
    public boolean incr(Integer mid, Integer amount) {
        NftMetas nftMetas = new NftMetas();
        NftMetas nftMetas1 = getNftMetas(mid);
        Integer count = 0;
        if (nftMetas != null) {
             count = nftMetas1.getCount();
        }
        nftMetas.setCount(amount+count);
        UpdateWrapper<NftMetas> nftMetasUpdateWrapper = new UpdateWrapper<>();
        nftMetasUpdateWrapper.eq("mid", mid);
        int update = nftMetasMapper.update(nftMetas, nftMetasUpdateWrapper);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isExist(Integer mid) {
        NftMetas nftMetas = getNftMetas(mid);
        if (nftMetas != null) {
            return true;
        }
        return false;
    }

    private NftMetas getNftMetas(Integer mid) {
        QueryWrapper<NftMetas> nftMetasWrapper = new QueryWrapper<>();
        nftMetasWrapper.eq("mid", mid);
        NftMetas nftMetas = nftMetasMapper.selectOne(nftMetasWrapper);
        return nftMetas;
    }
}
