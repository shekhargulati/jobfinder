package com.openshift.jobfinder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ImportResource("classpath:security.xml")
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
    	return NoOpPasswordEncoder.getInstance();
	}
    
	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}

}
