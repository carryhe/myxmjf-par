<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>充值</title>
    <link rel="stylesheet" href="/css/reset.css">
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/recharge.css">
    <link rel="stylesheet" href="/css/user_siderbar.css">
    <script type="text/javascript" src="${ctx}/js/assets/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
    <script type="text/javascript" src="${ctx}/js/recharge.js"></script>
    <script type="text/javascript">
        var ctx="${ctx}";
    </script>

</head>
<body>
<#include "../include/header.ftl">
<div class="container clear">
<#include "../include/user_siderbar.ftl">
    <div class="content fr">
        <div class="recharge-title">
            <div class="recharge-title-left">
                充值
            </div>
            <div class="recharge-title-right">
                <div class="record-but">
                    <a href="/account/rechargeRecord">
                        <button class="but">
                            充值记录
                        </button>
                    </a>
                </div>
            </div>
        </div>

        充值成功!!!
    </div>

</div>
<div class="rebox" style="height: 198%">
    <div class="popup">
        <div class="popup-title">
            <span>登录网上银行充值</span>
            <img src="/img/close-icon.png" id="popupclose">
        </div>
        <div class="popup-content">
            <span>
                请您在新打开的网上银行页面进行充值，充值完成前不要关闭该窗口
            </span>
            <div class="popup-botton">
                <a href="/user/assets?0?3"><button>已完成充值</button></a>
                <a  href="/question?5?4"><button>充值遇到问题</button></a>
            </div>
        </div>
    </div>
</div>
</body>
</html>