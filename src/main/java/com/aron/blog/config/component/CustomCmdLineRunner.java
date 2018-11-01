package com.aron.blog.config.component;

import com.aron.blog.domain.constant.HttpMethodEnum;
import com.aron.blog.domain.entity.Permission;
import com.aron.blog.service.PermissionService;
import com.aron.blog.utils.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

/**
 * @author: Y-Aron
 * @create: 2018-10-12 14:42
 **/
@Component
public class CustomCmdLineRunner implements CommandLineRunner {

    private final RequestMappingHandlerMapping handlerMapping;

    private final PermissionService permissionService;

    @Autowired
    public CustomCmdLineRunner(RequestMappingHandlerMapping handlerMapping,
                               PermissionService p) {
        this.handlerMapping = handlerMapping;
        this.permissionService = p;
    }

    /**
     * 初始化权限表
     * `@RequestMapping`注解默认不写入权限表。如果要使其写入权限表则指定 method属性
     * `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` 默认支持写入权限表
     * @param args
     */
    @Override
    public void run(String... args) {
        // 获取Controller层下URL所有方法
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        // 获取所有的权限列表
        List<Permission> permissionList = permissionService.getPermissionList();

        for (RequestMappingInfo requestInfo: handlerMethods.keySet()) {  // requestInfo 请求信息

            // 获取URL的http类型
            RequestMethodsRequestCondition methodsCondition = requestInfo.getMethodsCondition();

            String httpMethod = String.valueOf(SetUtils
                    .first(methodsCondition.getMethods()));

            if (HttpMethodEnum.isDefine(httpMethod)) {
                // 获取请求URL信息
                String url = String.valueOf(SetUtils
                        .first(requestInfo.getPatternsCondition().getPatterns()));

                Permission permission = new Permission();
                // 设置请求类型
                permission.setHttpMethod(httpMethod);
                // 设置权限名称
                permission.setName(requestInfo.toString());
                // 设置url
                permission.setUrl(url);

                if (!permissionList.contains(permission)) {
                    // 将权限写入权限表
                    permissionService.createPermission(permission);
                }
            }
        }
    }
}
