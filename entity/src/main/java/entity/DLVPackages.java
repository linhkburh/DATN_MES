package entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dlvPackages")
public class DLVPackages {
	public DLVPackages() {
		this.lstDlvPackage = new ArrayList<DLVPackage>();
	}
	private List<DLVPackage> lstDlvPackage;

	public List<DLVPackage> getLstDlvPackage() {
		return lstDlvPackage;
	}

}
