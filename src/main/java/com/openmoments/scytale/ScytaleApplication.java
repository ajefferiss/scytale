package com.openmoments.scytale;

import com.openmoments.scytale.repositories.AuthTypeRepository;
import com.openmoments.scytale.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScytaleApplication implements CommandLineRunner {

	@Autowired
	private AuthTypeRepository authTypeRepository;
	@Autowired
	private ClientRepository clientRepository;

	public static void main(String[] args) {
		SpringApplication.run(ScytaleApplication.class, args);
	}

	@Override
	public void run(String... args) {
		new LoadAuthDatabase().initAuthenticationDatabase(authTypeRepository);
		new LoadClientDatabase().initClientDatabase(clientRepository, authTypeRepository);
	}

}
