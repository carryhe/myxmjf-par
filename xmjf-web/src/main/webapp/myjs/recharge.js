$(function () {


    // 切换验证码
    $("#validImg").click(function () {
        $(this).attr("src",ctx+"/img/getPictureVerifyImage?time="+new Date());
    });
    $("#rechargeBut").click(function () {
        // 充值金额
        var rechargeAmount=$("#rechargeAmount").val();
        // 验证码
        var pictureCode=$("#pictureCode").val();
        // 校验密码
        var password=$("#password").val();
        if(isEmpty(rechargeAmount)){
            layer.tips("充值金额不能为空!","#rechargeAmount");
            return;
        }
        if(isEmpty(pictureCode)){
            layer.tips("验证码不能为空!","#pictureCode");
            return;
        }
        if(isEmpty(password)){
            layer.tips("交易不能为空!","#password");
            return;
        }
        document.forms["fm"].submit();
    })
})