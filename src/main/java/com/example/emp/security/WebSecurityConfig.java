package com.example.emp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.emp.security.jwt.JwtAuthenticationEntryPoint;
import com.example.emp.security.jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v3 (OpenAPI)
            "/v2/api-docs/**", //TODO V2 or V3?
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/api/authenticate"
    };

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public JwtRequestFilter jwtRequestFilter() {
		return new JwtRequestFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable().
				authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().				
				anyRequest().authenticated().and(). //all other requests need to be authenticated
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().
				sessionManagement().
				sessionCreationPolicy(SessionCreationPolicy.STATELESS); // make sure we use stateless session; session won't be used to store user's state.

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}