package com.nft.trigger.controller;


import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.common.Utils.FileUtils;
import com.nft.domain.apply.service.INftSubmitService;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.common.anno.Status;
import com.nft.domain.nft.model.req.AddOrder;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.apply.model.req.ApplyReq;
import com.nft.domain.order.service.INftOrderService;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.vo.UserVo;
import jodd.util.StringUtil;
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
    private final INftOrderService iNftOrderService;
    private final INftSubmitService iNftSubmitService;
    private final Token2User token2User;



    @PostMapping("addsellcheck")
    @ResponseBody
    @AuthPermisson()
    //添加藏品到待审核数据库
    public Result addsellcheck(@Valid @RequestBody ApplyReq applyReq) {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        return iNftSubmitService.addApply(userOne, applyReq);
    }

    //上传图片接口
    @PostMapping("/upfile")
    @ResponseBody
    @AuthPermisson()
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
        return iNftSubmitService.ReviewCollection(req);
    }

    //抢购藏品
    @PostMapping("addorder")
    @ResponseBody
    @AuthPermisson(Constants.permiss.regularUser)
    public Result purchaseConllection(
            @Valid
            @RequestBody AddOrder addOrder) {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        //这里直接获取到用户id 和 购买商品的id即可，传入到方法中在方法中进行执行
        return iNftOrderService.addConllectionOrder(userOne, addOrder.getId());
    }


    //支付藏品订单
    // TODO: 2024/1/7 应该是支付后自动调用这个方法，这里为了测试就跳过支付阶段直接执行支付藏品后的方法
    @GetMapping("payConllectionOrder")
    @ResponseBody
    @AuthPermisson()
    public Result payOrder(
            @NotNull
            @RequestParam String OrderNumber,
            @NotNull
            Integer paytype
    ) {
        //传入订单id，传入支付类型，传入http用于校验用户信息等
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        return iNftOrderService.payOrder(userOne, OrderNumber, paytype);
    }

    //todo 支付回调接收
    //目前订单处理业务流程：用户将商品提交到订单中 （mq定时30分钟后检查订单状态） -> 支付订单 -> 修改订单状态
    //     30分钟后 => 接受mq消息查询订单状态 如果是已支付/已取消/已退款等（只要不是待支付订单《初始状态》）
                //        如果订单是初始状态改为取消订单
    //todo 以上业务需要修改，因为如果订单虽然大于30分钟了，但是用户还是停在支付界面，
    // 订单以及被修改为取消了，但是用户还是付款了，这时候就需要在回调函数中判断用户支付状态，从而进行修改实际订单状态
}
