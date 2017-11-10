package cn.hexg.xm.dto;


import cn.hexg.xm.po.BasItem;

public class BasItemDto extends BasItem {
    private Integer total;
    private Long syTime;// 贷款项目剩余离发布剩余时间

    public Long getSyTime() {
        return syTime;
    }

    public void setSyTime(Long syTime) {
        this.syTime = syTime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
