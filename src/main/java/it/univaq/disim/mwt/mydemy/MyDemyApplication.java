package it.univaq.disim.mwt.mydemy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@ComponentScan(basePackages = "it.univaq.disim.mwt.mydemy", excludeFilters = @Filter(type = FilterType.REGEX, pattern="it.univaq.disim.mwt.mydemy.business.impl.file.*"))
@EnableMongoRepositories(basePackages = "it.univaq.disim.mwt.mydemy.repository.mongo")
public class MyDemyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyDemyApplication.class, args);		
	}

}
