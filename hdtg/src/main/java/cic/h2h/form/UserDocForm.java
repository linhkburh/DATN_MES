package cic.h2h.form;

import java.io.File;

import entity.frwk.UserDocumentation;
import frwk.form.SearchForm;

public class UserDocForm extends SearchForm<UserDocumentation>{
	UserDocumentation userDocumentation = new UserDocumentation();
	@Override
	public UserDocumentation getModel() {
		return userDocumentation;
	}
	public UserDocumentation getUserDocumentation() {
		return userDocumentation;
	}
	public void setUserDocumentation(UserDocumentation userDocumentation) {
		this.userDocumentation = userDocumentation;
	}

}
