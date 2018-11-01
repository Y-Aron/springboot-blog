package com.aron.blog.domain.constant;


public enum HttpMethodEnum {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private final String value;

    HttpMethodEnum(String value) {
        this.value = value;
    }

    /**
     * 判断是否属于该枚举
     * @param val
     * @return
     */
    public static boolean isDefine(String val){
        for (HttpMethodEnum method: HttpMethodEnum.values()){
            if (method.value.equals(val)) {
                return true;
            }
        }
        return false;
    }

    public String getValue() {
        return value;
    }
}