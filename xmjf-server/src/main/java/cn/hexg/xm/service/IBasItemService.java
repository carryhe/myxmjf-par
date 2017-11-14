package cn.hexg.xm.service;

import cn.hexg.xm.po.BasItem;
import cn.hexg.xm.query.BasItemQuery;
import cn.hexg.xm.utils.PageList;

public interface IBasItemService {

    public BasItem queryById(Integer id);

    public PageList queryBasItemsByParams(BasItemQuery basItemQuery);

    void updateBasItemStatusToOpen(Integer itemId);
}
