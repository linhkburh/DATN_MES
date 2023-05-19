package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "V_QUOTATION_ITEM_FINISH")
public class QuotationItemFinish implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 40)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;
	@JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private Customer customer;
	@JoinColumn(name = "QUOTATION_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private Quotation quotationId;
	@Column(name = "MA_BAN_VE", nullable = true, length = 40)
	private String maBanVe;
	@Column(name = "TEN_BAN_VE", nullable = true, length = 40)
	private String tenBanVe;
	@Column(name = "ma_quan_ly", nullable = true, length = 40)
	private String maQuanLy;
	@Column(name = "SO_LUONG")
	private Long soLuong;
	@Column(name = "SO_LUONG_DA_XUAT")
	private Long soLuongDaXuat;
	
	public String getMaQuanLy() {
		return maQuanLy;
	}

	public void setMaQuanLy(String maQuanLy) {
		this.maQuanLy = maQuanLy;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Quotation getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(Quotation quotationId) {
		this.quotationId = quotationId;
	}

	public String getMaBanVe() {
		return maBanVe;
	}

	public void setMaBanVe(String maBanVe) {
		this.maBanVe = maBanVe;
	}

	public String getTenBanVe() {
		return tenBanVe;
	}

	public void setTenBanVe(String tenBanVe) {
		this.tenBanVe = tenBanVe;
	}

	public Long getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(Long soLuong) {
		this.soLuong = soLuong;
	}

	public Long getSoLuongDaXuat() {
		return soLuongDaXuat;
	}

	public void setSoLuongDaXuat(Long soLuongDaXuat) {
		this.soLuongDaXuat = soLuongDaXuat;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
