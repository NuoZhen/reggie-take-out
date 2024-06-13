package com.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("\n  _____                  _        _        _                      _   \n" +
                " |  __ \\                (_)      | |      | |                    | |  \n" +
                " | |__) |___  __ _  __ _ _  ___  | |_ __ _| | _____    ___  _   _| |_ \n" +
                " |  _  // _ \\/ _` |/ _` | |/ _ \\ | __/ _` | |/ / _ \\  / _ \\| | | | __|\n" +
                " | | \\ \\  __/ (_| | (_| | |  __/ | || (_| |   <  __/ | (_) | |_| | |_ \n" +
                " |_|  \\_\\___|\\__, |\\__, |_|\\___|  \\__\\__,_|_|\\_\\___|  \\___/ \\__,_|\\__|\n" +
                "              __/ | __/ |                                             \n" +
                "             |___/ |___/                                              \n" +
                "项目启动成功！");
    }
}
