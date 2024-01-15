package com.nft.app.process.sort;

import com.nft.app.process.sort.dto.AddMetaCmd;
import com.nft.app.process.sort.dto.UpdataCmd;
import com.nft.common.Result;
import com.nft.domain.nftSort.model.MetaEntityFactory;
import com.nft.domain.nftSort.model.entity.MetaEntity;
import com.nft.domain.nftSort.repository.ISortRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class SortCommandService {
    private final ISortRepository iSortRepository;
    private final MetaEntityFactory metaEntityFactory;
    public Result creat(AddMetaCmd cmd) {
        MetaEntity sortVo = iSortRepository.selectSortByName(cmd.getName(), cmd.getSlug());
        if (sortVo != null) return Result.error("检查’分类名‘或’分类标识符‘是否已经存在");
        MetaEntity meta = metaEntityFactory.newInstance(cmd.getName(), cmd.getSlug());
        meta.initCount();
        boolean creat = iSortRepository.creat(meta);
        if (creat) {
            return Result.success("创建成功");
        }
        return Result.error("error");
    }
    public Result save(UpdataCmd cmd) {
        Integer mid = cmd.getMid();
        MetaEntity meta = iSortRepository.selectSortByMid(mid);
        if (meta == null) {
            return Result.error("分类不存在，无法修改");
        }
        meta.setName(cmd.getName())
                .setSlug(cmd.getSlug());
        boolean b = iSortRepository.updateSort(meta);
        if (b) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
    //删除分类
    public Result delById(UpdataCmd cmd) {
        if (iSortRepository.delSortById(cmd.getMid())) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
