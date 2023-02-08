package it.univaq.disim.mwt.mydemy.business;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SearchType {
	
	private String value;
	private boolean regex; 

}
