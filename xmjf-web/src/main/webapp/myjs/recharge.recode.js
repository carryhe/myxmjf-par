$(function () {
    loadRechargeRecords();
});


function  loadRechargeRecords(pageNum) {
    var params={};
    params.pageNum=1;
    if(!isEmpty(pageNum)){
        params.pageNum=pageNum;
    }
    $.ajax({
        type:"post",
        url:ctx+"/account/queryAccountRechargeListByParams",
        data:params,
        dataType:"json",
        success:function (data) {
            var recodeList=data.list;
            var paginator=data.paginator;
            if(recodeList.length>0){
                /**
                 * 拼接 行记录内容
                 */
                initDivsHtml(recodeList);
                /**
                 * 拼接导航页码内容
                 */
                initNavigatePages(paginator);
            }else{
                layer.msg('暂无充值列表!');
            }
        }
    })
}

/**
 * 拼接行记录div
 * @param recodeList
 */
function initDivsHtml(recodeList) {
    /**
     * <script id="rechargeListTemp" type="text/x-handlebars-template">
     {{#each this}}
     <div class="table-content-first">
     {{addtime}}
     </div>
     <div class="table-content-center">
     {{actualAmount}}元
     </div>
     <div class="table-content-first">
     {{status}}
     </div>
     {{/each}}
     </script>
     */
    var divs="";
    if(recodeList.length>0){
        for(var i=0;i<recodeList.length;i++){
            var tempData=recodeList[i];
            divs=divs+"<div class='table-content-first'>"+tempData.addtime+"</div>" +
                "<div class='table-content-center'>"+tempData.actualAmount+"元</div>";
             divs=divs+"<div class='table-content-first'>";
            if(tempData.status==0){
             divs=divs+"未支付";
            }else{
                divs=divs+"已支付";
            }
            divs=divs+"</div>";
        }
        $("#rechargeList").html(divs);
    }
}



function  initNavigatePages(paginator) {
    var navigatepageNums=paginator.navigatepageNums;// 数组
    if(navigatepageNums.length>0){
        /**
         * 拼接导航页内容
         */
        var lis="";
        //var href=
        /**
         * 首页
         * 上一页
         * 下一页
         * 末页
         */
        if(!paginator.isFirstPage){
            lis=lis+"<li ><a href='javascript:getCurrentPageData(1)' title='首页' >首页</a></li>";
        }
        if(paginator.hasPreviousPage){
            lis=lis+"<li ><a href='javascript:getCurrentPageData("+(paginator.pageNum-1)+")' title='上一页' >上一页</a></li>";
        }

        for(var i=0;i<navigatepageNums.length;i++){
            var page=navigatepageNums[i];
            var href="javascript:getCurrentPageData("+page+")";
            if(paginator.pageNum==page){
                lis=lis+"<li class='active'><a  href='"+href+"' title='第"+page+"页' >"+page+"</a></li>";
            }else{
                lis=lis+"<li ><a href='"+href+"' title='第"+page+"页' >"+page+"</a></li>";
            }
        }

        if(paginator.hasNextPage){
            lis=lis+"<li ><a href='javascript:getCurrentPageData("+(paginator.pageNum+1)+")' title='下一页' >下一页</a></li>";
        }

        if(!paginator.isLastPage){
            lis=lis+"<li ><a href='javascript:getCurrentPageData("+(paginator.lastPage)+")' title='末页' >末页</a></li>";
        }

        $("#pages").html(lis);
    }
}

/**
 * 切换页码 重新刷新列表内容
 * @param pageNum
 */
function  getCurrentPageData(pageNum) {
    loadRechargeRecords(pageNum);
}


