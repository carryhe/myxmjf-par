package cn.hexg.xm.service.impl;

import cn.hexg.xm.base.BaseService;
import cn.hexg.xm.db.dao.IBasUserDao;
import cn.hexg.xm.po.BasUser;
import cn.hexg.xm.service.IBasUserService;
import cn.hexg.xm.utils.AssertUtil;
import cn.hexg.xm.utils.MD5;
import cn.hexg.xm.utils.PhoneRegularUtil;
import cn.hexg.xm.utils.RandomCodesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class BasUserServiceImpl extends BaseService<BasUser> implements IBasUserService {

    @Resource
    private IBasUserDao basUserDao;

    @Override
    public BasUser queryBasUserByPhone(String phone) {
        return basUserDao.queryBasUserByPhone(phone);
    }

    @Override
    public void saveUser(String phone, String password) {
        /**
         * 1.参数合法性校验
         * 2.手机号唯一
         * 3.用户信息注册 数据初始化
         * bas_user 表 存入用户基本信息
         *
         */
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(password),"请输入个人密 码!");
        AssertUtil.isTrue(null!=queryBasUserByPhone(phone),"该手机号已注 册,请更换手机号!");
        AssertUtil.isTrue(!PhoneRegularUtil.isMobileNO(phone),"请输入正确的手机号！");
        //进行添加用户信息向bas_user表中添加
        Integer userId = initBasUser(phone, password);

    }

    /**
     * 进行注册时候初始化，用户表
     * @param phone
     * @param password
     * @return
     */
    private Integer initBasUser(String phone, String password) {
        BasUser basUser = new BasUser();
        basUser.setType(1);
        basUser.setStatus(1);
        //进行设置用户的密码，进行加盐处理
        String salt = RandomCodesUtils.createRandom(false, 4);
        password = MD5.GetMD5Code(password + salt);
        basUser.setPassword(password);

        return 0;
    }
}
