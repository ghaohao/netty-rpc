package com.rpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @Author gu
 * @Version V1.0
 * @Date 17/12/29 下午3:59
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    /**
     * 服务接口
     * @return
     */
    Class<?> value();

    /**
     * 服务版本号
     * @return
     */
    String version() default "";
}
