package cn.hexg.xm.service.impl;

import cn.hexg.xm.base.BaseService;
import cn.hexg.xm.constant.P2pConstant;
import cn.hexg.xm.db.dao.BasUserSecurityDao;
import cn.hexg.xm.exceptions.ParamsExcetion;
import cn.hexg.xm.po.BasUserSecurity;
import cn.hexg.xm.service.IBasItemService;
import cn.hexg.xm.service.IBasUserSecurityService;
import cn.hexg.xm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class BasUserSecurityServiceImpl extends BaseService<BasUserSecurity> implements IBasUserSecurityService {

    @Resource
    private BasUserSecurityDao basUserSecurityDao;

    @Override
    public BasUserSecurity queryUserSecurityByUserId(int userId) {
         return basUserSecurityDao.queryUserSecurityByUserId(userId);
    }

    @Override
    public void queryUserAuthStatus(Integer id) {
        BasUserSecurity basUserSecurity = queryUserSecurityByUserId(id);
        AssertUtil.isTrue(null==basUserSecurity,"用户没有登录！");
        AssertUtil.isTrue(!basUserSecurity.getRealnameStatus().equals(1),"该用户没有进行实名认证！");
    }

    @Override
    public void updateUserSercuityInfo(Integer id, String realName, String idCard, String payPassword) {
        checkUserSercuitParams(realName,idCard);
        //首先将该用户的信息查出来
        BasUserSecurity basUserSecurity = queryUserSecurityByUserId(id);
        AssertUtil.isTrue(null==basUserSecurity,"用户信息错误！");
        basUserSecurity.setRealname(realName);
        basUserSecurity.setIdentifyCard(idCard);
        /*
        * 将交易密码进行加密操作
        * */
        basUserSecurity.setPaymentPassword(payPassword);
        basUserSecurity.setRealnameStatus(1);
        basUserSecurity.setVerifyTime(new Date());
        AssertUtil.isTrue(basUserSecurityDao.update(basUserSecurity)<1, P2pConstant.OP_FAILED_MSG);
    }

    private void checkUserSercuitParams( String realName, String idCard) {
        AssertUtil.isTrue(StringUtils.isBlank(realName),"用户真实姓名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(idCard),"用户身份证信息不能为空！");
    }

}
