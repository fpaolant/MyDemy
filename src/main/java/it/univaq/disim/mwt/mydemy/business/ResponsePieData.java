package it.univaq.disim.mwt.mydemy.business;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class ResponsePieData {
	private ArrayList<String> labels = new ArrayList<>();
	private ArrayList<ResponsePieDataDataset> datasets = new ArrayList<>();

	public void addLabel(String label) {
		this.labels.add(label);
	}
	public void addDataset(ResponsePieDataDataset dataset) {
		this.datasets.add(dataset);
	}
}
