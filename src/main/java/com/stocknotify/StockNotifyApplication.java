package com.stocknotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockNotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockNotifyApplication.class, args);
    }
}
