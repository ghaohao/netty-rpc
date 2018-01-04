package com.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @Author gu
 * @Version V1.0
 * @Date 18/1/3 下午12:13
 */
public class Startup {
    private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);

    public static void main(String[] args) {
        LOGGER.info("server started");
        new ClassPathXmlApplicationContext("spring-server.xml");
    }
}
