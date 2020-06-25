package com.javaboy.febs.server.test.service.fallback;

import com.javaboy.febs.server.test.service.IHelloService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
/* HelloServiceFallback实现FallbackFactory,泛型指定为上面定义的IHelloService*/
public class HelloServiceFallback implements FallbackFactory<IHelloService> {

    /* 因为IHelloService目前只包含一个抽象方法，所以它是一个函数式接口，上面的代码可用Lambda表达式简化*/
    @Override
    public IHelloService create(Throwable throwable) {
        return name -> {
            log.error("调用febs-server-system服务出错", throwable);
            return "调用出错";
        };
    }
}