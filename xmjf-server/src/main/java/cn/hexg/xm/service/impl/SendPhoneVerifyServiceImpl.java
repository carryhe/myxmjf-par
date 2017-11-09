package cn.hexg.xm.service.impl;

import com.alibaba.fastjson.JSON;
import cn.hexg.xm.constant.P2pConstant;
import cn.hexg.xm.service.IBasUserService;
import cn.hexg.xm.service.ISendPhoneVerifyService;
import cn.hexg.xm.utils.AssertUtil;
import cn.hexg.xm.utils.PhoneRegularUtil;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendPhoneVerifyServiceImpl implements ISendPhoneVerifyService {

    @Resource
    private IBasUserService basUserService;
    @Override
    public void sendPhoneCode(String phone, String code) throws Exception {
        checkSendPhoneVerify(phone,code);
        phoneRegular(phone);
        AssertUtil.isTrue(null!=basUserService.queryBasUserByPhone(phone),"该手机号已注册!");

        //调用发送短信的接口
        TaobaoClient client = new DefaultTaobaoClient(P2pConstant.TAOBAO_SEND_PHONE_URL,P2pConstant.TAOBAO_APP_KEY, P2pConstant.TAOBAO_APP_SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("");
        req.setSmsType(P2pConstant.TAOBAO_SMS_TYPE);
        req.setSmsFreeSignName(P2pConstant.TAOBAO_SMS_FREE_SIGN_NAME);
        Map<String,String> map=new HashMap<String,String>();
        map.put("code",code);
        req.setSmsParamString(JSON.toJSONString(map));
        req.setRecNum(phone );
        req.setSmsTemplateCode(P2pConstant.TAOBAO_SMS_TEMPLATE_CODE);
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        client.execute(req);

    }


    /**
     * 手机号正则验证
     * @param phone
     */
    private void phoneRegular(String phone) {
        AssertUtil.isTrue(!PhoneRegularUtil.isMobileNO(phone),"请输入正确的手机号！");
    }

    /**
     * 进行校验发送手机短信验证码的参数
     * @param phone
     * @param imageCode
     */
    private void checkSendPhoneVerify(String phone, String imageCode) {
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(imageCode),"图片验证码不能为空！");
    }


}
