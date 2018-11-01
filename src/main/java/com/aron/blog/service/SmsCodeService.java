package com.aron.blog.service;

import com.aron.blog.domain.entity.ResultBean;

public interface SmsCodeService {
    ResultBean sendTemplateSMS(String mobile);
}
