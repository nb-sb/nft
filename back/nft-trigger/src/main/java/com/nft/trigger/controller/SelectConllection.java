package com.nft.trigger.controller;

import com.nft.app.process.collection.CollectionQueryService;
import com.nft.app.process.collection.SellCollectionQueryService;
import com.nft.app.process.order.OrderQueryService;
import com.nft.app.process.order.dto.OrderByStatusQuery;
import com.nft.common.Constants;
import com.nft.common.PageRequest;
import com.nft.common.Result;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.nft.model.req.InfoKindReq;
import com.nft.domain.order.model.req.OrderStateReq;
import com.nft.domain.order.service.INftOrderService;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.vo.UserVo;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@Validated
public class SelectConllection {

    private final CollectionQueryService collectionQueryService;
    private final SellCollectionQueryService sellCollectionQueryService;
    private final OrderQueryService orderQueryService;
    private final INftOrderService iNftOrderService;
    private final Token2User token2User;
    private final HttpServletRequest httpServletRequest;

    //查询单个藏品
    @GetMapping("selectConllectionById")
    @ResponseBody
    public Result SelectById(@NotNull(message = "id 不能为空")
                             @Min(value = 1)
                             @RequestParam Integer id) {
        return sellCollectionQueryService.selectConllectionById(id);
    }
    //查询所有出售的藏品
    @GetMapping("selectConllectionPage")
    @ResponseBody
    //分页查询在售的藏品
    public Result Select_Conllection_ByPage(@Valid PageRequest pageRequest) {
        //1.都可以查询到出售列表中的内容
        //查询当前页码，查询条数
        return sellCollectionQueryService.pageList(pageRequest);
    }

    //按照分类查询出售藏品
    @GetMapping("selectConllectionKindByPage")
    @ResponseBody
    public Result selectConllectionKindPage(@Valid InfoKindReq infoKindReq) {
        //查询当前页码，查询条数
        return sellCollectionQueryService.selectSellConllectionKindPage(infoKindReq);
    }

    //管理员查询所有订单信息
    @GetMapping("selectOrdersByPage")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public Result selectOrderByPage(@Valid PageRequest pageRequest) {
         //查询当前页码，查询条数
        return orderQueryService.pageList(pageRequest);
    }
    //管理员查询提交表信息
    @GetMapping("selectSubmitCache")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public Result selectSubmitCache(@Valid PageRequest pageRequest) {
        return collectionQueryService.applyPageList(pageRequest);
    }
    //查询自己订单信息
    @GetMapping("/user/orders/listall")
    @ResponseBody
    @AuthPermisson()
    public Result selectMyOrderAll() {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        //注意：只返回 藏品名称，商品图片，藏品价格，订单号，创建时间
        //详细信息用下面接口查询，此接口用于展示用户自己拥有订单列表
        return orderQueryService.getOrder(userOne.getId());
    }
    //查询自己订单详细信息
    @GetMapping("/user/orders/{orderId}")
    @ResponseBody
    @AuthPermisson()
    public Result getUserOrderDetails(@PathVariable String orderId) {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        return orderQueryService.getOrder(userOne.getId(),orderId);
    }
    //查询自己待支付/已支付等状态订单 由于用户自己订单不会过多，所以无需使用分页查询（定期清理数据库中取消订单）
    @GetMapping("/user/orders/list")
    @ResponseBody
    @AuthPermisson()
    public Result getUserOrderDetails(@Valid OrderStateReq orderStateReq) {
        if (orderStateReq == null) {
            return selectMyOrderAll();
        }
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        OrderByStatusQuery orderByStatusQuery = new OrderByStatusQuery(userOne.getId(),orderStateReq.getStatus());
        return orderQueryService.getOrderByStatus(orderByStatusQuery);
    }

}
