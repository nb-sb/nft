package com.nft.infrastructure.repository;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Utils.BeanCopyUtils;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.SellInfoVo;
import com.nft.domain.nft.repository.ISellInfoRespository;
import com.nft.infrastructure.dao.OrderInfoMapper;
import com.nft.infrastructure.dao.SellInfoMapper;
import com.nft.infrastructure.dao.SubmitCacheMapper;
import com.nft.infrastructure.fisco.model.bo.SellStroageCreateSellInputBO;
import com.nft.infrastructure.fisco.service.SellStroageService;
import com.nft.infrastructure.po.SellInfo;
import com.nft.infrastructure.po.SubmitCache;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
@Log4j2
@AllArgsConstructor
public class ISellInfoRespositoryImpl implements ISellInfoRespository {
    private final SubmitCacheMapper submitCacheMapper;
    private final SellInfoMapper sellInfoMapper;
    private final SellStroageService sellStroageService;
    @Override
    public SellInfoVo selectSellInfoById(Integer id) {
        SellInfo sellInfo = sellInfoMapper.selectById(id);
        return BeanCopyUtils.convertTo(sellInfo, SellInfoVo::new);
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
    public IPage<ConllectionInfoVo> selectSellConllectionByPage(Page page) {
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
    public IPage<ConllectionInfoVo> selectSellConllectionKindByPage(Page page, Integer mid) {
        //使用es查询数据
//        List<ConllectionInfoVo> records = esUtils.searchByPage(page, String.valueOf(mid), ConllectionInfoVo.class);
//        System.err.println(records);
//        if (records.size() == page.getSize()) {
//            //如果查询到内容则直接返回结果
//            Page<ConllectionInfoVo> conllectionInfoVoPage = new Page<>();
//            return conllectionInfoVoPage.setRecords(records);
//        }
        IPage<ConllectionInfoVo> conllectionInfoVoIPage = sellInfoMapper.selectSellConllectionKindByPage(page, Long.valueOf(mid));
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
    public boolean setSellStocks(Integer id, Integer number) {
//        SellInfoVo sellInfo1 = selectSellInfoById(id);
//        if (sellInfo1 == null) {
//            log.error("商品id不存在");
//            return false;
//        }
//        Integer remain = sellInfo1.getRemain();
        SellInfo sellInfo = new SellInfo();
        sellInfo.setRemain(number);
        sellInfo.setId(id);
        int update = sellInfoMapper.updateById(sellInfo);
        return update > 0;
    }
    @Override
    public boolean insertSellInfo(Integer id, String hash) {
        SubmitCache submitCache = submitCacheMapper.selectById(id);
        if (submitCache == null) return false;
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
    public boolean addSellByFISCO(String hash,BigInteger totail)   {

        SellStroageCreateSellInputBO sellStroageCreateSellInputBO = new SellStroageCreateSellInputBO();
        //传入hash 和 发售总数
        sellStroageCreateSellInputBO.set_hash(hash)
                .setAmount(totail);
        System.out.println(sellStroageCreateSellInputBO);
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
}
