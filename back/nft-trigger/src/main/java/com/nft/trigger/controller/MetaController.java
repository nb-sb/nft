package com.nft.trigger.controller;

import com.nft.common.Constants;
import com.nft.common.Result;
import com.nft.domain.common.Aop.AuthPermisson;
import com.nft.domain.nftSort.model.req.SortReq;
import com.nft.domain.nftSort.model.req.UpdateSortReq;
import com.nft.domain.nftSort.model.res.SortRes;
import com.nft.domain.nftSort.model.vo.SortVo;
import com.nft.domain.nftSort.service.ISortService;
import com.nft.domain.support.Search;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Log4j2
@AllArgsConstructor
@Validated
public class MetaController {
    private final ISortService iSortService;

    //管理员 - 添加分类
    @PostMapping("addSort")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public Result addSort(@Valid @RequestBody SortReq sortReq) {
        //1.判断是否为管理员/是否有权限
        //2.判断分类是否存在
        //3.判断分类缩写是否存在
        boolean b = iSortService.addSort(sortReq);
        if (b) return  SortRes.success( true);
        return SortRes.error("检查’分类名‘或’分类标识符‘是否已经存在", false);
    }

    //查看分类
    @GetMapping("getSort")
    @ResponseBody
    public Result selectSortByPage(@Valid  Search search) {
        List<SortVo> sortVos = iSortService.selectSortByPage(search);
        return SortRes.success( sortVos);
    }

    //管理员 - 修改分类名
    @PostMapping("changeSortName")
    @ResponseBody
    @AuthPermisson(Constants.permiss.admin)
    public Result changeSortName(@Valid @RequestBody UpdateSortReq updateSortReq) {
        boolean b = iSortService.updateCollection(updateSortReq);
        if (b) return SortRes.success( true);
        return SortRes.error(false);
    }
    //管理员 - 删除分类
    @GetMapping("delSort")
    @AuthPermisson(Constants.permiss.admin)
    @ResponseBody
    public Result deleteSort(@NotNull(message = "分类id 不能为空")
                               @Min(value = 1)
                               @RequestParam Integer mid) {
        boolean b = iSortService.delSortById(mid);
        if (b) return SortRes.success( true);
        return SortRes.error(false);
    }
}
