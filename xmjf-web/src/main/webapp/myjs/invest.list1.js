$(function () {
    //进行初始化页面，投资列表的展示。
    loadInverseListData();//进行加载投资列表的方法,没有参数进行默认的查询
    //  期限类别切换事件
    $(".tab").click(function () {
        $(this).addClass("list_active");
        $(".tab").not($(this)).removeClass("list_active");//移除非当前节点list_active 属性
        //console.log("期限类别:"+$(this).index());
        var itemCycle=$(this).index();
        var isHistory=0;// 非历史项目
        if(itemCycle==4){
            isHistory=1;
        }
        var itemType=$("#itemType").val();
        loadInverseListData(itemCycle,isHistory,itemType);
    })

})

/**
 * 加载投资列表数据
 *   1.投资期限
 *   2.是否为历史项目
 *   3.贷款项目类型
 *   4.翻页页码
 *   5.每页展示内容总数
 */
function loadInverseListData(itemCycle, isHistory, itemType, pageNum, pageSize) {
    //进行创建ajax请求传入后台的参数
    var params = {};
    //设置默认值
    params.isHistory = 1;
    params.pageNum = 1;
    params.pageSize = 10;
    //进行赋值
    if (!isEmpty(itemCycle)) {
        params.itemCycle = itemCycle;
    }
    if (!isEmpty(isHistory)) {
        params.isHistory = isHistory;
    }
    if (!isEmpty(itemType)) {
        params.itemType = itemType;
    }
    if (!isEmpty(pageNum)) {
        params.pageNum = pageNum;
    }
    if (!isEmpty(pageSize)) {
        params.pageSize = pageSize;
    }

    //使用ajax进行局部刷新数据
    $.ajax({
        type: "post",
        url: ctx + "/basItem/queryBasItemsByParams",
        data: params,
        dataType: "json",
        success: function (data) {
            //获取后台的数据，进行向前台刷新数据
            /**
             * 后台响应的内容data(PageList)
             * 1,Paginator  存放进行分页的数据
             * 2.list  basItemDtos，进行装入前台需要的数据对象
             */
            var paginator = data.paginator;
            var basItemDtos = data.list;
            if (basItemDtos != null && basItemDtos.length > 0) {
                //进行页面上的拼接数据
                initHtml(basItemDtos);
                //进行添加导航页
                initNavigatePages(paginator);


            } else {
                layer.msg('暂无投资项列表!');
            }

        }


    })


}

/**
 * 进行数据列表页面展示
 * @param list
 */
function initHtml(list) {
    if (list != null && list.length > 0) {
        var trs = "";
        for (var i = 0; i < list.length; i++) {
            var tempData = list[i];
            trs = trs + "<tr>";
            // 年化率
            trs = trs + "<td>" +
                "<strong>" + tempData.itemRate + "</strong>";
            trs = trs + "<span>%";
            if (tempData.itemAddRate != null && tempData.itemAddRate != "") {
                trs = trs + "+" + tempData.itemAddRate + "%";
            }
            trs = trs + "</span>";
            trs = trs + "</td>";
            // 项目期限
            trs = trs + "<td>" + tempData.itemCycle + tempData.itemCycleUnit + "</td>";
            // 项目名称 项目类型拼接
            trs = trs + "<td>";
            trs = trs + tempData.itemName;
            if (tempData.itemIsnew == 1) {
                trs = trs + "<strong class='colorful' new>NEW</strong>";
            }
            if (tempData.itemIsnew == 0 && tempData.moveVip == 1) {
                trs = trs + "<strong class='colorful' app>APP</strong>";
            }
            if (tempData.itemIsnew == 0 && tempData.moveVip == 0 && tempData.itemIsrecommend == 1) {
                trs = trs + "<strong class='colorful' hot>HOT</strong>";
            }
            if (tempData.itemIsnew == 0 && tempData.moveVip == 0 && tempData.itemIsrecommend == 0 && !isEmpty(tempData.password)) {
                trs = trs + "<strong class='colorful' lock>LOCK</strong>";
            }
            trs = trs + "</td>";
            //  信用积分
            trs = trs + "<td class='trust_range'>";
            if (tempData.total > 90) {
                trs = trs + "A+";
            }
            if (tempData.total > 85 && tempData.total <= 90) {
                trs = trs + "A";
            }
            if (tempData.total > 75 && tempData.total <= 85) {
                trs = trs + "A-";
            }
            if (tempData.total > 65 && tempData.total <= 75) {
                trs = trs + "B";
            }
            trs = trs + "</td>";
            // 担保公司
            trs = trs + "<td>";
            var url = ctx + "/img/logo.png";
            trs = trs + "<img src=" + url + " >";
            trs = trs + "</td>";
            // 投资进度

            trs = trs + "<td>";
            if (tempData.itemStatus == 1) {
                trs = trs + "<strong class='countdown time' data-time='" + tempData.syTime + "' data-item='" + tempData.id + "'>";
                trs = trs + "<time class='hour'></time>";
                trs = trs + " &nbsp;:<time class='min'></time>";
                trs = trs + " &nbsp;:<time class='sec'></time>";
                trs = trs + "</strong>"
            } else {
                trs = trs + "<div class='itemScale' data-value='" + tempData.itemScale + "'  ></div>";
            }

            trs = trs + "</td>";
            //  操作项
            trs = trs + "<td>";
            if (tempData.itemStatus == 1) {
                trs = trs + "<p><a href=''>" +
                    "<input class='countdownButton' valid type='button' value='即将开放'></a></p>";
            }
            if (tempData.itemStatus == 10 || tempData.itemStatus == 13 || tempData.itemStatus == 18) {
                /**
                 * <p class="left_money">可投金额{{laveAmount}}元</p>
                 <p><a href="/item/details/{{id}}?{{id}}"><input valid type="button" value='立即投资'></a></p>
                 */
                var leaveAmmount = tempData.itemAccount - tempData.itemOngoingAccount;
                trs = trs + "<p class='left_money'>可投金额" + leaveAmmount + "元</p>";
                trs = trs + "<p><a href=''><input valid type='button' value='立即投资'></a></p>";
            }
            if (tempData.itemStatus == 20) {
                trs = trs + "<p><a href=''><input not_valid type='button' value='已抢完'></a></p>"
            }
            if (tempData.itemStatus == 30 || tempData.itemStatus == 31) {
                trs = trs + "<p><a href=''><input not_valid type='button' value='还款中'></a></p>"
            }
            if (tempData.itemStatus == 32) {
                trs = trs + "<p style='position: relative'>" +
                    "<a href='' class='yihuankuan'>已还款</a>" +
                    "<div class='not_valid_pay'></div>" +
                    "</p>";
            }
            if (tempData.itemStatus == 23) {
                trs = trs + "<p><a href=''><input not_valid type='button' value='已满标'></a></p>"
            }
            trs = trs + "</td>";
            //  行单元格拼接结束
            trs = trs + "</tr>";
        }
        $("#pcItemList").html(trs);
    }

}

/**
 * 进行添加导航页
 * @param paginator
 */
function initNavigatePages(paginator) {
    var navigatepageNums = paginator.navigatepageNums;// 数组
    if (navigatepageNums.length > 0) {
        /**
         * 拼接导航页内容
         */
        var lis = "";
        /**
         * 首页
         * 上一页
         * 下一页
         * 末页
         */
        if (!paginator.isFirstPage) {
            lis = lis + "<li ><a href='javascript:getCurrentPageData(1)' title='首页' >首页</a></li>";
        }
        if (paginator.hasPreviousPage) {
            lis = lis + "<li ><a href='javascript:getCurrentPageData(" + (paginator.pageNum - 1) + ")' title='上一页' >上一页</a></li>";
        }
        for (var i = 0; i < navigatepageNums.length; i++) {
            var page = navigatepageNums[i];
            var href = "javascript:getCurrentPageData(" + page + ")";
            if (paginator.pageNum == page) {
                lis = lis + "<li class='active'><a  href='" + href + "' title='第" + page + "页' >" + page + "</a></li>";
            } else {
                lis = lis + "<li ><a href='" + href + "' title='第" + page + "页' >" + page + "</a></li>";
            }
        }

        if (paginator.hasNextPage) {
            lis = lis + "<li ><a href='javascript:getCurrentPageData(" + (paginator.pageNum + 1) + ")' title='下一页' >下一页</a></li>";
        }

        if (!paginator.isLastPage) {
            lis = lis + "<li ><a href='javascript:getCurrentPageData(" + (paginator.lastPage) + ")' title='末页' >末页</a></li>";
        }
        $("#pages").html(lis);
    }
}

/**
 * 切换页码 重新刷新列表内容
 * @param pageNum
 */
function  getCurrentPageData(pageNum) {
    //alert("第"+pageNum+"内容");
    var itemCycle=0;// 期限类别
    var isHistory=0;
    $("#validItem .tab").each(function () {
        if($(this).hasClass("list_active")){
            itemCycle= $(this).index();
        }
    });
    if(itemCycle==4){
        isHistory=1;// 设置为历史项目
    }

    var itemType=$("#itemType").val();


    loadInverseListData(itemCycle,isHistory,itemType,pageNum,10);
}
//项目类别改变 执行该方法
function  initItemData(itemType) {
    var itemCycle=0;// 期限类别
    var isHistory=0;
    $("#validItem .tab").each(function () {
        if($(this).hasClass("list_active")){
            itemCycle= $(this).index();
        }
    });
    if(itemCycle==4){
        isHistory=1;
    }
    loadInverseListData(itemCycle,isHistory,itemType);
}

/**
 * 渲染贷款项目投资进度
 */
function initInvestScale() {
    radialIndicator.defaults.radius=40;
    radialIndicator.defaults.barColor="orange";
    radialIndicator.defaults.barWidth=10;
    radialIndicator.defaults.roundCorner=true;
    radialIndicator.defaults.percentage=true;
    radialIndicator.defaults.fontSize=25;
    $(".itemScale").each(function () {
        var investScale= $(this).attr("data-value");//获取投资进度
        $(this).radialIndicator();
        $(this).data('radialIndicator').value(investScale);
    })

}

/**
 * 即将开标项目倒计时
 */
function initInvestDjs() {
    $(".countdown").each(function () {
        var syTime= $(this).attr("data-time");
        var itemId=$(this).attr("data-item");
        timer(syTime,$(this),itemId);
    })
}



function timer(intDiff,obj,itemId){
    if( obj.timers){
        clearInterval( obj.timers);
    }

    obj.timers=setInterval(function(){
        var day=0,
            hour=0,
            minute=0,
            second=0;//时间默认值
        if(intDiff > 0){
            day = Math.floor(intDiff / (60 * 60 * 24));
            hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
            minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
            second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
        }

        if (minute <= 9) minute = '0' + minute;
        if (second <= 9) second = '0' + second;
        obj.find('.hour').html(hour);
        obj.find('.min').html(minute);
        obj.find('.sec').html(second);
        intDiff--;
        if(intDiff==-1){
            $.ajax({
                url : ctx+'/basItem/updateBasItemStatusToOpen',
                dataType : 'json',
                type : 'post',
                data:{
                    itemId:itemId
                },
                success : function(data) {
                    if(data.code==200){
                        window.location.reload()
                    }
                },
                error : function(textStatus, errorThrown) {

                }
            });
        }
    }, 1000);
}









