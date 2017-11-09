$(function () {
    $(".validImg").click(function () {
        $(".validImg").attr("src", ctx + "/getPictureVerifyImage?time=" + new Date());
    });
// 获取验证码
    $("#clickMes").click(function () {
// 获取手机号
        var phone = $("#phone").val();
// 获取图片验证码内容
        var imageCode = $("#code").val();
        if ("" == phone) {
            layer.tips("请输入手机号!", "#phone");
            return;
        }
        if ("" == imageCode) {
            layer.tips("请输入图片验证码!", "#verification");
            return;
        }
        var _this = $(this);
// 执行 ajax 发送请求 参数:phone picVerifyCode
        $.ajax({
            type: "post",
            url: ctx + "/sendPhoneVerify/sendPhoneVerifyCode",
            data: {
                phone: phone,
                imageCode: imageCode
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    time(_this);
                } else {
                    layer.tips(data.msg, "#clickMes");
                }
            }
        })
    })


var wait = 6;

function time(o) {
    if (wait == 0) {
        o.removeAttr("disabled");
        o.val('获取验证码');
        o.css("color", '#ffffff');
        o.css("background", "#fcb22f");
        wait = 6;
    } else {
        o.attr("disabled", true);
        o.css("color", '#fff');
        o.css("background", '#ddd');
        o.val("重新发送(" + wait + "s)");
        wait--;
        setTimeout(function () {
            time(o)
        }, 1000)
    }
}

// 注册点击事件添加
$("#register").click(function () {
    var phone = $("#phone").val();
    var picVerifyCode = $("#code").val();
    var verification = $("#verification").val();
    var password = $("#password").val();
    if ("" == phone) {
        layer.tips("请输入手机号!", "#phone");
        return;
    }
    if ("" == picVerifyCode) {
        layer.tips("请输入图片验证码!", "#verification");
        return;
    }
    if ("" == verification) {
        layer.tips("请输入手机验证码!", "#verification");

        return;
    }
    if ("" == password) {
        layer.tips("请输入密码!", "#password");
        return;
    }
    $.ajax({
        type: "post",
        url: ctx + "/user/registerUser",
        data: {
            phone: phone,
            picVerifyCode: picVerifyCode,
            phoneVerifyCode: verification,
            password: password
        },
        dataType: "json",
        success: function (data) {
            if (data.code == 200) {
                window.location.href = ctx + "/user/toLoginPage";
            } else {
                layer.tips(data.msg, "#register");
            }
        }
    })
})

})
