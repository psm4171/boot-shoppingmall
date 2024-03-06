package com.psm.shoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = {"com.psm.shoppingmall.repository"})
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SpringBootShoppingmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShoppingmallApplication.class, args);
    }

}
