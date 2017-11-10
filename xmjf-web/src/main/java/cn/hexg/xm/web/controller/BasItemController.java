package cn.hexg.xm.web.controller;

import cn.hexg.xm.model.ResultInfo;
import cn.hexg.xm.query.BasItemQuery;
import cn.hexg.xm.service.IBasItemService;
import cn.hexg.xm.utils.PageList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("basItem")
public class BasItemController extends BaseController {

    @Resource
    private IBasItemService basItemService;

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

}
