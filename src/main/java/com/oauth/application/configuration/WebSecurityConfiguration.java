package com.oauth.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
	@Bean
	@Order(5)
	SecurityFilterChain loginSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf(csrf -> csrf.disable())
				.headers(headers -> headers
					.frameOptions(frameOptions -> frameOptions.disable()))
				.authorizeHttpRequests((req) -> req
					.requestMatchers("/login/**").permitAll()
					.requestMatchers("/css/**").permitAll()
					.requestMatchers("/images/**").permitAll()
					.requestMatchers("/authorized").permitAll()
					.requestMatchers("/authorized-local").permitAll()
					.requestMatchers("/employee-session").permitAll()
					.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.defaultSuccessUrl("/", true)
						.successHandler(loginSuccessHandler())
						.permitAll());

		return httpSecurity.build();
	}

	private AuthenticationSuccessHandler loginSuccessHandler() {
		return (httpServletRequest, httpServletResponse, authentication) -> {

			httpServletResponse.setStatus(200);
		};
	}

	@Bean
	@Order(2)
	SecurityFilterChain configureSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
				.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public UserDetailsService users() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		UserDetails user1 = User.withUsername("user1")
				.password(encoder.encode("password"))
				.roles("USER")
				.build();

		UserDetails user2 = User.withUsername("user2")
				.password(encoder.encode("password"))
				.roles("USER")
				.build();

		List<UserDetails> userDetailsList = new ArrayList<>();
		userDetailsList.add(user1);
		userDetailsList.add(user2);

		return new InMemoryUserDetailsManager(userDetailsList);
	}
}
