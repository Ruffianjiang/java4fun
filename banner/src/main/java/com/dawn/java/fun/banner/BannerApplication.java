package com.dawn.java.fun.banner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BannerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BannerApplication.class);
        springApplication.setBanner(new MyBanner());
        springApplication.run(args);
    }

}
