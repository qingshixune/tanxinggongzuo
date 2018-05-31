package cn.gov.zunyi.video.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 使用Swagger2生成对应的api文档
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket buildDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.groupName("Admin API")
				//.genericModelSubstitutes(DeferredResult.class)
				.genericModelSubstitutes(ResponseEntity.class)
				.useDefaultResponseMessages(false)
				.forCodeGeneration(true)
				.select()
				.apis(RequestHandlerSelectors.basePackage("cn.gov.zunyi.video.web.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		Contact contact = new Contact("zunyi video", "http://localhost/swagger-ui.html", "81222045@qq.com");
		return new ApiInfoBuilder().title("Swagger2 Document Api").description("Spring-boot-Springfox")
				.license("Apache License Version 2.0").contact(contact).version("2.0").build();
	}

}
