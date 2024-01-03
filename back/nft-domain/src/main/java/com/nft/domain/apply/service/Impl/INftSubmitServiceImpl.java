package com.nft.domain.apply.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.common.Constants;
import com.nft.common.Redis.RedisUtil;
import com.nft.common.Result;
import com.nft.domain.apply.model.vo.SubCacheVo;
import com.nft.domain.apply.repository.ISubmitCacheRespository;
import com.nft.domain.apply.service.INftSubmitService;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.res.AuditRes;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.nft.repository.*;
import com.nft.domain.support.Token2User;
import com.nft.domain.support.ipfs.IpfsService;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.domain.user.repository.IUserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
@Log4j2
@Service
@AllArgsConstructor
public class INftSubmitServiceImpl implements INftSubmitService {
    private final INftSellRespository iNftSellRespository;
    private final ISubmitCacheRespository iSubmitCacheRespository;
    private final Token2User token2User;
    private final INftMetasRespository iNftMetasRespository;

    @Override
    public Result addSellCheck(HttpServletRequest httpServletRequest, SellReq sellReq) {
        UserVo userVo =token2User.getUserOne(httpServletRequest);
        if (userVo == null) return Result.userNotFinded();


        SubCacheVo subCacheVo = iSubmitCacheRespository.selectOneByHash(sellReq.getHash());
        if (subCacheVo != null) {
            log.info("hash 已经存在 - 无法提交重复的作品！"+subCacheVo);
            return new NftRes("0", "hash 已经存在 - 无法提交重复的作品！");
        }
        //查询分类id是否存在
        if (!iNftMetasRespository.isExist(sellReq.getMid())) {
            log.info("分类不存在："+sellReq.getMid());
            return new NftRes("0", "分类不存在："+sellReq.getMid());
        }
        boolean res = iSubmitCacheRespository.addSellCheck(sellReq, userVo);

        if (res) return new NftRes("1", "已经添加到审核中~");
        return new NftRes("0", "添加审核失败，请联系网站管理员查看");
    }

    @Override
    public AuditRes changeSellStatus(ReviewReq req) {
        //判断修改前后变量结果是否一致，如果修改前后都是同一状态则不修改
        SubCacheVo subCacheVo = iSubmitCacheRespository.selectSubSellById(req.getId());
        if (!Constants.SellState.DOING.getCode().equals(subCacheVo.getStatus())) {
            return new AuditRes(Constants.SellState.NOTDOING.getCode(), Constants.SellState.NOTDOING.getInfo());
        }
        if (subCacheVo.getStatus().equals(req.getStatus())) {
            log.warn("修改前后状态一致！");
            return new AuditRes(Constants.SellState.ERROR.getCode(), Constants.SellState.ERROR.getInfo());
        }
        if (Constants.SellState.REFUSE.getCode().equals(req.getStatus())) {
            //如果是不同意的话这里可以直接修改审核结果然后返回到controller中
            iSubmitCacheRespository.upDateSubStatus(req);
            return new AuditRes(Constants.SellState.REFUSE.getCode(), Constants.SellState.REFUSE.getInfo());
        }
        if (Constants.SellState.PASS.getCode().equals(req.getStatus())) {
            return new AuditRes(Constants.SellState.PASS.getCode(), Constants.SellState.PASS.getInfo());
        }
        return new AuditRes(Constants.SellState.REFUSE.getCode(), Constants.SellState.REFUSE.getInfo());
    }


    @Override
    public boolean insertSellInfo(ReviewReq req, String hash) {
        boolean b = iSubmitCacheRespository.upDateSubStatus(req); //修改提交表
        boolean b1 = iNftSellRespository.insertSellInfo(req.getId(), hash); //增加出售表
        return b && b1;
    }
}
