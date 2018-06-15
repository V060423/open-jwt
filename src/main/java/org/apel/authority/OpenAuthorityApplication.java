package org.apel.authority;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.apel.authority")
public class OpenAuthorityApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenAuthorityApplication.class, args);
	}
}
