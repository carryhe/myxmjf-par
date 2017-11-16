package cn.hexg.xm.web.controller;

import cn.hexg.xm.exceptions.ParamsExcetion;
import cn.hexg.xm.model.ResultInfo;
import cn.hexg.xm.po.*;
import cn.hexg.xm.query.BasItemQuery;
import cn.hexg.xm.service.*;
import cn.hexg.xm.utils.AssertUtil;
import cn.hexg.xm.utils.PageList;
import com.fasterxml.jackson.databind.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("basItem")
public class BasItemController extends BaseController {

    @Resource
    private IBasItemService basItemService;
    @Resource
    private IBasUserSecurityService basUserSecurityService;
    @Resource
    private IBusItemLoanService busItemLoanService;
    @Resource
    private IBusAccountService busAccountService;
    @Resource
    private ISysPictureService sysPictureService;
    @Resource
    private IBusItemInvestService busItemInvestService;



    @RequestMapping("basItemListPage")
    public String basItemList(){
        return "item/invest_list";
    }

    /**
     * 创建进行查询我要投资列表的接口
     * 返回pagelist
     */
    @RequestMapping("queryBasItemsByParams")
    @ResponseBody
    public PageList queryBasItemsByParams(BasItemQuery basItemQuery){
        return basItemService.queryBasItemsByParams(basItemQuery);
    }

    @RequestMapping("updateBasItemStatusToOpen")
    @ResponseBody
    public ResultInfo updateBasItemStatusToOpen(Integer itemId){
        basItemService.updateBasItemStatusToOpen(itemId);
        return success("项目开放成功!");
    }

    /**
     * 根据项目id
     * 转到项目的详情页
     * @param itemId
     * @return
     */
    @RequestMapping("toBasItemDetailPage")
    public String toBasItemDetailPage(Integer itemId, Model model, HttpSession session){
        if (itemId==null||itemId<1){
           throw new ParamsExcetion("传入参数有误！");
        }
        BasItem basItem = basItemService.queryById(itemId);
        Integer itemUserId = basItem.getItemUserId();
        //查询关于用户的信息
        BasUserSecurity basUserSecurity = basUserSecurityService.queryUserSecurityByUserId(itemUserId);
        BusItemLoan busItemLoan=busItemLoanService.queryBusItemLoanByItemId(itemId);
        // 获取session中用户信息  userId
        BasUser basUser= (BasUser) session.getAttribute("user");
        if(null!=basUser){
            BusAccount busAccount= busAccountService.queryBusAccountByUserId(basUser.getId());
            model.addAttribute("busAccount",busAccount);
        }

        List<SysPicture> sysPictures=sysPictureService.querySysPicturesByItemId(itemId);
        model.addAttribute("pics",sysPictures);
        model.addAttribute("busItemLoan",busItemLoan);
        model.addAttribute("loanUser",basUserSecurity);
        model.addAttribute("item",basItem);
        return "item/details";
    }

    @RequestMapping("doInvest")
    @ResponseBody
    public ResultInfo doInvest(BigDecimal amount,Integer itemId,String password,HttpSession session){
        //获取用户的id
        BasUser user = (BasUser) session.getAttribute("user");
        AssertUtil.isTrue(null==user||user.getId()<1,"用户未登录");
        busItemInvestService.addBusItemInvest(user.getId(),amount,itemId,password);
        return success("投资项目成功！");
    }

}
