package com.nft.trigger.controller;


import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.common.Utils.FileUtils;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.model.res.AuditRes;
import com.nft.domain.nft.model.res.NftRes;
import com.nft.domain.nft.service.INftSellService;
import com.nft.domain.support.ipfs.IpfsService;
import com.nft.domain.user.service.IUserAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@Log4j2
@AllArgsConstructor
@Validated
public class SellController {

    private final HttpServletRequest httpServletRequest;
    private final INftSellService iNftSellService;
    private final IUserAccountService iUserAccountService;
    private final IpfsService ipfsService;


    @PostMapping("addsellcheck")
    @ResponseBody
    //添加藏品到待审核数据库
    public Object addsellcheck(@Valid @RequestBody SellReq sellReq) {

        NftRes nftRes = iNftSellService.addSellCheck(httpServletRequest, sellReq);
        return nftRes;
    }

    //上传图片接口
    @PostMapping("/upfile")
    @ResponseBody
    public Object upfile(HttpServletRequest request) {
        List<String> result = new ArrayList<>();
        //多个文件上传  就只是简单的多文件上传保存在本地的磁盘
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
            //<input type="file" name="photo"/>
            List<MultipartFile> files = mrequest.getFiles("photo");
            Iterator<MultipartFile> iter = files.iterator();
            while (iter.hasNext()) {
                MultipartFile photo = iter.next();        // 取出每一个上传文件
                try {
                    result.add(FileUtils.saveFile(photo));        // 保存上传信息
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    //审核藏品, 1.修改藏品状态并将藏品 2.加载至区块链上数据中 3.添加ipfs数据
    @PostMapping("auditSell")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public Result reviewCollection(@Valid @RequestBody ReviewReq req) {
        return iNftSellService.ReviewCollection(req);
    }

    //抢购藏品
    @PostMapping("addorder")
    @ResponseBody
    @AuthPermisson(Constants.permiss.regularUser)
    public Result purchaseConllection(
            @NotNull(message = "ConllectionID 不能为空")
            @Min(value = 1,message = "商品id不能小于 0")
            @RequestParam Integer ConllectionID) {
        //这里直接获取到用户id 和 购买商品的id即可，传入到方法中在方法中进行执行
        return iNftSellService.purchaseConllection(httpServletRequest, ConllectionID);
    }

    //支付藏品订单
    public void payOrder() {

    }




    //管理员 - 添加分类
    public void addSort() {
        //1.判断是否为管理员/是否有权限
        //2.判断分类是否存在
        //3.判断分类缩写是否存在

    }
    //管理员 - 查看分类
    public void selectSortByPage() {

    }
    //管理员 - 修改分类名
    public void changeSortName() {

    }
    //管理员 - 删除分类
    public void deleteSort() {

    }

}
