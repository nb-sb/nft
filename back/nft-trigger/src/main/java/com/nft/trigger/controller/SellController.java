package com.nft.trigger.controller;


import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.common.Utils.FileUtils;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.nft.model.req.AddOrder;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.nft.model.req.SellReq;
import com.nft.domain.nft.service.INftSellService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@AllArgsConstructor
@Validated
public class SellController {

    private final HttpServletRequest httpServletRequest;
    private final INftSellService iNftSellService;



    @PostMapping("addsellcheck")
    @ResponseBody
    @AuthPermisson(Constants.permiss.everyone)
    //添加藏品到待审核数据库
    public Result addsellcheck(@Valid @RequestBody SellReq sellReq) {
        return iNftSellService.addSellCheck(httpServletRequest, sellReq);
    }

    //上传图片接口
    @PostMapping("/upfile")
    @ResponseBody
    @AuthPermisson(Constants.permiss.everyone)
    public Object upfile(HttpServletRequest request) {
        List<String> result = new ArrayList<>();
        //多个文件上传  就只是简单的多文件上传保存在本地的磁盘
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
            //<input type="file" name="photo"/>
            List<MultipartFile> files = mrequest.getFiles("photo");
            // 取出每一个上传文件
            for (MultipartFile photo : files) {
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
            @Valid
            @RequestBody AddOrder addOrder) {
        //这里直接获取到用户id 和 购买商品的id即可，传入到方法中在方法中进行执行
        return iNftSellService.purchaseConllection(httpServletRequest, addOrder.getId());
    }


    //支付藏品订单
    @GetMapping("payConllectionOrder")
    @ResponseBody
    @AuthPermisson()
    public Result payOrder(
            @NotNull
            @RequestParam String OrderNumber,
            Integer paytype
    ) {
        //传入订单id，传入支付类型，传入http用于校验用户信息等
        return iNftSellService.payOrder(httpServletRequest, OrderNumber, paytype);
    }


}
