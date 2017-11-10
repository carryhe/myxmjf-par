package cn.hexg.xm.query;

import cn.hexg.xm.base.BaseQuery;

public class BasItemQuery extends BaseQuery {
    private Integer itemCycle;// 项目期限类别  1-（0-30）  2-（30-90） 3-（90 以上）
    private Integer isHistory;//是否为历史项目  1-历史项目  0-可投标项目
    private Integer itemType;//贷款项目类别

    public Integer getItemCycle() {
        return itemCycle;
    }

    public void setItemCycle(Integer itemCycle) {
        this.itemCycle = itemCycle;
    }

    public Integer getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Integer isHistory) {
        this.isHistory = isHistory;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }
}
