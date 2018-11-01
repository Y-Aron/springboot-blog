package com.aron.blog.service.impl;

import com.aron.blog.domain.constant.ResultTypeEnum;
import com.aron.blog.domain.entity.ResultBean;
import com.aron.blog.service.SmsCodeService;
import com.aron.blog.utils.RestSmsUtils;
import com.aron.blog.utils.ResultUtils;
import com.aron.blog.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: Y-Aron
 * @create: 2018-10-11 19:38
 **/
@Service(value = "ronglianService")
@ConditionalOnProperty(name = "user.smsService.component", havingValue = "ronglianService")
public class RonglianServiceImpl implements SmsCodeService {

    @Value("${ronglian.templateCode}")
    private String templateCode;

    @Value("${user.smsCode.expires}")
    private String expires;

    @Value("${user.smsCode.smsCodelen}")
    private int smsCodelen;

    private static final String STATUS_CODE_KEY = "statusCode";
    private static final String STATUS_MESSAGE_KEY = "statusMsg";
    private static final String SUCCESS_CODE = "000000";

    private final RestSmsUtils restAPI;
    @Autowired
    public RonglianServiceImpl(RestSmsUtils restAPI) {
        this.restAPI = restAPI;
    }

    @Override
    public ResultBean sendTemplateSMS(String mobile) {

        String validateCode = Utils.generateValidateCode(smsCodelen);
        Map<String, Object> smsResult = restAPI.sendTemplateSMS(mobile, templateCode,
                new String[]{validateCode, expires});

        if (SUCCESS_CODE.equals(smsResult.get(STATUS_CODE_KEY))) {
            return ResultUtils.success(ResultTypeEnum.SEND_SMS_OK, validateCode);
        } else {
            return ResultUtils.fail(ResultTypeEnum.SEND_SMS_ERROR,
                    smsResult.get(STATUS_MESSAGE_KEY));
        }
    }
}
