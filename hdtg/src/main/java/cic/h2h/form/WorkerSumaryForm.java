package cic.h2h.form;

import java.math.BigDecimal;

import entity.frwk.SysUsers;
import frwk.form.SearchForm;
import cic.h2h.form.WorkerSumaryForm.WorkerSumary;

public class WorkerSumaryForm extends SearchForm<WorkerSumary> {
	private WorkerSumary wkSumary = new WorkerSumary();
	private String woCode, naWorker, frDate, toDate, drCode, opCode, maCode, machiningSts;
	private String shift, factoryUnit;
	private String company;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getFactoryUnit() {
		return factoryUnit;
	}

	public void setFactoryUnit(String factoryUnit) {
		this.factoryUnit = factoryUnit;
	}

	@Override
	public WorkerSumary getModel() {
		return wkSumary;
	}

	public WorkerSumary getWkSumary() {
		return wkSumary;
	}

	public void setWkSumary(WorkerSumary wkSumary) {
		this.wkSumary = wkSumary;
	}

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	public String getNaWorker() {
		return naWorker;
	}

	public void setNaWorker(String naWorker) {
		this.naWorker = naWorker;
	}

	public String getFrDate() {
		return frDate;
	}

	public void setFrDate(String frDate) {
		this.frDate = frDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getDrCode() {
		return drCode;
	}

	public void setDrCode(String drCode) {
		this.drCode = drCode;
	}

	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public String getMaCode() {
		return maCode;
	}

	public void setMaCode(String maCode) {
		this.maCode = maCode;
	}

	public String getMachiningSts() {
		return machiningSts;
	}

	public void setMachiningSts(String machiningSts) {
		this.machiningSts = machiningSts;
	}

	public static class WorkerSumary {
		/**
		 * Cong nhan
		 */
		private SysUsers worker;
		private BigDecimal machiningOk, machiningTotal, machiningNg;
		private BigDecimal machiningEst, machiningTime, machiningSetupTime;

		public BigDecimal getMachiningSetupTime() {
			return machiningSetupTime;
		}

		public void setMachiningSetupTime(BigDecimal machiningSetupTime) {
			this.machiningSetupTime = machiningSetupTime;
		}

		private BigDecimal machiningSlow, machiningSlowPercent;
		// Sua sx
		private BigDecimal machiningRpAmount, machiningRpTime;
		private BigDecimal polishingOk, polishingTotal, polishingNg, polishingTime, polishingRp, polishingRpTime;
		private BigDecimal qcAmount, qcTime;
		private BigDecimal totalTime;
		// Sua qc
		private BigDecimal rpAmount, rpTime;

		public BigDecimal getMachiningOk() {
			return machiningOk;
		}

		public void setMachiningOk(BigDecimal machiningOk) {
			this.machiningOk = machiningOk;
		}

		public BigDecimal getMachiningTotal() {
			return machiningTotal;
		}

		public void setMachiningTotal(BigDecimal machiningTotal) {
			this.machiningTotal = machiningTotal;
		}

		public BigDecimal getMachiningNg() {
			return machiningNg;
		}

		public void setMachiningNg(BigDecimal machiningNg) {
			this.machiningNg = machiningNg;
		}

		public BigDecimal getMachiningEst() {
			return machiningEst;
		}

		public void setMachiningEst(BigDecimal machiningEst) {
			this.machiningEst = machiningEst;
		}

		public BigDecimal getMachiningTime() {
			return machiningTime;
		}

		public void setMachiningTime(BigDecimal machiningTime) {
			this.machiningTime = machiningTime;
		}

		public BigDecimal getMachiningSlow() {
			return machiningSlow;
		}

		public void setMachiningSlow(BigDecimal machiningSlow) {
			this.machiningSlow = machiningSlow;
		}

		public BigDecimal getMachiningSlowPercent() {
			return machiningSlowPercent;
		}

		public void setMachiningSlowPercent(BigDecimal machiningSlowPercent) {
			this.machiningSlowPercent = machiningSlowPercent;
		}

		public BigDecimal getMachiningRpAmount() {
			return machiningRpAmount;
		}

		public void setMachiningRpAmount(BigDecimal machiningRpAmount) {
			this.machiningRpAmount = machiningRpAmount;
		}

		public BigDecimal getMachiningRpTime() {
			return machiningRpTime;
		}

		public void setMachiningRpTime(BigDecimal machiningRpTime) {
			this.machiningRpTime = machiningRpTime;
		}

		public BigDecimal getPolishingOk() {
			return polishingOk;
		}

		public void setPolishingOk(BigDecimal polishingOk) {
			this.polishingOk = polishingOk;
		}

		public BigDecimal getPolishingTotal() {
			return polishingTotal;
		}

		public void setPolishingTotal(BigDecimal polishingTotal) {
			this.polishingTotal = polishingTotal;
		}

		public BigDecimal getPolishingNg() {
			return polishingNg;
		}

		public void setPolishingNg(BigDecimal polishingNg) {
			this.polishingNg = polishingNg;
		}

		public BigDecimal getPolishingTime() {
			return polishingTime;
		}

		public void setPolishingTime(BigDecimal polishingTime) {
			this.polishingTime = polishingTime;
		}

		public BigDecimal getQcAmount() {
			return qcAmount;
		}

		public void setQcAmount(BigDecimal qcAmount) {
			this.qcAmount = qcAmount;
		}

		public BigDecimal getQcTime() {
			return qcTime;
		}

		public void setQcTime(BigDecimal qcTime) {
			this.qcTime = qcTime;
		}

		public BigDecimal getTotalTime() {
			return totalTime;
		}

		public void setTotalTime(BigDecimal totalTime) {
			this.totalTime = totalTime;
		}

		public SysUsers getWorker() {
			return worker;
		}

		public void setWorker(SysUsers worker) {
			this.worker = worker;
		}

		public BigDecimal getRpAmount() {
			return rpAmount;
		}

		public void setRpAmount(BigDecimal rpAmount) {
			this.rpAmount = rpAmount;
		}

		public BigDecimal getRpTime() {
			return rpTime;
		}

		public void setRpTime(BigDecimal rpTime) {
			this.rpTime = rpTime;
		}

		public BigDecimal getPolishingRp() {
			return polishingRp;
		}

		public void setPolishingRp(BigDecimal polishingRp) {
			this.polishingRp = polishingRp;
		}

		public BigDecimal getPolishingRpTime() {
			return polishingRpTime;
		}

		public void setPolishingRpTime(BigDecimal polishingRpTime) {
			this.polishingRpTime = polishingRpTime;
		}

	}
}
