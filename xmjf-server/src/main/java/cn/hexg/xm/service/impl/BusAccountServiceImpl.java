package cn.hexg.xm.service.impl;

import cn.hexg.xm.base.BaseService;
import cn.hexg.xm.db.dao.BusAccountDao;
import cn.hexg.xm.po.BusAccount;
import cn.hexg.xm.service.IBusAccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class BusAccountServiceImpl extends BaseService<BusAccount> implements IBusAccountService {
    @Resource
    private BusAccountDao busAccountDao;
    @Override
    public BusAccount queryBusAccountByUserId(Integer userId) {
       /* if (userId==null||userId<1){
            throw new ParamsExcetion("参数异常！");
        }*/
        return busAccountDao.queryBusAccountByUserId(userId);

    }
}
