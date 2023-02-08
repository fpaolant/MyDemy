package it.univaq.disim.mwt.mydemy.business;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseCategoryItem {
	private long id;
	private long parent_id;
	private String title;
	private long level;
}