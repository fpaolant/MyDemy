package it.univaq.disim.mwt.mydemy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "it.univaq.disim.mwt.mydemy.repository")
public class MyDemyApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyDemyApplication.class, args);		
	}
}
