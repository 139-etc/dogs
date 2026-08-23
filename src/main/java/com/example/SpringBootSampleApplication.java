package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.repository")
public class SpringBootSampleApplication {

	public static void main(String[] args) {

	    System.out.println("=== MAIN START ===");

	    try {
	        System.out.println("=== BEFORE SPRING RUN ===");

	        SpringApplication.run(SpringBootSampleApplication.class, args);

	        System.out.println("=== AFTER SPRING RUN ===");

	    } catch (Throwable e) {

	        System.out.println("=== SPRING STARTUP ERROR ===");

	        e.printStackTrace(System.out);

	        System.out.println("=== ERROR END ===");

	        throw e;
	    }
	}
}
