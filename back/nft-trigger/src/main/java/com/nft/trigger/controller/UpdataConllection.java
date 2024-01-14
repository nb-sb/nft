package com.nft.trigger.controller;

import com.nft.app.collection.CollectionCommandService;
import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.nft.model.req.UpdataCollectionReq;
import com.nft.domain.order.service.INftOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UpdataConllection {
    private final INftOrderService iNftOrderService;
    private final CollectionCommandService collectionCommandService;


    /**
     * @Des 修改藏品信息
     * @Date 2023/12/22 15:16
     * @Param 传入藏品id 必传项
     * @Param 1.如果传入了藏品名称或介绍可以进行对应的修改造作,
     *        2.如果传入的是藏品状态则修改藏品出售状态
     *        注意: 1和2不能同时调用,例如修改出售状态的时候无法修改藏品信息,若都传入参数则优先修改藏品信息
     * @Return
     */
    @PostMapping("updataConllection")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public Result updataConllection(@Valid @RequestBody UpdataCollectionReq updataCollectionReq) {
        //1.验证调用者权限
        //2.进行更新藏品信息
        // TODO: 2024/1/14 这个方法暂时待优化，等有具体修改藏品信息等具体需求的时候进行相应修改
        return collectionCommandService.updataConllectionInfo(updataCollectionReq);
    }
}
