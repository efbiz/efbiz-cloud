package org.efbiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * User: Joni
 * Email: joni@efbiz.org
 * Date: 2017/3/29
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServeApplication.class, args);
    }
}
