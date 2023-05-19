package frwk.form;

import entity.frwk.SysDictType;
import frwk.form.ModelForm;

public class SysDictTypeForm extends SearchForm<SysDictType> {

	private SysDictType sysDictType = new SysDictType();
	private String code, name;

	public SysDictType getSysDictType() {
		return sysDictType;
	}

	public void setSysDictType(SysDictType sysDictType) {
		this.sysDictType = sysDictType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public SysDictType getModel() {
		
		return sysDictType;
	}

}
