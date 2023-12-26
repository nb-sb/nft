package com.nft.trigger.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.common.Result;
import com.nft.domain.nft.model.res.GetNftRes;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.service.INftSelectService;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@Validated
public class SelectConllection {

    private final INftSelectService iNftSelectService;

    //查询单个藏品
    @GetMapping("selectConllectionById")
    @ResponseBody
    public Result SelectById(@NotNull(message = "id 不能为空")
                                 @Min(value = 1)
                                 @RequestParam Integer id) {
        GetNftRes result = iNftSelectService.selectConllectionById(id);
        return result;
    }

    @GetMapping("selectConllectionPage")
    @ResponseBody
    //分页查询在售的藏品
    public Result Select_Conllection_ByPage( @NotNull(message = "current 不能为空")
                                                 @Min(value = 1)
                                                 @RequestParam Integer current,
                                             @Min(value = 1)
                                             @NotNull(message = "size 不能为空") Integer size) {
        //1.都可以查询到出售列表中的内容
        Page<ConllectionInfoVo> page = new Page<>(current,size); //查询当前页码，查询条数
       return iNftSelectService.selectConllectionByPage(page);
    }

    //按照分类查询藏品
    @GetMapping("selectConllectionKindByPage")
    @ResponseBody
    public Result selectConllectionKindByPage(@NotNull(message = "current 不能为空")
                                                  @Min(value = 1)
                                                  @RequestParam Integer current,
                                                  @Min(value = 1)
                                                  @NotNull(message = "size 不能为空") Integer size,
                                                  @Min(value = 1)
                                                  @NotNull(message = "mid 不能为空") Integer mid) {
        Page<ConllectionInfoVo> page = new Page<>(current,size); //查询当前页码，查询条数
        return iNftSelectService.selectConllectionKindByPage(page,mid);
    }
    //
}
