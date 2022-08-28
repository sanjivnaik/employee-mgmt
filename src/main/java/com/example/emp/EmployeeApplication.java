package com.example.emp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.emp.springboot.entity.Employee;
import com.example.emp.springboot.entity.Role;
import com.example.emp.springboot.entity.Role.ERole;
import com.example.emp.springboot.repository.EmployeeRepository;
import com.example.emp.springboot.repository.RoleRepository;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2 //Url to access "http://localhost:8081/swagger-ui/index.htm"
public class EmployeeApplication implements CommandLineRunner {
	
	protected final Log logger = LogFactory.getLog(getClass());

	public static void main(String[] args) {
		SpringApplication.run(EmployeeApplication.class, args);
	}

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {
		Role role1 = Role.builder().name(ERole.ROLE_ADMIN).build();
		Role role2 = Role.builder().name(ERole.ROLE_MODERATOR).build();
		Role role3 = Role.builder().name(ERole.ROLE_USER).build();
		
		roleRepository.save(role1);
		roleRepository.save(role2);
		roleRepository.save(role3);		

		Employee employee1 = Employee.builder().firstName("Admin").lastName("Adm").email("sanjiv@gmail.com").username("admin").password(encoder.encode("test123"))
				.roles(Arrays.asList(role1))
				.build();
		Employee employee2 = Employee.builder().firstName("Moderate").lastName("Mod").email("maninee@gmail.com").username("moderate").password(encoder.encode("test123"))
				.roles(Arrays.asList(role2))
				.build();
		Employee employee3 = Employee.builder().firstName("User").lastName("Usr").email("aarushi@gmail.com").username("user").password(encoder.encode("test123"))
				.roles(Arrays.asList(role3))
				.build();

		employeeRepository.save(employee1);
		employeeRepository.save(employee2);
		employeeRepository.save(employee3);
		
		logger.info("Swagger UI can be accessed by <context_path>/swagger-ui/index.htm");
	}

	@Bean
	public Docket api() {
	    return new Docket(DocumentationType.SWAGGER_2)
	      .apiInfo(apiInfo())
	      .securityContexts(Arrays.asList(securityContext()))
	      .securitySchemes(Arrays.asList(apiKey()))
	      .select()
	      .apis(RequestHandlerSelectors.any())
	      .paths(PathSelectors.any())
	      .build();
	}
	
	private ApiKey apiKey() { 
	    return new ApiKey("JWT", "Authorization", "header"); 
	}
	
	private SecurityContext securityContext() { 
	    return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
	} 

	private List<SecurityReference> defaultAuth() { 
	    AuthorizationScope[] authorizationScopes = {new AuthorizationScope("global", "accessEverything")}; 
	    return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
	}
	
	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "Employee Management API",
	      "Some custom description of API.",
	      "1.0",
	      "Terms of service",
	      new Contact("Sanjiv Naik", "www.innovasolutions.com", "sanjiv.naik@innovasolutions.com"),
	      "License of API",
	      "API license URL",
	      Collections.emptyList());
	}

}
