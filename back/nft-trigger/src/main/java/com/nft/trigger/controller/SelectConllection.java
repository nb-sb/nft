package com.nft.trigger.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.domain.nft.model.vo.ConllectionInfoVo;
import com.nft.domain.nft.service.INftSelectService;

import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SelectConllection {

    private final INftSelectService iNftSelectService;

    //查询单个藏品
    @GetMapping("selectConllectionById")
    @ResponseBody
    public ConllectionInfoVo SelectById(@RequestParam Integer id) {
        return iNftSelectService.selectConllectionById(id);
    }

    @GetMapping("selectConllectionPage")
    @ResponseBody
    //分页查询在售的藏品
    public Object Select_Conllection_ByPage(@RequestParam Integer current,Integer size) {
        //1.都可以查询到出售列表中的内容
        Page<ConllectionInfoVo> page = new Page<>(current,size); //查询当前页码，查询条数
       return iNftSelectService.selectConllectionByPage(page);
    }

    //按照分类查询藏品
    @GetMapping("selectConllectionKindByPage")
    @ResponseBody
    public Object selectConllectionKindByPage(@RequestParam Integer current,Integer size,Integer mid) {
        Page<ConllectionInfoVo> page = new Page<>(current,size); //查询当前页码，查询条数
        return iNftSelectService.selectConllectionKindByPage(page,mid);
    }
    //
}
