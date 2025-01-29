package com.usyd.edugenie;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author caorong
 */
@SpringBootApplication
@RestController
public class EdugenieApplication {
    public static void main(String[] args) {
        SpringApplication.run(EdugenieApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s! Welcome to EduGenie! %d", name, (int) (Math.random() * 100));
    }
}