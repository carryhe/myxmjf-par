package cn.hexg.xm.web.controller;
/**
 * 进行发送手机验证码的Controller
 */

import cn.hexg.xm.constant.P2pConstant;
import cn.hexg.xm.exceptions.ParamsExcetion;
import cn.hexg.xm.model.ResultInfo;
import cn.hexg.xm.service.ISendPhoneVerifyService;
import cn.hexg.xm.utils.RandomCodesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("sendPhoneVerify")
public class SendPhoneVerifyController extends BaseController {

    @Resource
    private ISendPhoneVerifyService sendPhoneVerifyService;

    @RequestMapping("sendPhoneVerifyCode")
    @ResponseBody
    public ResultInfo sendPhoneVerify(String phone, String imageCode, HttpSession session){

        ResultInfo resultInfo = null;
        //获取Session中的验证码
        String sessionImageCode = (String) session.getAttribute(P2pConstant.PICTURE_VERIFY_KEY);

       /* if (StringUtils.isBlank(sessionImageCode)){
            return failed("图片验证码过期，请刷新！");
        }*/

        if (StringUtils.isBlank(imageCode)){
            return failed("图片验证码不能为空！");
        }
        if (!sessionImageCode.equals(imageCode)){
            return failed("验证码输入错误！");
        }
        // 删除session  pic key
        //session.removeAttribute(P2pConstant.PICTURE_VERIFY_KEY);
        /**
         * 手机短信发送接口
         */
        String code= RandomCodesUtils.createRandom(true,4);

        try {
            sendPhoneVerifyService.sendPhoneCode(phone,code);
            /**
             * 存储验证码
             * 验证码失效时间设置
             */
            session.setAttribute(P2pConstant.PHONE_VERIFY_CODE+phone,code);
            session.setAttribute(P2pConstant.PHONE_VERIFY_CODE_EXPIR_TIME+phone,new Date());//
            resultInfo=success("验证码发送成功!");
        }catch (ParamsExcetion paramsExcetion){
            resultInfo= failed(paramsExcetion.getErrorMsg());
        }catch (Exception e) {
            e.printStackTrace();
            resultInfo= failed("验证码发送失败,请稍后再试!");
        }
        return  resultInfo;
    }

}
