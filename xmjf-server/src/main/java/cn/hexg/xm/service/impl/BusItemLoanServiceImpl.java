package cn.hexg.xm.service.impl;

import cn.hexg.xm.base.BaseService;
import cn.hexg.xm.db.dao.BusItemLoanDao;
import cn.hexg.xm.exceptions.ParamsExcetion;
import cn.hexg.xm.po.BusItemLoan;
import cn.hexg.xm.service.IBusItemLoanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class BusItemLoanServiceImpl extends BaseService<BusItemLoan> implements IBusItemLoanService {

    @Resource
    private BusItemLoanDao busItemLoanDao;
    @Override
    public BusItemLoan queryBusItemLoanByItemId(Integer itemId) {
       /* if (itemId==null||itemId<1){
            throw new ParamsExcetion("参数异常！");
        }*/
        return busItemLoanDao.queryBusItemLoanByItemId(itemId);
    }
}
