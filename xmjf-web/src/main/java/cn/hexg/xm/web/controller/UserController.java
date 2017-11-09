package cn.hexg.xm.web.controller;

import cn.hexg.xm.constant.P2pConstant;
import cn.hexg.xm.exceptions.ParamsExcetion;
import cn.hexg.xm.model.ResultInfo;
import cn.hexg.xm.service.IBasUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by lp on 2017/11/7.
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private IBasUserService basUserService;


    @RequestMapping("login")
    public String toLogin(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "login";
    }

    @RequestMapping("register")
    public String toRegister(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "register";
    }

    @RequestMapping("registerUser")
    @ResponseBody
    public ResultInfo registerUser(String phone, String picVerifyCode, String phoneVerifyCode, String password
            , HttpSession session) {
        ResultInfo resultInfo = null;
        String sessionImageCode = (String) (session.getAttribute(P2pConstant.PICTURE_VERIFY_KEY));

        if (StringUtils.isBlank(sessionImageCode)) {
            return failed("图片验证码失效！");
        }
        if (!sessionImageCode.equals(picVerifyCode)) {
            return failed("图片验证码输入不正确！");
        }

        //进行验证手机验证码
        //手机验证码发送的时间
        Date sessionDate = (Date) session.getAttribute(P2pConstant.PHONE_VERIFY_CODE_EXPIR_TIME+phone);
        //手机验证码
        String sessionMobileCode = (String) session.getAttribute(P2pConstant.PHONE_VERIFY_CODE+phone);

        if (null != sessionDate && null != sessionMobileCode) {
            //时间单位是秒了/1000
            long time = (new Date().getTime() - sessionDate.getTime()) / 1000;
            if (time > P2pConstant.PHONE_VERIFY_CODE_TIME) {
                return failed("手机验证码已过期!");
            }
        } else {
            return failed("验证码失效，请重新收集获取验证码!");
        }
        if (!sessionMobileCode.equals(phoneVerifyCode)) {
            return failed("手机验证码不正确!");
        }
        try {
            basUserService.saveUser(phone, password);
            // 注册成功 移除 session 信息 图片 session 收集验证码 session 验证码失效时间 session
            session.removeAttribute(P2pConstant.PHONE_VERIFY_CODE + phone);
            session.removeAttribute(P2pConstant.PHONE_VERIFY_CODE_EXPIR_TIME + phone);
            session.removeAttribute(P2pConstant.PICTURE_VERIFY_KEY);

            resultInfo = success("用户注册成功!");
        } catch (ParamsExcetion e) {
            e.printStackTrace();
            resultInfo = failed(e.getErrorMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo = failed("用户注册失败，请稍后再试...");
        }
        return resultInfo;
    }

    @RequestMapping("toLoginPage")
    public String toLoginPage(){
        return "index";
    }

}
