var chart = null;
$(function () {
    loadAccountInfoData();
    
    loadFiveMonthsInvestData();

});



function loadAccountInfoData() {
    $.ajax({
        type:"post",
        url:ctx+"/account/queryAccountInfoByUserId",
        dataType:"json",
        success:function (data) {
            var total=["总金额",data.total];
            var waitCapital=["代收金额",data.waitCapital];
            var waitIncome=["代收利息",data.waitIncome];
            var cash=["可提现金额",data.cash];
            var frozen=["冻结金额",data.frozen];
            var data1=[];
            data1.push(total,waitCapital,waitIncome,cash,frozen);
            loadHighchartsData(data1);
        }
        
    })

}

function  loadHighchartsData(data1) {
    $('#pie_chart').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            spacing : [100, 0 , 40, 0]
        },
        title: {
            floating:true,
            text: '总资产:'+data1[0][1]
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        series: [{
            type: 'pie',
            innerSize: '80%',
            name: '资产详情',
            data: data1
        }]
    }, function(c) {
        // 环形图圆心
        var centerY = c.series[0].center[1],
            titleHeight = parseInt(c.title.styles.fontSize);
        c.setTitle({
            y:centerY + titleHeight/2
        });
        chart = c;
    })
}


function  loadFiveMonthsInvestData() {
    $.ajax({
        type:"post",
        url:ctx+"/busItemInvest/queryItemInvestsFiveMonthByUserId",
        dataType:"json",
        success:function(data){
            loadHighchartsData02(data);
        }
    })
}


function loadHighchartsData02(data) {
    $('#line_chart').highcharts({
        chart: {
            type: 'line'
        },
        title: {
            text: '投资趋势图'
        },
        subtitle: {
            text: '数据来源: SXT'
        },
        xAxis: {
            categories: data.months
        },
        yAxis: {
            title: {
                text: '投资金额(￥)'
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true          // 开启数据标签
                },
                enableMouseTracking: false // 关闭鼠标跟踪，对应的提示框、点击事件会失效
            }
        },
        series: [{
            name: '投资',
            data:data.amounts
        }]
    });

}
