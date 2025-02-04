package com.wsystems.residentstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ResidentstoreApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {

		SpringApplication.run(ResidentstoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Senha: " + passwordEncoder.encode("123456"));

		boolean result = passwordEncoder.matches("123456", "$2a$10$MRw5I/BUjRxCqjn59DN2ouKu/NPLk/5X8u5D2NpiL45PMB1vjHcNK");
		System.out.println(result);
	}
}
