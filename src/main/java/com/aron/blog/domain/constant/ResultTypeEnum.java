package com.aron.blog.domain.constant;

import org.apache.commons.lang3.StringUtils;

public enum ResultTypeEnum {

    SUCCESS(0, "ok~"),
    FAIL(-1, "系统错误，请稍后再试~"),
    URL_FORBIDDEN(403, "Forbidden"),

    SEND_SMS_OK(100, "短信发送成功~"),
    SEND_SMS_ERROR(101, "短信发送失败~"),

    USER_REGISTER_SUCCESS(200, "用户注册成功"),
    USER_REGISTER_FAIL(201, "用户注册失败~"),
    USER_ALREADY_EXISTS(202, "该用户已存在~"),
    USER_NOT_EXIST(203, "该用户不存在~"),
    USER_PASSWORD_FAIL(204, "密码错误~"),
    USER_LOGIN_SUCCESS(205, "用户登陆成功~"),
    USER_LOGIN_FAIL(206, "用户登陆失败~"),
    USER_NOT_NULL(207, "用户名不能为空~"),
    USER_PASSWORD_NOT_NULL(208, "密码不能为空~"),
    USER_URL_METHOD_NOT_ALLOWED(209, "请求登陆URL不支持GET方式"),
    CAPTCHA_FAIL(210, "验证码错误~"),
    CAPTCHA_NOT_NULL(210, "请重新刷新验证码~"),

    JWT_EXPIRED_FAIL(301, "token 超时~请重新登陆！"),
    JWT_INVALID_FAIL(302, "token 无效~");

    private final int code;

    private final String message;

    ResultTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultTypeEnum getResultType(String message){
        if (StringUtils.isBlank(message)) {
            return ResultTypeEnum.SUCCESS;
        }

        for (ResultTypeEnum resultTypeEnum: ResultTypeEnum.values()){
            if (resultTypeEnum.getMessage().equals(message)){
                return resultTypeEnum;
            }
        }
        return ResultTypeEnum.FAIL;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
