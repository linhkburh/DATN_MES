package cic.h2h.form;

import entity.MaterialType;
import frwk.form.SearchForm;

public class MaterialTypeForm extends SearchForm<MaterialType> {

	MaterialType materialType = new MaterialType();
	private String codeSearch, nameSearch;

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

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

	@Override
	public MaterialType getModel() {
		
		return materialType;
	}

}
