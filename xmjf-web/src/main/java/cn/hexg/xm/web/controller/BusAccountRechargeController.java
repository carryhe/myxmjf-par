package cn.hexg.xm.web.controller;

import cn.hexg.xm.constant.P2pConstant;
import cn.hexg.xm.dto.PayDto;
import cn.hexg.xm.exceptions.ParamsExcetion;
import cn.hexg.xm.model.ResultInfo;
import cn.hexg.xm.po.BasUser;
import cn.hexg.xm.po.BasUserSecurity;
import cn.hexg.xm.po.BusAccountRecharge;
import cn.hexg.xm.service.IBasUserSecurityService;
import cn.hexg.xm.service.IBusAccountRechargeService;
import cn.hexg.xm.service.IBusAccountService;
import cn.hexg.xm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Controller
@RequestMapping("account")
public class BusAccountRechargeController extends BaseController {

    @Resource
    private IBasUserSecurityService basUserSecurityService;
    @Resource
    private IBusAccountRechargeService busAccountRechargeService;

    @RequestMapping("setting")
    public  String toSettingPage(HttpSession session, Model model){
        BasUser basUser= (BasUser) session.getAttribute("user");
        BasUserSecurity userSecurity=basUserSecurityService.queryUserSecurityByUserId(basUser.getId());
        model.addAttribute("security",userSecurity);
        return "/user/setting";
    }

    @RequestMapping("recharge")
    public String toAccountRecharge(){
        return "user/recharge";
    }

    @RequestMapping("addBusAccountRecharge")
    public String addBusAccountRecharge(BigDecimal amount, String picCode, String password, HttpSession session, Model model){
        //进行添加账户交易记录
        BasUser user = (BasUser) session.getAttribute("user");
        AssertUtil.isTrue(user==null||user.getId()<1,"用户为登录！");
        String sessionPicCode= (String) session.getAttribute(P2pConstant.PICTURE_VERIFY_KEY);
        if(StringUtils.isBlank(sessionPicCode)){
            model.addAttribute("msg","交易验证码已过期!");
            return "user/recharge";
        }
        if(!sessionPicCode.equals(picCode)){
            model.addAttribute("msg","验证码不正确!");
            return "user/recharge";
        }
        try {
            session.removeAttribute(P2pConstant.PICTURE_TRADE_VERIFY_KEY);
            BusAccountRecharge busAccountRecharge=new BusAccountRecharge();
            busAccountRecharge.setRechargeAmount(amount);
            busAccountRecharge.setUserId(user.getId());
            PayDto payDto=busAccountRechargeService.addBusAccountRechargeAndBuildPayInfo(busAccountRecharge,password);
            model.addAttribute("pay",payDto);
        } catch (ParamsExcetion e) {
            e.printStackTrace();
            model.addAttribute("msg",e.getErrorMsg());
            return "user/recharge";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("msg",P2pConstant.OP_FAILED_MSG);
            return "user/recharge";
        }

        return "user/pay";
    }


}
