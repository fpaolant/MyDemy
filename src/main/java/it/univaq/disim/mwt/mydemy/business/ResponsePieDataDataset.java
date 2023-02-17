package it.univaq.disim.mwt.mydemy.business;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class ResponsePieDataDataset {
	private ArrayList<Integer> data = new ArrayList<>();
	private ArrayList<String> backgroundColor = new ArrayList<>();

	public void addData(int number) {
		this.data.add(number);
	}
	public void addColor(String color) {
		this.backgroundColor.add(color);
	}
}

