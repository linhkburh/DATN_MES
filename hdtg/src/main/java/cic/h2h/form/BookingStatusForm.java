package cic.h2h.form;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import cic.h2h.form.BookingStatusForm.BookingStatus;
import common.util.FormatNumber;
import entity.BssFactoryUnit;
import entity.Customer;
import entity.QuotationItem;
import entity.frwk.SysDictParam;
import entity.frwk.SysUsers;
import frwk.form.SearchForm;

public class BookingStatusForm extends SearchForm<BookingStatus> {
	private static final Logger logger = Logger.getLogger(BookingStatusForm.class);

	private BookingStatus bookingStatus = new BookingStatus();

	private String code;
	private String company, frDate, toDate, source;
	private String type;

	/**
	 * Id khach hang
	 */
	private String customerCode;

	private boolean fistTime;

	private String manageCodeSearch;

	private String orderCode;

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public String getCode() {
		return code;
	}

	public String getCompany() {
		return company;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public String getFrDate() {
		return frDate;
	}

	public String getManageCodeSearch() {
		return manageCodeSearch;
	}

	@Override
	public BookingStatus getModel() {
		return bookingStatus;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public String getSource() {
		return source;
	}

	public String getToDate() {
		return toDate;
	}

	public String getType() {
		return this.type;
	}

	public boolean isFistTime() {
		return fistTime;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public void setFistTime(boolean fistTime) {
		this.fistTime = fistTime;
	}

	public void setFrDate(String frDate) {
		this.frDate = frDate;
	}

	public void setManageCodeSearch(String manageCodeSearch) {
		this.manageCodeSearch = manageCodeSearch;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static class BookingStatus {
		private Date startTime, endTime;
		private String type;

		private QuotationItem qi = new QuotationItem();

		private Customer customer;

		private Work booking = new Work("booking", this);

		private Work exe = new Work("exe", this);
		private Work os = new Work("os", this);
		private Work po = new Work("po", this);
		private Work qc = new Work("qc", this);
		private Work rp = new Work("rp", this);
		/**
		 * Thoi gian con lai thuc te (thoi gian tinh theo ke hoach tren khoi luong cong viec con lai)
		 * 
		 * @return
		 */
		private BigDecimal outStandingTime;
		private BigDecimal mesure;

		public Work getBooking() {
			return booking;
		}

		public Customer getCustomer() {
			return customer;
		}

		public Date getEndTime() {
			return endTime;
		}

		public Work getExe() {
			return exe;
		}

		public BigDecimal getExeLate() {
			if (exe.exe.exeTime == null || exe.plan.exeTime == null)
				return null;
			return exe.exe.exeTime.subtract(exe.plan.exeTime);
		}

		public BigDecimal getExeLatePercent() {
			if (getExeLate() == null || exe.getTarget().getExeTime().compareTo(new BigDecimal(0)) == 0)
				return null;
			return getExeLate().multiply(new BigDecimal(100)).divide(exe.getTarget().getExeTime(), 3,
					RoundingMode.HALF_UP);
		}

		public BigDecimal getMesure() {
			return mesure;
		}

		public Work getOs() {
			return os;
		}

		public BigDecimal getOutStandingAmount() {
			BigDecimal outStandingAmount = booking.getAmount();
			if (exe.getAmount() != null)
				outStandingAmount = outStandingAmount.subtract(exe.getAmount());
			// Ng, broken
			if (po.getNg() != null)
				outStandingAmount = outStandingAmount.add(po.getNg());
			if (po.getBroken() != null)
				outStandingAmount = outStandingAmount.add(po.getBroken());
			if (qc.getNg() != null)
				outStandingAmount = outStandingAmount.add(qc.getNg());
			if (qc.getBroken() != null)
				outStandingAmount = outStandingAmount.add(qc.getBroken());
			// So luong sua thanh cong
			if (rp.getAmount() != null)
				outStandingAmount = outStandingAmount.subtract(rp.getAmount());
			return outStandingAmount;

		}

		public BigDecimal getOutStandingTime() {
			return outStandingTime;
		}

		/**
		 * Thoi gian con lai theo ke hoach, thoi gian theo ke hoach - thoi gian da chay
		 * 
		 * @return
		 */
		public BigDecimal getPlanOutStandingTime() {
			if (booking.getPlan() == null)
				return null;
			BigDecimal planOutStandingTime = booking.getPlan().getExeTime();
			if (exe.getExe().getExeTime() != null)
				planOutStandingTime.subtract(exe.getExe().getExeTime());
			return planOutStandingTime;
		}

		public Work getPo() {
			return po;
		}

		public BigDecimal getProLate() {
			if (exe.target.getProTime() == null || exe.getProTime() == null)
				return null;
			return exe.getProTime().subtract(exe.getProTime());
		}

		public BigDecimal getProPercent() {
			if (getProLate() == null || getProLate().compareTo(new BigDecimal(0)) == 0)
				return null;
			return getProLate().multiply(new BigDecimal(100)).divide(exe.target.getProTime(), 3, RoundingMode.HALF_UP);
		}

		public Work getQc() {
			return qc;
		}

		public QuotationItem getQi() {
			return qi;
		}

		public Work getRp() {
			return rp;
		}

		public BigDecimal getSetupLate() {
			// Chua hoan thanh, chua tinh duoc dai luong nay
			if (exe.amount == null || booking.amount == null || exe.amount.compareTo(booking.amount) != 0)
				return null;
			if (exe.getSetupTime() == null || exe.target.getSetupTime() == null)
				return null;
			return exe.getSetupTime().subtract(exe.target.getSetupTime());
		}

		public BigDecimal getSetupLatePercent() {
			if (getSetupLate() == null || exe.target.getSetupTime().compareTo(new BigDecimal(0)) == 0)
				return null;
			return getSetupLate().multiply(new BigDecimal(100)).divide(exe.target.getSetupTime(), 3,
					RoundingMode.HALF_UP);
		}

		public Date getStartTime() {
			return startTime;
		}

		public String getType() {
			return type;
		}

		public boolean isFinish() {
			return booking.amount == exe.amount;
		}

		public void read(ResultSet rs) throws Exception {
			// Khach hang
			readCus(rs);
			// Qi
			readQi(rs);
			// booking
			readBooking(rs);
			// exe
			readExe(rs);
			exe.read(rs);
			os.read(rs);
			// So luong sua hang cong gop vao san xuat
			exe.addAmount(os);
			po.read(rs);
			qc.read(rs);
			rp.read(rs);
			if (rs.getBigDecimal("thoi_gian_con_lai_thuc_te") != null)
				this.outStandingTime = rs.getBigDecimal("thoi_gian_con_lai_thuc_te").divide(mesure, 2,
						RoundingMode.HALF_UP);
			this.startTime = rs.getDate("start_time");
			this.endTime = rs.getDate("end_time");

		}

		public void readBooking(ResultSet rs) throws Exception {
			booking.setItems(rs.getLong("so_luong_ban_ve"));
			booking.setAmount(rs.getBigDecimal("booking_amount"));
			if (rs.getBigDecimal("tg_setup_bao_gia") != null)
				booking.setSetupTime(rs.getBigDecimal("tg_setup_bao_gia").divide(mesure, 2, RoundingMode.HALF_UP));
			if (rs.getBigDecimal("tg_gia_cong_bao_gia") != null)
				booking.setExeTime(rs.getBigDecimal("tg_gia_cong_bao_gia").divide(mesure, 2, RoundingMode.HALF_UP));
			if (rs.getBigDecimal("so_luong_da_xuat") != null)
				booking.setExpAmount(rs.getLong("so_luong_da_xuat"));
			else
				booking.setExpAmount(0l);
			booking.setOutStandingAmount(booking.getAmount().longValue() - booking.getExpAmount());
			if (rs.getDate("deliver_date") != null)
				booking.setDeliverDate(new Date(rs.getDate("deliver_date").getTime()));
			booking.readDetailTime(rs);

		}

		public void readCus(ResultSet rs) throws SQLException {
			String custId = rs.getString("id_khach_hang");
			String custCode = rs.getString("ma_kh");
			String custName = rs.getString("ten_kh");
			this.customer = new Customer(custId, custCode, custName);

		}

		public void readExe(ResultSet rs) throws Exception {
			BookingStatus.Work exe = this.exe.exe;
			if (rs.getBigDecimal("tg_lap_trinh_thuc_te") != null)
				exe.setProTime(rs.getBigDecimal("tg_lap_trinh_thuc_te").divide(mesure, 2, RoundingMode.HALF_UP));
			if (rs.getBigDecimal("tg_setup_thuc_te") != null)
				exe.setSetupTime(rs.getBigDecimal("tg_setup_thuc_te").divide(mesure, 2, RoundingMode.HALF_UP));
//			if (rs.getBigDecimal("tg_gia_cong_du_kien") != null)
//				exe.setPlanTime(rs.getBigDecimal("tg_gia_cong_du_kien").divide(mesure, 2, RoundingMode.HALF_UP));

			// target
			BookingStatus.Work target = this.exe.target;
			if (rs.getBigDecimal("tg_lap_trinh_target") != null)
				target.setProTime(rs.getBigDecimal("tg_lap_trinh_target").divide(mesure, 2, RoundingMode.HALF_UP));
			// kg co dai luong nay
			// target.setSetupTime(rs.getBigDecimal("tg_setup_target"));
//			if (rs.getBigDecimal("tg_gia_cong_target") != null)
//				target.setExeTime(rs.getBigDecimal("tg_gia_cong_target").divide(mesure, 2, RoundingMode.HALF_UP));

			this.exe.readDetailTime(rs);

		}

		public void readQi(ResultSet rs) throws SQLException {
			this.qi.setCode(rs.getString("code"));
			this.qi.setManageCode(rs.getString("manage_code"));
			this.qi.setQcode(rs.getString("qcode"));
			this.qi.setWorkOderNumber(rs.getString("work_order_number"));
			if ("qi".equals(this.type)) {
				this.qi.setId(rs.getString("id"));
				this.qi.setSiggle(new SysDictParam(null, null, null, rs.getString("hDescription")));
				this.qi.setAcId(new SysUsers(null, rs.getString("acName"), rs.getString("acUserName")));
				this.qi.setFactoryUnit(new BssFactoryUnit(null, null, rs.getString("buCode"), rs.getString("buName")));
			}

		}

		public void setBooking(Work booking) {
			this.booking = booking;
		}

		public void setCustomer(Customer customer) {
			this.customer = customer;
		}

		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}

		public void setExe(Work exe) {
			this.exe = exe;
		}

		public void setOs(Work os) {
			this.os = os;
		}

		public void setOutStandingTime(BigDecimal outStandingTime) {
			this.outStandingTime = outStandingTime;

		}

		public void setPo(Work po) {
			this.po = po;
		}

		public void setQc(Work qc) {
			this.qc = qc;
		}

		public void setQi(QuotationItem qi) {
			this.qi = qi;
		}

		public void setRp(Work rp) {
			this.rp = rp;
		}

		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}

		public void setType(String type) {
			this.type = type;
			if ("qi".equals(type))
				mesure = new BigDecimal(60);
			else
				// mesure = new BigDecimal(1);
				mesure = new BigDecimal(60);
		}

		public static class Work {
			private String alias;
			// Số lượng chi tiết hoan thanh
			private BigDecimal amount;
			// Số lượng chi tiết huy
			private BigDecimal broken;
			private Work exe;
			// Thời gian gia công
			private BigDecimal exeTime;
			/**
			 * Thoi gian gia cong du kien
			 */
			private BigDecimal estEstTime;

			// Thời gian gia công
			private BigDecimal exeTimeEDM;

			// Thời gian gia công
			private BigDecimal exeTimeMCDB;

			// Thời gian gia công
			private BigDecimal exeTimeP;
			// Thời gian gia công
			private BigDecimal exeTimeT;
			// Thời gian gia công
			private BigDecimal exeTimeWC;
			// So luong da xuat
			private Long expAmount;
			private Long outStandingAmount;
			/**
			 * So luong ban ve
			 */
			private long items;
			// Số lượng chi tiết ng
			private BigDecimal ng;
			private Work plan;

			private BigDecimal planTime;

			// Thời gian lập trình
			private BigDecimal proTime;

			// Thời gian setup
			private BigDecimal setupTime;

			private Work target;
			// Tong
			private BigDecimal totalAmount;
			private BookingStatus bookingStatus;
			private Date deliverDate;

			public Work() {
			}

			public Work(String alias, BookingStatus bookingStatus) {
				this.alias = alias;
				this.bookingStatus = bookingStatus;
				if ("booking".equals(alias) || "exe".equals(alias)) {
					this.target = new Work("target", bookingStatus);
					this.plan = new Work("plan", bookingStatus);
					// Tranh de quy
					if ("exe".equals(alias)) {
						this.exe = new Work();
						this.exe.alias = "exe";
						this.exe.bookingStatus = bookingStatus;
					}

				}
			}

			/**
			 * Add them so luong ng, huy, thoi gian thuc hien tu gia cong ngoai cho sx
			 * 
			 * @param os
			 */
			public void addAmount(Work os) {
				if (os.ng != null) {
					if (this.ng == null)
						this.ng = os.ng;
					else
						this.ng = this.ng.add(os.ng);
				}
				if (os.broken != null) {
					if (this.broken == null)
						this.broken = os.broken;
					else
						this.broken = this.broken.add(os.broken);
				}
				if (os.exeTime != null) {
					if (this.exeTime == null)
						this.exeTime = os.exeTime;
					else
						this.exeTime = this.exeTime.add(os.exeTime);
				}

			}

			public BigDecimal getAmount() {
				return amount;
			}

			public String getAmountDes() {
				String huy = this.broken != null && this.broken.doubleValue() > 0
						? String.format("<font color='red'>%s</font>", FormatNumber.num2Str(this.broken))
						: this.broken == null ? "0" : FormatNumber.num2Str(this.broken);
				String ng = alias.equals("rp") ? null
						: this.ng != null && this.ng.doubleValue() > 0
								? String.format("<font color='red'>%s</font>", FormatNumber.num2Str(this.ng))
								: this.ng == null ? "0" : FormatNumber.num2Str(this.ng);
				if (alias.equals("rp")) {
					return String.format("<span title='Tổng/Ok/Hủy'>%s</span>",
							String.format("%s/%s/%s",
									new Object[] {
											this.totalAmount == null ? "0" : FormatNumber.num2Str(this.totalAmount),
											this.amount == null ? "0" : FormatNumber.num2Str(this.amount), huy }));
				} else {
					return String.format("<span title='Tổng/Ok/Ng/Hủy'>%s</span>",
							String.format("%s/%s/%s/%s",
									new Object[] {
											this.totalAmount == null ? "0" : FormatNumber.num2Str(this.totalAmount),
											this.amount == null ? "0" : FormatNumber.num2Str(this.amount), ng, huy }));
				}
			}

			public BigDecimal getBroken() {
				return broken;
			}

			public Date getDeliverDate() {
				return deliverDate;
			}

			public BigDecimal getEstEstTime() {
				return estEstTime;
			}

			public Work getExe() {
				return exe;
			}

			public BigDecimal getExeTime() {
				return exeTime;
			}

			public BigDecimal getExeTimeECM() {
				return exeTimeEDM;
			}

			public BigDecimal getExeTimeMCDB() {
				return exeTimeMCDB;
			}

			public BigDecimal getExeTimeP() {
				return exeTimeP;
			}

			public BigDecimal getExeTimeT() {
				return exeTimeT;
			}

			public BigDecimal getExeTimeWC() {
				return exeTimeWC;
			}

			public Long getExpAmount() {
				return expAmount;
			}

			public long getItems() {
				return items;
			}

			public BigDecimal getNg() {
				return ng;
			}

			public Long getOutStandingAmount() {
				return outStandingAmount;
			}

			public Work getPlan() {
				return plan;
			}

			public BigDecimal getPlanTime() {
				return planTime;
			}

			public BigDecimal getProTime() {
				return proTime;
			}

			public BigDecimal getSetupTime() {
				return setupTime;
			}

			public Work getTarget() {
				return target;
			}

			public BigDecimal getTotalAmount() {
				return totalAmount;
			}

			public void push(JSONArray ja) {
				// Thoi gian thuc hien
				ja.put(FormatNumber.num2Str(this.exeTime));
				// So luong chi tiet
				ja.put(getAmountDes());

			}

			public void read(ResultSet rs) throws SQLException {
				// Exe da doc rieng
				if (!"exe".equals(alias)) {
					if (rs.getBigDecimal(alias + "_" + "tg") != null)
						this.exeTime = rs.getBigDecimal(alias + "_" + "tg").divide(this.bookingStatus.mesure, 2,
								RoundingMode.HALF_UP);
				}
				// Thoi gian du kien cho nguoi va qc
				if ("po".equals(alias) || "qc".equals(alias)) {
					if (rs.getBigDecimal(alias + "_" + "tg_dk") != null)
						this.estEstTime = rs.getBigDecimal(alias + "_" + "tg_dk").divide(this.bookingStatus.mesure, 2,
								RoundingMode.HALF_UP);
				}

				if (rs.getBigDecimal(alias + "_" + "tong") != null)
					this.totalAmount = rs.getBigDecimal(alias + "_" + "tong");
				if (rs.getBigDecimal(alias + "_" + "ok") != null)
					this.amount = rs.getBigDecimal(alias + "_" + "ok");
				// Sua hang kg co NG
				if (!alias.equals("rp")) {
					if (rs.getBigDecimal(alias + "_" + "ng") != null)
						this.ng = rs.getBigDecimal(alias + "_" + "ng");
				}

				if (rs.getBigDecimal(alias + "_" + "huy") != null)
					this.broken = rs.getBigDecimal(alias + "_" + "huy");
			}

			public void readDetailTime(ResultSet rs) throws Exception {
				this.target.readTime(this, rs);
				this.plan.readTime(this, rs);
				if (this.exe != null)
					this.exe.readTime(this, rs);
			}

			public void readDetailTime1(ResultSet rs) throws SQLException {
				if (alias.equals("booking")) {
					// target
					target = new Work("target", this.bookingStatus);
					if (rs.getBigDecimal("tg_gia_cong_bao_gia") != null)
						target.exeTime = rs.getBigDecimal("tg_gia_cong_bao_gia").divide(this.bookingStatus.mesure, 2,
								RoundingMode.HALF_UP);
					if (rs.getBigDecimal("bao_gia_tg_gia_cong_target_T") != null)
						target.exeTimeT = rs.getBigDecimal("bao_gia_tg_gia_cong_target_T")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					if (rs.getBigDecimal("bao_gia_tg_gia_cong_target_P") != null)
						target.exeTimeP = rs.getBigDecimal("bao_gia_tg_gia_cong_target_P")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					if (rs.getBigDecimal("bao_gia_tg_gia_cong_target_WC") != null)
						target.exeTimeWC = rs.getBigDecimal("bao_gia_tg_gia_cong_target_WC")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					if (rs.getBigDecimal("bao_gia_tg_gia_cong_target_EDM") != null)
						target.exeTimeEDM = rs.getBigDecimal("bao_gia_tg_gia_cong_target_EDM")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					if (rs.getBigDecimal("bao_gia_tg_gia_cong_target_M_C_DB") != null)
						target.exeTimeMCDB = rs.getBigDecimal("bao_gia_tg_gia_cong_target_M_C_DB")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					if (rs.getBigDecimal("tg_setup_bao_gia") != null)
						target.setupTime = rs.getBigDecimal("tg_setup_bao_gia").divide(this.bookingStatus.mesure, 2,
								RoundingMode.HALF_UP);

					// exe
					plan = new Work("plan", this.bookingStatus);
					if (rs.getBigDecimal("tg_gia_cong_bao_gia_ke_hoach") != null)
						plan.exeTime = rs.getBigDecimal("tg_gia_cong_bao_gia_ke_hoach")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					if (rs.getBigDecimal("bao_gia_tg_gia_cong_target_T_ke_hoach") != null)
						plan.exeTimeT = rs.getBigDecimal("bao_gia_tg_gia_cong_target_T_ke_hoach")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					if (rs.getBigDecimal("bao_gia_tg_gia_cong_target_P_ke_hoach") != null)
						plan.exeTimeP = rs.getBigDecimal("bao_gia_tg_gia_cong_target_P_ke_hoach")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					if (rs.getBigDecimal("bao_gia_tg_gia_cong_target_WC_ke_hoach") != null)
						plan.exeTimeWC = rs.getBigDecimal("bao_gia_tg_gia_cong_target_WC_ke_hoach")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					if (rs.getBigDecimal("bao_gia_tg_gia_cong_target_EDM_ke_hoach") != null)
						plan.exeTimeEDM = rs.getBigDecimal("bao_gia_tg_gia_cong_target_EDM_ke_hoach")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					if (rs.getBigDecimal("bao_gia_tg_gia_cong_target_M_C_DB_ke_hoach") != null)
						plan.exeTimeMCDB = rs.getBigDecimal("bao_gia_tg_gia_cong_target_M_C_DB_ke_hoach")
								.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					return;
				}
				// Thuc te
				exe = new Work("exe", this.bookingStatus);
				if (rs.getBigDecimal("exe_tg") != null)
					exe.exeTime = rs.getBigDecimal("exe_tg").divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
				if (rs.getBigDecimal("exe_tg_T") != null)
					exe.exeTimeT = rs.getBigDecimal("exe_tg_T").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("exe_tg_P") != null)
					exe.exeTimeP = rs.getBigDecimal("exe_tg_P").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("exe_tg_WC") != null)
					exe.exeTimeWC = rs.getBigDecimal("exe_tg_WC").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("exe_tg_EDM") != null)
					exe.exeTimeEDM = rs.getBigDecimal("exe_tg_EDM").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("exe_tg_M_C_DB") != null)
					exe.exeTimeMCDB = rs.getBigDecimal("exe_tg_M_C_DB").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);

				// target
				target = new Work("target", this.bookingStatus);
				if (rs.getBigDecimal("tg_gia_cong_target") != null)
					target.exeTime = rs.getBigDecimal("tg_gia_cong_target").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("tg_gia_cong_target_T") != null)
					target.exeTimeT = rs.getBigDecimal("tg_gia_cong_target_T").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("tg_gia_cong_target_P") != null)
					target.exeTimeP = rs.getBigDecimal("tg_gia_cong_target_P").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("tg_gia_cong_target_WC") != null)
					target.exeTimeWC = rs.getBigDecimal("tg_gia_cong_target_WC").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("tg_gia_cong_target_EDM") != null)
					target.exeTimeEDM = rs.getBigDecimal("tg_gia_cong_target_EDM").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("tg_gia_cong_target_M_C_DB") != null)
					target.exeTimeMCDB = rs.getBigDecimal("tg_gia_cong_target_M_C_DB").divide(this.bookingStatus.mesure,
							2, RoundingMode.HALF_UP);
				// Du kien
				plan = new Work("plan", this.bookingStatus);
				if (rs.getBigDecimal("tg_gia_cong_du_kien") != null)
					plan.exeTime = rs.getBigDecimal("tg_gia_cong_du_kien").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("tg_gia_cong_du_kien_T") != null)
					plan.exeTimeT = rs.getBigDecimal("tg_gia_cong_du_kien_T").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("tg_gia_cong_du_kien_P") != null)
					plan.exeTimeP = rs.getBigDecimal("tg_gia_cong_du_kien_P").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("tg_gia_cong_du_kien_WC") != null)
					plan.exeTimeWC = rs.getBigDecimal("tg_gia_cong_du_kien_WC").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("tg_gia_cong_du_kien_EDM") != null)
					plan.exeTimeEDM = rs.getBigDecimal("tg_gia_cong_du_kien_EDM").divide(this.bookingStatus.mesure, 2,
							RoundingMode.HALF_UP);
				if (rs.getBigDecimal("tg_gia_cong_du_kien_M_C_DB") != null)
					plan.exeTimeMCDB = rs.getBigDecimal("tg_gia_cong_du_kien_M_C_DB").divide(this.bookingStatus.mesure,
							2, RoundingMode.HALF_UP);

			}

			private void readField(String columnPrefix, String fieldName, ResultSet rs) throws Exception {
				String columnName = columnPrefix + "_" + fieldName;
				try {
					BigDecimal tmp = rs.getBigDecimal(columnName);
					if (tmp == null)
						return;
					tmp = tmp.divide(this.bookingStatus.mesure, 2, RoundingMode.HALF_UP);
					Field f = this.getClass().getDeclaredField(fieldName);
					f.set(this, tmp);
				} catch (Exception e) {
					logger.error("Error column name: " + columnName, e);
					throw e;
				}

			}

			private void readTime(Work parent, ResultSet rs) throws Exception {
				String columnPrefix = parent.alias + "_" + this.alias;
				readField(columnPrefix, "exeTime", rs);
				readField(columnPrefix, "exeTimeT", rs);
				readField(columnPrefix, "exeTimeP", rs);
				readField(columnPrefix, "exeTimeEDM", rs);
				readField(columnPrefix, "exeTimeWC", rs);
				readField(columnPrefix, "exeTimeMCDB", rs);
			}

			public void setAmount(BigDecimal amount) {
				this.amount = amount;
			}

			public void setBroken(BigDecimal broken) {
				this.broken = broken;
			}

			public void setDeliverDate(Date deliverDate) {
				this.deliverDate = deliverDate;
			}

			public void setEstEstTime(BigDecimal estEstTime) {
				this.estEstTime = estEstTime;
			}

			public void setExe(Work exe) {
				this.exe = exe;
			}

			public void setExeTime(BigDecimal exeTime) {
				this.exeTime = exeTime;
			}

			public void setExeTimeECM(BigDecimal exeTimeEDM) {
				this.exeTimeEDM = exeTimeEDM;
			}

			public void setExeTimeMCDB(BigDecimal exeTimeMCDB) {
				this.exeTimeMCDB = exeTimeMCDB;
			}

			public void setExeTimeP(BigDecimal exeTimeP) {
				this.exeTimeP = exeTimeP;
			}

			public void setExeTimeT(BigDecimal exeTimeT) {
				this.exeTimeT = exeTimeT;
			}

			public void setExeTimeWC(BigDecimal exeTimeWC) {
				this.exeTimeWC = exeTimeWC;
			}

			public void setExpAmount(Long expAmount) {
				this.expAmount = expAmount;
			}

			public void setItems(long items) {
				this.items = items;
			}

			public void setNg(BigDecimal ng) {
				this.ng = ng;
			}

			public void setOutStandingAmount(Long outStandingAmount) {
				this.outStandingAmount = outStandingAmount;
			}

			public void setPlan(Work plan) {
				this.plan = plan;
			}

			public void setPlanTime(BigDecimal planTime) {
				this.planTime = planTime;
			}

			public void setProTime(BigDecimal proTime) {
				this.proTime = proTime;
			}

			public void setSetupTime(BigDecimal setupTime) {
				this.setupTime = setupTime;
			}

			public void setTarget(Work target) {
				this.target = target;
			}

			public void setTotalAmount(BigDecimal totalAmount) {
				this.totalAmount = totalAmount;
			}

		}
	}

}
