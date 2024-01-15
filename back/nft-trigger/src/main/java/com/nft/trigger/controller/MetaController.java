package com.nft.trigger.controller;

import com.nft.app.process.sort.SortCommandService;
import com.nft.app.process.sort.SortQueryService;
import com.nft.app.process.sort.dto.AddMetaCmd;
import com.nft.app.process.sort.dto.UpdataCmd;
import com.nft.common.Constants;
import com.nft.common.PageRequest;
import com.nft.common.Result;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.nftSort.service.ISortService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@Log4j2
@AllArgsConstructor
@Validated
public class MetaController {
    private final SortCommandService sortCommandService;
    private final SortQueryService sortQueryService;

    //管理员 - 添加分类
    @PostMapping("addSort")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public Result addSort(@Valid @RequestBody AddMetaCmd cmd) {
        //1.判断是否为管理员/是否有权限
        AddMetaCmd addMetaCmd = new AddMetaCmd();
        addMetaCmd.setName(cmd.getName());
        addMetaCmd.setSlug(cmd.getSlug());
        return sortCommandService.creat(addMetaCmd);
    }

    //查看分类
    @GetMapping("getSort")
    @ResponseBody
    public Result selectSortByPage(@Valid PageRequest pageRequest) {
        return sortQueryService.pageList(pageRequest);
    }

    //管理员 - 修改分类名
    @PostMapping("changeSortName")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public Result changeSortName(@Valid @RequestBody UpdataCmd cmd) {
        Result save = sortCommandService.save(cmd);
        return save;
    }
    //管理员 - 删除分类
    @GetMapping("delSort")
    @AuthPermisson(Constants.permiss.admin)
    @ResponseBody
    public Result deleteSort(@NotNull(message = "分类id 不能为空")
                               @Min(value = 1)
                               @RequestParam Integer mid) {
        return sortCommandService.delById(new UpdataCmd().setMid(mid));
    }
}
