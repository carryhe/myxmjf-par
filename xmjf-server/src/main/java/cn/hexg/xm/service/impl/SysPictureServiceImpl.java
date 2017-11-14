package cn.hexg.xm.service.impl;

import cn.hexg.xm.base.BaseService;
import cn.hexg.xm.db.dao.SysPictureDao;
import cn.hexg.xm.exceptions.ParamsExcetion;
import cn.hexg.xm.po.SysPicture;
import cn.hexg.xm.service.ISysPictureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class SysPictureServiceImpl extends BaseService<SysPicture> implements ISysPictureService{
    @Resource
    private SysPictureDao sysPictureDao;
    @Override
    public List<SysPicture> querySysPicturesByItemId(Integer itemId) {
       /* if (itemId==null||itemId<1){
            throw new ParamsExcetion("参数异常！");
        }*/
        return sysPictureDao.querySysPicturesByItemId(itemId);

    }
}
