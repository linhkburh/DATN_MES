package frwk.form;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ModelForm<T> {
	private String tokenId, tokenIdKey;
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getTokenIdKey() {
		return tokenIdKey;
	}

	public void setTokenIdKey(String tokenIdKey) {
		this.tokenIdKey = tokenIdKey;
	}

	public abstract T getModel();
	private String keyWord;
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	@JsonIgnore
	public Class getModelClass(){
		return getModel().getClass();
	}
}