package cn.hexg.xm.service.impl;

import cn.hexg.xm.base.BaseService;
import cn.hexg.xm.constant.P2pConstant;
import cn.hexg.xm.db.dao.BasItemDao;
import cn.hexg.xm.dto.BasItemDto;
import cn.hexg.xm.po.BasItem;
import cn.hexg.xm.query.BasItemQuery;
import cn.hexg.xm.service.IBasItemService;
import cn.hexg.xm.utils.AssertUtil;
import cn.hexg.xm.utils.PageList;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class BasItemServiceImpl extends BaseService<BasItem> implements IBasItemService {

    @Resource
    private BasItemDao basItemDao;

    @Override
    public PageList queryBasItemsByParams(BasItemQuery basItemQuery) {
        PageHelper.startPage(basItemQuery.getPageNum(), basItemQuery.getPageSize());
        List<BasItemDto> basItemDtos = basItemDao.queryBasItemsByParams(basItemQuery);
        if(null!=basItemDtos&&basItemDtos.size()>0){
            for(BasItemDto basItemDto:basItemDtos){
                if(basItemDto.getItemStatus().equals(1)){
                    Date relaseTime=basItemDto.getReleaseTime();
                    Date currentTime=new Date();
                    Long syTime=relaseTime.getTime()-currentTime.getTime();
                    if(syTime>0){
                        basItemDto.setSyTime(syTime/1000);
                    }
                }
            }
        }
        PageList pageList = new PageList(basItemDtos);
        return pageList;
    }

    @Override
    public void updateBasItemStatusToOpen(Integer itemId) {
        AssertUtil.isTrue(null==itemId||null==basItemDao.queryById(itemId),"待开放的项目不存在!");
        AssertUtil.isTrue(basItemDao.updateBasItemStatusToOpen(itemId)<1, P2pConstant.OP_FAILED_MSG);
    }
}
