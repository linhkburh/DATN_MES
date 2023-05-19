package frwk.form;

import java.util.ArrayList;
import java.util.List;

import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;
import frwk.form.ModelForm;

public class SysDictParamForm extends SearchForm<SysDictParam> {

	SysDictParam sysParam = new SysDictParam();

	private List<SysDictType> params = new ArrayList<SysDictType>();

	private String paramsType, codeSearch, valueSearch;

	public SysDictParam getSysParam() {
		return sysParam;
	}

	public void setSysParam(SysDictParam sysParam) {
		this.sysParam = sysParam;
	}

	public List<SysDictType> getParams() {
		return params;
	}

	public void setParams(List<SysDictType> params) {
		this.params = params;
	}

	public String getParamsType() {
		return paramsType;
	}

	public void setParamsType(String paramsType) {
		this.paramsType = paramsType;
	}

	public String getCodeSearch() {
		return codeSearch;
	}

	public void setCodeSearch(String codeSearch) {
		this.codeSearch = codeSearch;
	}

	public String getValueSearch() {
		return valueSearch;
	}

	public void setValueSearch(String valueSearch) {
		this.valueSearch = valueSearch;
	}

	@Override
	public SysDictParam getModel() {
		
		return sysParam;
	}

}
