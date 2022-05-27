package com.example.emp;

import java.util.Arrays;

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

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class EmployeeApplication implements CommandLineRunner {

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
		

		Employee employee1 = Employee.builder().firstName("Sanjiv").lastName("Naik").email("sanjiv@gmail.com").username("sanjiv").password(encoder.encode("test123"))
				.roles(Arrays.asList(role1))
				.build();
		Employee employee2 = Employee.builder().firstName("Maninee").lastName("Patel").email("maninee@gmail.com").username("maninee").password(encoder.encode("test123"))
				.roles(Arrays.asList(role2))
				.build();
		Employee employee3 = Employee.builder().firstName("Aarushi").lastName("Naik").email("aarushi@gmail.com").username("aarushi").password(encoder.encode("test123"))
				.roles(Arrays.asList(role3))
				.build();

		employeeRepository.save(employee1);
		employeeRepository.save(employee2);
		employeeRepository.save(employee3);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.example.demo.controller")).build();//TODO - check the package
	}

}
