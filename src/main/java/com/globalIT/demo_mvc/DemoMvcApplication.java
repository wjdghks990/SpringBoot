package com.globalIT.demo_mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// #####[ 서비스, 컨트롤러, 환경설정 ]#######
@ComponentScan(basePackages = {"testcontroller","com.globalIT.demo_mvc"})

@SpringBootApplication
public class DemoMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMvcApplication.class, args);
	}

}
