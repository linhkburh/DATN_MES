package cic.h2h.form;

import entity.Material;
import frwk.form.SearchForm;

public class MaterialForm extends SearchForm<Material> {

	Material material = new Material();

	private String code, name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	@Override
	public Material getModel() {
		
		return material;
	}

}
