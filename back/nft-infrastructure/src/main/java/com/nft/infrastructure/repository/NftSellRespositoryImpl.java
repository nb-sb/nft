package com.nft.infrastructure.repository;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.SubCacheVo;
import com.nft.domain.nft.repository.INftSellRespository;
import com.nft.domain.user.model.req.LoginReq;
import com.nft.domain.user.model.vo.UserVo;
import com.nft.infrastructure.dao.NftRelationshipsMapper;
import com.nft.infrastructure.dao.SellInfoMapper;
import com.nft.infrastructure.dao.SubmitCacheMapper;
import com.nft.infrastructure.fisco.model.bo.SellStroageCreateSellInputBO;
import com.nft.infrastructure.fisco.service.SellStroageService;
import com.nft.infrastructure.po.SellInfo;
import com.nft.infrastructure.po.SubmitCache;
import com.nft.infrastructure.util.ElasticSearchUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Map;

@Repository
@Log4j2
@AllArgsConstructor
public class NftSellRespositoryImpl implements INftSellRespository {

    private final UserInfoRepositoryImpl userInfoRepository;
    private final SubmitCacheMapper submitCacheMapper;
    private final SellInfoMapper sellInfoMapper;
    private final NftRelationshipsMapper nftRelationshipsMapper;
    private final INftRelationshipImpl nftRelationship;
    private final INftMetasImpl nftMetas;
    private final SellStroageService sellStroageService;
    private final ElasticSearchUtils esUtils;

    @Override
    public NftRes addSellCheck(SellReq sellReq, Map<String, String> userMap) {

        String username = userMap.get("username");
        String pass = userMap.get("password");
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername(username).setPassword(pass);
        UserVo userVo = userInfoRepository.selectOne(loginReq);
        if (userVo == null) {
            return new NftRes("401", "用户token错误请从新登录");
        }
        SubmitCache submitCache = BeanCopyUtils.convertTo(sellReq, SubmitCache::new);
        submitCache.setStatus(0).setAuthorId(String.valueOf(userVo.getId()))
                .setAuthorAddress(userVo.getAddress());
        QueryWrapper<SubmitCache> submitWrapper = new QueryWrapper<>();
        submitWrapper.eq("hash", submitCache.getHash());
        SubmitCache submitCache1 = submitCacheMapper.selectOne(submitWrapper);
        if (submitCache1 != null) {
            log.warn("hash 已经存在 - 无法提交重复的作品！");
            return new NftRes("0", "hash 已经存在 - 无法提交重复的作品！");
        }
        //查询分类id是否存在
        if (!nftMetas.isExist(sellReq.getMid())) {
            log.warn("分类不存在："+sellReq.getMid());
            return new NftRes("0", "该分类不存在");
        }

        int insert = submitCacheMapper.insert(submitCache);
        submitCache1 = submitCacheMapper.selectOne(submitWrapper);
        if (insert > 0) {
            //添加分类表
            nftRelationship.addMetas(submitCache1.getId(), sellReq.getMid());
            //修改分类表中分类记录数
            nftMetas.incr(sellReq.getMid(), 1);
            return new NftRes("1", "successful!");
        }
        return new NftRes("0", "未知添加错误");
    }

    @Override
    public boolean upDateSubStatus(ReviewReq req) {
        UpdateWrapper<SubmitCache> submitWrapper = new UpdateWrapper<>();
        submitWrapper.eq("id", req.getId());
        SubmitCache submitCache = new SubmitCache();
        submitCache.setStatus(req.getStatus());
        int update = submitCacheMapper.update(submitCache,submitWrapper);
        if (update>0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updataSellStatus(UpdataCollectionReq updataCollectionReq) {
        SellInfo sellInfo = new SellInfo().setStatus(updataCollectionReq.getStatus());
        UpdateWrapper<SellInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("id", updataCollectionReq.getId());
        int update = sellInfoMapper.update(sellInfo, updateWrapper);
        return update > 0;
    }

    @Override
    public boolean insertSellInfo(Integer id, String hash) {
        SubmitCache submitCache = submitCacheMapper.selectById(id);
        SellInfo sellInfo = new SellInfo();
        sellInfo.setUniqueId(id)
                .setHash(submitCache.getHash())
                .setAmount(submitCache.getTotal())
                .setRemain(submitCache.getTotal())
                .setAuther(submitCache.getAuthorAddress())
                .setStatus(1)
                .setIpfsHash(hash);
        int insert = sellInfoMapper.insert(sellInfo);
        return insert > 0;
    }

    @Override
    public boolean addSellByFISCO(String hash,Integer id)   {
        //获取提交数据
        SubmitCache submitCache = submitCacheMapper.selectById(id);
        SellStroageCreateSellInputBO sellStroageCreateSellInputBO = new SellStroageCreateSellInputBO();
        //传入hash 和 发售总数
        sellStroageCreateSellInputBO.set_hash(hash)
                .setAmount(BigInteger.valueOf(submitCache.getTotal()));
        try {
//            SellStroageExistSellInputBO sellStroageCatRemainInputBO = new SellStroageExistSellInputBO();
//            sellStroageCatRemainInputBO.set_hash("hash123");
//            CallResponse callResponse = sellStroageService.existSell(sellStroageCatRemainInputBO);
//            System.err.println(callResponse);

            TransactionResponse sell = sellStroageService.createSell(sellStroageCreateSellInputBO);
            System.err.println(sell);

            if (sell.getValues() == null) {
                log.error(sell.getReceiptMessages());
                return false;
            }
            log.info("出售作品添加到区块链的结果为："+sell.getValues());
            JSONArray jsonArray = JSONUtil.parseArray(sell.getValues());
            return (boolean) jsonArray.get(0);
        }catch (Exception e){
            log.error("添加出售订单到区块链错误！"+e.getMessage());
            return false;
        }

    }

    @Override
    public SubCacheVo selectSubSellById(Integer id) {
        SubmitCache submitCache = submitCacheMapper.selectById(id);
        if (submitCache != null) {
            return BeanCopyUtils.convertTo(submitCache, SubCacheVo::new);
        }
        return null;
    }

    public SellInfo selectSellInfoById(Integer id) {
        return sellInfoMapper.selectById(id);
    }

    @Override
    public ConllectionInfoVo selectConllectionById(Integer id) {
        SellInfo sellInfo = sellInfoMapper.selectById(id);
        ConllectionInfoVo conllectionInfoVo;
        if (sellInfo == null) {
            return null;
        }
        SubmitCache submitCache = submitCacheMapper.selectById(sellInfo.getUniqueId());
        if (submitCache != null) {
            conllectionInfoVo = BeanCopyUtils.convertTo(sellInfo, ConllectionInfoVo::new);
            conllectionInfoVo.setPath(submitCache.getPath())
                    .setPresent(submitCache.getPresent())
                    .setName(submitCache.getName())
                    .setPrice(submitCache.getPrice());
            return conllectionInfoVo;
        }
        return null;
    }



    @Override
    public  IPage<ConllectionInfoVo> selectConllectionByPage(Page<ConllectionInfoVo> page) {
        //
        // 漏洞1.比如id为5的商品已经删除了，es没及时更新，查询为第一页1-10的内容，也就会将这个id为5的商品传输出去
        // 漏洞2：如果查询商品为10条数据，但是es中最后一页的商品为3条数据就会导致下面这个if判断无效 大量请求穿过es打到mysql上，
        // 漏洞3: 比如先查了第3页的第一个,缓存个数现在为1,又查询了第1页的第1个,由于这个if相等导致返回的是缓存id=3(第三页的第一个的数值)
        //使用es查询数据
//        List<ConllectionInfoVo> records = esUtils.searchByPage(page, ConllectionInfoVo.class);
//        if (records.size() == page.getSize()) {
//            //如果查询到内容则直接返回结果
//            Page<ConllectionInfoVo> conllectionInfoVoPage = new Page<>();
//            return conllectionInfoVoPage.setRecords(records);
//        }
        //查不到内容则查询数据库的方法并加入到es中
        IPage<ConllectionInfoVo> conllectionInfoVoIPage = sellInfoMapper.selectConllectionByPage(page);
//        esUtils.insertList(conllectionInfoVoIPage.getRecords()); //同步更新es
        return conllectionInfoVoIPage;
    }

    @Override
    public IPage<ConllectionInfoVo> selectConllectionKindByPage(Page<ConllectionInfoVo> page, Integer mid) {
        //使用es查询数据
//        List<ConllectionInfoVo> records = esUtils.searchByPage(page, String.valueOf(mid), ConllectionInfoVo.class);
//        System.err.println(records);
//        if (records.size() == page.getSize()) {
//            //如果查询到内容则直接返回结果
//            Page<ConllectionInfoVo> conllectionInfoVoPage = new Page<>();
//            return conllectionInfoVoPage.setRecords(records);
//        }
        IPage<ConllectionInfoVo> conllectionInfoVoIPage = sellInfoMapper.selectConllectionKindByPage(page, Long.valueOf(mid));
//        esUtils.insertList(conllectionInfoVoIPage.getRecords()); //同步更新es
        return conllectionInfoVoIPage;
    }

    @Override
    public boolean updataConllectionInfo(UpdataCollectionReq updataCollectionReq) {
        SubmitCache submitCache = new SubmitCache();
        if (updataCollectionReq.getName() != null) {
            submitCache.setName(updataCollectionReq.getName());
        }
        if (updataCollectionReq.getPresent() != null) {
            submitCache.setPresent(updataCollectionReq.getPresent());
        }
        if (updataCollectionReq.getPresent() ==null && updataCollectionReq.getName() == null) {
            log.error("传入的参数都为空值!");
            return false;
        }
        SellInfo sellInfo = sellInfoMapper.selectById(updataCollectionReq.getId());
        submitCache.setId(sellInfo.getUniqueId());
        int update = submitCacheMapper.updateById(submitCache);
        return update >0;
    }

    @Override
    public boolean decreaseSellStocks(Integer id, Integer number) {

        SellInfo sellInfo1 = selectSellInfoById(id);
        if (sellInfo1 == null) {
            log.error("商品id不存在");
            return false;
        }
        Integer remain = sellInfo1.getRemain();
        SellInfo sellInfo = new SellInfo();
        sellInfo.setRemain(remain - 1);
        sellInfo.setId(sellInfo1.getId());
        int update = sellInfoMapper.updateById(sellInfo);
        if (update > 0) {
            return true;
        }
        return false;
    }


}
