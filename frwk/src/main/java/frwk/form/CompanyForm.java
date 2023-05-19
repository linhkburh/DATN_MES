package frwk.form;

import entity.Company;
import frwk.form.SearchForm;

public class CompanyForm extends SearchForm<Company> {

	private Company company = new Company();

	private String codeSearch, nameSearch;

	public String getCodeSearch() {
		return codeSearch;
	}

	public void setCodeSearch(String codeSearch) {
		this.codeSearch = codeSearch;
	}

	public String getNameSearch() {
		return nameSearch;
	}

	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public Company getModel() {
		
		return company;
	}

}
