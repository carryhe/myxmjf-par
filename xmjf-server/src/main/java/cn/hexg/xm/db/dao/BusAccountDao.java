package cn.hexg.xm.db.dao;

import cn.hexg.xm.base.BaseDao;
import cn.hexg.xm.po.BusAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface BusAccountDao extends BaseDao<BusAccount> {

    public BusAccount queryBusAccountByUserId(Integer userId);

    /**
     * 用户账户金额信息更新
     * @param usableAmount  日志可用余额
     * @param frozenAmount  日志冻结余额
     * @param waitAmount    日志待收金额
     * @param cashAmount    日志可用金额
     * @param repayAmount   日志待还款金额
     * @param userId        登录用户id
     * @return
     */
    public  Integer uppdateBusAccount(@Param("usableAmount")BigDecimal usableAmount,
                                      @Param("frozenAmount")BigDecimal frozenAmount,
                                      @Param("waitAmount")BigDecimal waitAmount,
                                      @Param("cashAmount")BigDecimal cashAmount,
                                      @Param("repayAmount") BigDecimal repayAmount,
                                      @Param("userId") Integer userId);

}