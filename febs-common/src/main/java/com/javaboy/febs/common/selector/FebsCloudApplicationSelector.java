package com.javaboy.febs.common.selector;

import com.javaboy.febs.common.configure.FebsAuthExceptionConfigure;
import com.javaboy.febs.common.configure.FebsOAuth2FeignConfigure;
import com.javaboy.febs.common.configure.FebsServerProtectConfigure;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class FebsCloudApplicationSelector implements ImportSelector {

    /**
     * 通过selectImports方法，我们一次性导入了
     * FebsAuthExceptionConfigure、FebsOAuth2FeignConfigure和FebsServerProtectConfigure这三个配置类
     * @param annotationMetadata 元数据注释
     * @return String[]
     */
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                /* 开启全局异常处理*/
                FebsAuthExceptionConfigure.class.getName(),
                /* 开启带令牌的Feign请求，避免微服务内部调用出现401异常*/
                FebsOAuth2FeignConfigure.class.getName(),
                /* 开启微服务防护，避免客户端绕过网关直接请求微服务*/
                FebsServerProtectConfigure.class.getName()
        };
    }
}