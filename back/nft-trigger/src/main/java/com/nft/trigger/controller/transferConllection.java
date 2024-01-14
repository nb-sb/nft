package com.nft.trigger.controller;

import com.nft.app.collection.CollectionCommandService;
import com.nft.app.collection.dto.TransferCmd;
import com.nft.common.Result;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.support.Token2User;
import com.nft.domain.user.model.vo.UserVo;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Validated
public class transferConllection {
    private final HttpServletRequest httpServletRequest;
    private final CollectionCommandService collectionCommandService;
    private final Token2User token2User;
    //用户转增藏品
    @PostMapping("transferCollection")
    @ResponseBody
    @AuthPermisson
    public Result transfer(@Valid @RequestBody TransferCmd cmd) {
        UserVo userOne = token2User.getUserOne(httpServletRequest);
        if (userOne == null) return Result.userNotFinded();
        cmd.setUserAddress(userOne.getAddress());
        cmd.setUserPrivatekey(userOne.getPrivatekey());
        return collectionCommandService.transferCollection(cmd);
    }
}
