package com.aron.blog.utils;

import com.aron.blog.domain.constant.ResultTypeEnum;
import com.aron.blog.domain.entity.ResultBean;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 22:07
 **/
public class ResultUtils {


    public static ResultBean success(ResultTypeEnum codeEnum){
        return success(codeEnum, null);

    }

    public static ResultBean success(ResultTypeEnum codeEnum, Object data){
        return initResult(true, codeEnum, data);
    }

    public static ResultBean fail(ResultTypeEnum codeEnum, Object data){
        return initResult(false, codeEnum, data);
    }

    public static ResultBean fail(String message){
        return fail(ResultTypeEnum.getResultType(message), null);
    }

    public static ResultBean fail(ResultTypeEnum codeEnum){
        return fail(codeEnum, null);
    }
    /**
     * 初始化响应参数
     * @param status
     * @param codeEnum
     * @param data
     * @return
     */
    private static ResultBean initResult(
            boolean status, ResultTypeEnum codeEnum, Object data){
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(status);
        resultBean.setCode(codeEnum.getCode());
        resultBean.setMessage(codeEnum.getMessage());
        resultBean.setData(data);
        return resultBean;
    }
}
