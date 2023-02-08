package it.univaq.disim.mwt.mydemy.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


@Data
@Component
@ConfigurationProperties(prefix = "mydemy")
public class MyDemyProperties {
	
	private String dateFormat;

}
