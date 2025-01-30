package de.sommer.stepflowBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class StepflowBackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().directory("src/main/resources").filename(".env").ignoreIfMissing().ignoreIfMalformed().load();
		SpringApplication.run(StepflowBackendApplication.class, args);
	}

}
