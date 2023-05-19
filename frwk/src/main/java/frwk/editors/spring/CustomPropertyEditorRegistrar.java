package frwk.editors.spring;

import java.util.Date;

import javax.persistence.TemporalType;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

public final class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {

	@Override
	public void registerCustomEditors(PropertyEditorRegistry registry) {
		registry.registerCustomEditor(Date.class, new TimestampEditor());
		registry.registerCustomEditor(String.class, new TimestampEditor());
        registry.registerCustomEditor(TemporalType.class, new TimestampEditor());
		
	}
	

}
