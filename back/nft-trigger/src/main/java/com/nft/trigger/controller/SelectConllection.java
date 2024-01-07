package com.nft.trigger.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.PageRequest;
import com.nft.common.Result;
import com.nft.domain.nft.model.req.InfoKindReq;
import com.nft.domain.nft.model.res.GetNftRes;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.model.vo.OrderInfoVo;
import com.nft.domain.nft.service.INftInfoService;
import com.nft.domain.order.service.INftOrderService;
import com.nft.domain.support.Search;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@Validated
public class SelectConllection {

    private final INftInfoService iNftInfoService;
    private final INftOrderService iNftOrderService;

    //查询单个藏品
    @GetMapping("selectConllectionById")
    @ResponseBody
    public Result SelectById(@NotNull(message = "id 不能为空")
                             @Min(value = 1)
                             @RequestParam Integer id) {
        GetNftRes result = iNftInfoService.selectConllectionById(id);
        return result;
    }
    //查询所有出售的藏品
    @GetMapping("selectConllectionPage")
    @ResponseBody
    //分页查询在售的藏品
    public Result Select_Conllection_ByPage(@Valid PageRequest pageRequest) {
        //1.都可以查询到出售列表中的内容
        //查询当前页码，查询条数
        return iNftInfoService.selectSellConllectionByPage(
                new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize())
        );
    }

    //按照分类查询出售藏品
    @GetMapping("selectConllectionKindByPage")
    @ResponseBody
    public Result selectConllectionKindByPage(@Valid InfoKindReq infoKindReq) {
        //查询当前页码，查询条数
        return iNftInfoService.selectSellConllectionKindByPage(
                new Page<>(infoKindReq.getCurrent(), infoKindReq.getPageSize()),
                infoKindReq.getMin());
    }
    //查询所有订单信息
    @GetMapping("selectOrderByPage")
    @ResponseBody
    public Result selectOrderByPage(@Valid PageRequest pageRequest) {
         //查询当前页码，查询条数
        return iNftOrderService.selectAllOrder(
                new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize())
        );
    }
}
