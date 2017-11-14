package cn.hexg.xm.service;

import cn.hexg.xm.po.SysPicture;

import java.util.List;

public interface ISysPictureService {
    public List<SysPicture> querySysPicturesByItemId(Integer itemId);
}
