package cic.h2h.form;

import entity.OrderStatus;
import frwk.form.SearchForm;

public class OrderStatusForm extends SearchForm<OrderStatus>{

	OrderStatus orderStatus = new OrderStatus();
	private String cusCode,cusName,orderFrom,orderTo,machiningSts,orderSts;
	
	
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}


	public String getCusCode() {
		return cusCode;
	}


	public void setCusCode(String cusCode) {
		this.cusCode = cusCode;
	}


	public String getCusName() {
		return cusName;
	}


	public void setCusName(String cusName) {
		this.cusName = cusName;
	}


	public String getOrderFrom() {
		return orderFrom;
	}


	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}


	public String getOrderTo() {
		return orderTo;
	}


	public void setOrderTo(String orderTo) {
		this.orderTo = orderTo;
	}


	public String getMachiningSts() {
		return machiningSts;
	}


	public void setMachiningSts(String machiningSts) {
		this.machiningSts = machiningSts;
	}


	public String getOrderSts() {
		return orderSts;
	}


	public void setOrderSts(String orderSts) {
		this.orderSts = orderSts;
	}


	@Override
	public OrderStatus getModel() {
		
		return orderStatus;
	}

}
