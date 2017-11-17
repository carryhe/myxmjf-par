package cn.hexg.xm.service.impl;

import cn.hexg.xm.base.BaseService;
import cn.hexg.xm.db.dao.BusAccountDao;
import cn.hexg.xm.db.dao.IBasUserDao;
import cn.hexg.xm.dto.BusAccountDto;
import cn.hexg.xm.po.BusAccount;
import cn.hexg.xm.service.IBusAccountService;
import cn.hexg.xm.utils.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class BusAccountServiceImpl extends BaseService<BusAccount> implements IBusAccountService {
    @Resource
    private BusAccountDao busAccountDao;
    @Resource
    private IBasUserDao basUserDao;
    @Override
    public BusAccount queryBusAccountByUserId(Integer userId) {
       /* if (userId==null||userId<1){
            throw new ParamsExcetion("参数异常！");
        }*/
        return busAccountDao.queryBusAccountByUserId(userId);

    }

    @Override
    public BusAccountDto queryBusAccountInfoByUserId(Integer id) {
        //进行校验参数合法性
        AssertUtil.isTrue(id<1||basUserDao.queryById(id)==null,"用户不存在！");
        return busAccountDao.queryBusAccountInfoByUserId(id);
    }
}
