package cic.h2h.form;

import entity.Customer;
import frwk.form.SearchForm;

public class CustomerManageForm extends SearchForm<Customer>{
	private Customer customer = new Customer();
	private String cuCode, cuName, orgName, email;
	private String to;
	private Boolean partner, cus;
	
	public Boolean getPartner() {
		return partner;
	}
	public void setPartner(Boolean partner) {
		this.partner = partner;
	}
	public Boolean getCus() {
		return cus;
	}
	public void setCus(Boolean cus) {
		this.cus = cus;
	}
	@Override
	public Customer getModel() {
		// TODO Auto-generated method stub
		return customer;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getCuCode() {
		return cuCode;
	}
	public void setCuCode(String cuCode) {
		this.cuCode = cuCode;
	}
	public String getCuName() {
		return cuName;
	}
	public void setCuName(String cuName) {
		this.cuName = cuName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
}
