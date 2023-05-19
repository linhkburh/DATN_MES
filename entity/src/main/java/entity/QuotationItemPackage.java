package entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "QuotationItemPackage")
public class QuotationItemPackage {

	List<QuotationItem> quotationItems = new ArrayList<QuotationItem>();

	public List<QuotationItem> getQuotationItems() {
		return quotationItems;
	}

	public void setQuotationItems(List<QuotationItem> quotationItems) {
		this.quotationItems = quotationItems;
	}

}
