package com.elcorazon.adminlte;
//**********************************************************************************************************************
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//**********************************************************************************************************************
// http://localhost:9090/swagger-ui/index.html
//**********************************************************************************************************************
@SpringBootApplication
@EnableSwagger2
public class SystemApplication {
	//******************************************************************************************************************
	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}
	//******************************************************************************************************************
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.elcorazon.adminlte")).build();
	}
	//******************************************************************************************************************
}
//**********************************************************************************************************************
