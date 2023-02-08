package it.univaq.disim.mwt.mydemy.business;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestGrid {

	private String draw;
	private int start;
	private int length;
	private SearchType search;
	private List<OrderType> order;
	private List<ColumnType> columns;
	
	public String getSortCol() {
		OrderType orderType = this.order.get(0);
		return columns.get(orderType.getColumn()).getData();
	}
	
	public String getSortDir() {
		OrderType orderType = this.order.get(0);
		return orderType.getDir();
	}
}
