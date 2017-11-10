$(function () {
    $(document).keydown(function(event) {
        if (event.keyCode == 13) {
            $("#login").click();
        }
    });
        //登录按钮点击
    $("#login").click(function () {
        var phone = $("#phone").val();
        var password = $("#password").val();
        if (isEmpty(phone)){
            layer.tips("请输入手机号！","#phone");
            return;
        }
        if(isEmpty(password)){
            layer.tips("请输入密码！","#password");
            return;
        }
        //进行ajax发送数据进行登录
        $.ajax({
            type:"post",
            url:ctx+"/user/userLogin",
            data:{
                phone:phone,
                password:password
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    window.location.href=ctx+"/index";
                }else {
                    layer.tips(data.msg,"#login");
                }
            }
        })


    })






})


