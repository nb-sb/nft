package com.nft.trigger.controller;


import com.nft.app.process.order.OrderCommandService;
import com.nft.app.process.order.dto.PayOrderCmd;
import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.common.Utils.FileUtils;
import com.nft.domain.apply.service.INftSubmitService;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.nft.model.req.AddOrderCmd;
import com.nft.domain.nft.model.req.ReviewReq;
import com.nft.domain.apply.model.req.ApplyReq;
import com.nft.domain.order.service.INftOrderService;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.vo.UserVo;
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
    private final OrderCommandService orderCommandService;



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
            @RequestBody AddOrderCmd cmd) {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        cmd.setUserId(userOne.getId());
        cmd.setUserAddress(userOne.getAddress());
        //这里直接获取到用户id 和 购买商品的id即可，传入到方法中在方法中进行执行
        return orderCommandService.creat(cmd);
    }
    //todo 发起支付订单接口
    @GetMapping("payOrderZFB")
    @ResponseBody
    @AuthPermisson()
    public Result payOrderZFB(

    ) {
        //调用第三方支付接口
        //生成图片base64，返回至前端 / 直接跳转支付链接等方式
        return null;
    }

    //支付藏品订单
    //应该是支付后自动调用这个方法，这里为了测试就跳过支付阶段直接执行支付藏品后的方法
    // 此开源系统只使用网站余额支付，若有其他支付需求可以自行扩展，注释也都写好了2开很容易
    @GetMapping("payConllectionOrder")
    @ResponseBody
    @AuthPermisson()
    public Result payConllectionOrder(
            @NotNull
            @RequestParam String OrderNumber,
            @NotNull
            Integer paytype
    ) {
        //传入订单id，传入支付类型，传入http用于校验用户信息等
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        PayOrderCmd payOrderCmd = new PayOrderCmd(userOne.getId(), userOne.getAddress(), OrderNumber,paytype);
        return orderCommandService.payOrder(payOrderCmd);
    }

    //todo 支付回调接收
    //目前订单处理业务流程：用户将商品提交到订单中 （mq定时30分钟后检查订单状态） -> 支付订单 -> 修改订单状态
    //     30分钟后 => 接受mq消息查询订单状态 如果是已支付/已取消/已退款等（只要不是待支付订单《初始状态》）
                //        如果订单是初始状态改为取消订单
    //todo 以上业务需要修改，因为如果订单虽然大于30分钟了，但是用户还是停在支付界面，
    // 订单以及被修改为取消了，但是用户还是付款了，这时候就需要在回调函数中判断用户支付状态，从而进行修改实际订单状态
//    @PostMapping("/notify")  // 注意这里必须是POST接口
//    public String payNotify(HttpServletRequest request) throws Exception {
//        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
//            System.out.println("=========支付宝异步回调========");
//            Map<String, String> params = new HashMap<>();
//            Map<String, String[]> requestParams = request.getParameterMap();
//            for (String name : requestParams.keySet()) {
//                params.put(name, request.getParameter(name));
//            }
//            // 支付宝验签
//            if (Factory.Payment.Common().verifyNotify(params)) {
//                // 验签通过
//                System.out.println("交易名称: " + params.get("subject"));
//                System.out.println("交易状态: " + params.get("trade_status"));
//                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
//                System.out.println("商户订单号: " + params.get("out_trade_no"));
//                System.out.println("交易金额: " + params.get("total_amount"));
//                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
//                System.out.println("买家付款时间: " + params.get("gmt_payment"));
//                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
//                //保存到订单表中
//                    调用支付方法功能
//                // 更新订单为已支付
////                ShopOrder order = new ShopOrder();
////                order.setId(tradeNo);
////                order.setStatus("1");
////                Date payTime = DateUtil.parse(gmtPayment, "yyyy-MM-dd HH:mm:ss");
////                order.setZhhifuTime(payTime);
////                shopOrderMapper.updateById(order);
//            }
//        }

//        //如果是错误信息则写入日志，发送提醒等
//
//        return "success";
//    }
}
