package cn.hexg.xm.web.proxys;

import cn.hexg.xm.db.dao.IBasUserDao;
import cn.hexg.xm.exceptions.LoginException;
import cn.hexg.xm.po.BasUser;
import cn.hexg.xm.service.IBasUserService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Component
@Aspect
public class LoginProxy  {

    @Resource
    private HttpSession session;
    @Resource
    private IBasUserDao basUserDao;


    @Pointcut(value = "@annotation(cn.hexg.xm.web.annotations.Islogin)")
    public void pointCut(){
    }

    @Before(value = "pointCut()")
    public void before(){
        BasUser user = (BasUser) session.getAttribute("user");
        if(null==user||null== basUserDao.queryById(user.getId())){
            throw  new LoginException("用户未登录!");
        }
    }


}
