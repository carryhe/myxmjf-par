package cn.hexg.xm.web.controller;


import cn.hexg.xm.po.BasUser;
import cn.hexg.xm.query.BusItemInvestQuery;
import cn.hexg.xm.service.IBusItemInvestService;
import cn.hexg.xm.utils.PageList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("busItemInvest")
public class BusItemInvestController  extends  BaseController{
    @Resource
    private IBusItemInvestService busItemInvestService;


   /* @RequestMapping("queryBusItemInvestsByParams")
    @ResponseBody
    public PageList queryBusItemsByParams(BusItemInvestQuery busItemInvestQuery){
        return busItemInvestService.queryBusItemsByParams(busItemInvestQuery);
    }*/


    @RequestMapping("queryItemInvestsFiveMonthByUserId")
    @ResponseBody
    public Map<String,Object> queryItemInvestsFiveMonthByUserId(HttpSession session){
        BasUser basUser= (BasUser) session.getAttribute("user");
       return busItemInvestService.queryItemInvestsFiveMonthByUserId(basUser.getId());
    }
}
