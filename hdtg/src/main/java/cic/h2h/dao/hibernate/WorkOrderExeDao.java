package cic.h2h.dao.hibernate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibm.icu.util.Calendar;

import cic.h2h.form.WorkOrderExeForm;
import common.sql.DataSourceConfiguration;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.QcChkOutSrc;
import entity.QuotationItem;
import entity.WorkOrder;
import entity.WorkOrderExe;
import oracle.jdbc.OracleTypes;

@Repository(value = "workOrderExeDao")
public class WorkOrderExeDao extends H2HBaseDao<WorkOrderExe> {
	private static final Logger lg = Logger.getLogger(WorkOrderExeDao.class);

	public void getInfoWorkOrder(WorkOrder wo, WorkOrderExeForm woForm) throws SQLException {
		Connection connection = getConnection();
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			call = connection.prepareCall("{call MES_EXE.woSts(?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			call.setString("p_wo_id", wo.getId());
			call.setNull("p_quotation_id", OracleTypes.VARCHAR);
			call.setNull("p_or_code", OracleTypes.VARCHAR);
			call.setNull("p_cus_name", OracleTypes.VARCHAR);
			call.setNull("p_wo_code", OracleTypes.VARCHAR);
			call.setNull("p_drw_code", OracleTypes.VARCHAR);
			call.setNull("p_op_code", OracleTypes.VARCHAR);
			call.setNull("p_machine_code", OracleTypes.VARCHAR);
			call.setNull("p_exe_status", OracleTypes.FLOAT);
			call.setNull("p_status", OracleTypes.FLOAT);
			call.setNull("i_From", OracleTypes.FLOAT);
			call.setNull("i_To", OracleTypes.FLOAT);
			call.registerOutParameter("o_Total", Types.INTEGER);
			call.registerOutParameter("cResult", OracleTypes.CURSOR);
			call.execute();
			rs = (ResultSet) call.getObject("cResult");
			if (rs != null) {
				while (rs.next()) {
					wo.setWorkedTime(rs.getBigDecimal("TG_DA_THUC_HIEN"));
					wo.setBalanceTime(rs.getBigDecimal("TG_CON_THUC_TE"));
					if (wo.getNumOfFinishChildren() != null && wo.getNumOfFinishChildren().doubleValue() != 0)
						wo.setAverage(wo.getWorkedTime().divide(wo.getNumOfFinishChildren(), RoundingMode.HALF_UP));
					String cham1 = rs.getString("CHAM");
					if (woForm != null) {
						if (!Formater.isNull(cham1))
							woForm.setCham1(FormatNumber.num2Str(new BigDecimal(cham1)));
						String cham2 = rs.getString("PHAN_TRAM_CHAM");
						if (!Formater.isNull(cham2))
							woForm.setCham2(FormatNumber.num2Str(new BigDecimal(cham2)));
					}
				}
			}
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
		}
	}

	@SuppressWarnings("unchecked")
	public Long checkTotalAmount(String workOrderId) {
		BigDecimal totalAmount = new BigDecimal("0");
		try {
			Criteria criteria = getCurrentSession().createCriteria(WorkOrderExe.class);
			criteria.add(Restrictions.eq("workOrderId.id", workOrderId));
			List<WorkOrderExe> lst = criteria.list();
			for (WorkOrderExe item : lst) {
				if (item.getAmount() != null)
					totalAmount = totalAmount.add(item.getAmount());
			}
		} catch (Exception e) {

			lg.error(e.getMessage(), e);
		}
		return totalAmount.longValue();
	}

	@Autowired
	private QcChkOutSrcDao qcChkOutSrcDao;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private WorkOrderExeDao workOrderExeDao;

	@Override
	public void save(WorkOrderExe woExe) throws Exception {
		// Thoi diem bat dau, toi da sau 8 tieng
		Calendar nextEightHour = Calendar.getInstance();
		nextEightHour.add(Calendar.HOUR, 8);
		if (nextEightHour.getTime().before(woExe.getStartTime()))
			throw new ResourceException("Thời điểm bắt đầu chỉ được sau thời điểm hiện tại 8 giờ!");

		WorkOrder woInDb = woExe.getWorkOrderId();
		// truong hop import woInDb da duoc load
		if (!workOrderDao.getCurrentSession().contains(woInDb))
			workOrderDao.load(woInDb);

		// Chuyen gia cong ngoai
		QcChkOutSrc qcChkOutSrc = null;
		if ("OS".equals(woInDb.getQuotationItemExe().getExeStepId().getStepType().getCode())) {
			// So luong chuyen toi da (= min cac op truoc)
			BigDecimal maxAmount = BigDecimal.valueOf(woInDb.getReadyOsAmount());
			// So luong da chuyen sang QC getNumOfFinishItem giong nhu san xuat thong thuong
			if (woInDb.getNumOfFinishItem() != null)
				maxAmount = maxAmount.subtract(woInDb.getNumOfFinishItem());
			woExe.setAmount(woExe.getTotalAmount());
			if (!Formater.isNull(woExe.getId())) {
				WorkOrderExe oldInDB = workOrderExeDao.getPoJo(woExe.getId());
				// Cong them so luong cu
				maxAmount = maxAmount.add(oldInDB.getTotalAmount());
				// So luong da chuyen GCN
				long qcChkAmount = woInDb.getTotalToOs();
				if (qcChkAmount > 0) {
					// So luong moi sang qc
					BigDecimal newQcAmount = woInDb.getNumOfFinishItem().add(woExe.getTotalAmount());
					newQcAmount = newQcAmount.subtract(oldInDB.getTotalAmount());
					// So luong Qc da thuc hien
					if (qcChkAmount > newQcAmount.longValue())
						throw new ResourceException("Số lượng QC đã chuyển GCN %s lớn hơn tổng số lượng tại QC %s!",
								new Object[] { FormatNumber.num2Str(qcChkAmount), FormatNumber.num2Str(newQcAmount) });
				}
				// Cap nhat so luong chuyen GCN
				qcChkOutSrc = getQcChkOutSrc(woExe);
				qcChkOutSrc.setTotalAmount(woExe.getTotalAmount().longValue());
				qcChkOutSrc.setAmount(woExe.getAmount().longValue());
			} else {
				if (maxAmount.compareTo(new BigDecimal(0)) <= 0)
					throw new ResourceException("Đã chuyển hết số lượng gia công ngoài!");
				// Sinh ban ghi QC mac dinh
				qcChkOutSrc = new QcChkOutSrc(woExe);
			}
			if (maxAmount.compareTo(woExe.getTotalAmount()) < 0)
				throw new ResourceException("Số lượng chuyển %s, lớn hơn số lượng chuyển tối đa %s !",
						new Object[] { FormatNumber.num2Str(woExe.getTotalAmount()), maxAmount });
			woExe.setAmount(woExe.getTotalAmount());
			woExe.setNgAmount(0l);

		} else {
			woInDb.setSumary(Boolean.FALSE);
			BigDecimal maxTotalAmount = Boolean.TRUE.equals(woExe.getNgRepaire())
					? (woInDb.getTodoNgAmount() == null ? null : new BigDecimal(woInDb.getTodoNgAmount()))
					: woInDb.getTodoAmount();
			// Truong hop sua, revert gia tri hien tai
			if (!Formater.isNull(woExe.getId())) {
				WorkOrderExe oldInDB = workOrderExeDao.getPoJo(woExe.getId());
				maxTotalAmount = maxTotalAmount.add(oldInDB.getTotalAmount());
			} else {
				if (!Boolean.TRUE.equals(woExe.getNgRepaire()) && maxTotalAmount.compareTo(new BigDecimal(0)) == 0)
					throw new ResourceException("Lệnh sản xuất đã hoàn thành!");
			}
			if (maxTotalAmount.compareTo(woExe.getTotalAmount()) < 0) {
				String message = Boolean.TRUE.equals(woExe.getNgRepaire())
						? "Tổng số chi tiết cần sửa = %s, nhỏ hơn tổng số chi tiết %s!"
						: "Tổng số chi tiết cần sản xuất = %s, nhỏ hơn tổng số chi tiết %s!";
				throw new ResourceException(String.format(message, new Object[] { FormatNumber.num2Str(maxTotalAmount),
						FormatNumber.num2Str(woExe.getTotalAmount()) }));

			}
		}
		// Kiem tra may gia cong, thoi gian gia cong
		if (woExe.getAstMachine() != null) {
			WorkOrderExe sameTimeWoe = getSameTimeWoe(woExe);
			if (sameTimeWoe != null) {
				throw new ResourceException(
						String.format("Máy %s đã tồn tại thời gian gia công từ %s đến %s, tại LSX %s",
								new Object[] { sameTimeWoe.getAstMachine().getCode(), sameTimeWoe.getStartTimeStr(),
										sameTimeWoe.getEndTimeStr(), sameTimeWoe.getWorkOrderId().getCode() }));
			}

		}
		QuotationItem qi = woInDb.getQuotationItemExe().getQuotationItemId();
		// Luu du thua de tim kiem
		woExe.setCompany(qi.getQuotationId().getCompany());
		super.save(woExe);
		if ("OS".equals(woInDb.getQuotationItemExe().getExeStepId().getStepType().getCode()))
			qcChkOutSrcDao.save(qcChkOutSrc);
		quotationItemDao.flush();
		// Luu du thua so luong chi tiet hoan thanh
		qi.setNmOfFnsItem(quotationItemDao.numOfFinishItem(qi.getId()));
	}

	private QcChkOutSrc getQcChkOutSrc(WorkOrderExe woExe) {
		return (QcChkOutSrc) getCurrentSession().createCriteria(QcChkOutSrc.class)
				.add(Restrictions.eq("workOrderExe", woExe)).uniqueResult();
	}

	private WorkOrderExe getSameTimeWoe(WorkOrderExe woExe) {
		Criteria cSameTimeWoe = getCurrentSession().createCriteria(WorkOrderExe.class)
				.add(Restrictions.eq("astMachine", woExe.getAstMachine()));
		// Lenh co (thoi diem bat dau nam trong khoang va kg bang thoi diem ket thuc)
		// hoac (thoi diem ket thuc nam trong khoang va kg bang thoi diem bat dau)
		cSameTimeWoe.add(Restrictions.or(
				Restrictions.and(Restrictions.between("startTime", woExe.getStartTime(), woExe.getEndTime()),
						Restrictions.ne("startTime", woExe.getEndTime())),
				Restrictions.and(Restrictions.between("endTime", woExe.getStartTime(), woExe.getEndTime()),
						Restrictions.ne("endTime", woExe.getStartTime()))));
		if (!Formater.isNull(woExe.getId()))
			cSameTimeWoe.add(Restrictions.ne("id", woExe.getId()));
		cSameTimeWoe.setMaxResults(1);
		return (WorkOrderExe) cSameTimeWoe.uniqueResult();
	}

	@Autowired
	private QuotationItemDao quotationItemDao;

	@Override
	public void del(WorkOrderExe woExe) throws Exception {
		// Kiem tra so luong NG voi so luong NG da thuc hien sua
		if (!Boolean.TRUE.equals(woExe.getNgRepaire())) {
			WorkOrderExe oldInDb = getPoJo(woExe.getId());
			if (oldInDb.getNgAmount() != null && oldInDb.getNgAmount() != 0) {
				WorkOrder woInDb = workOrderDao.getSumaryWO(woExe.getWorkOrderId().getId());
				Double woGnAmount = woInDb.getNgAmount().doubleValue();
				woGnAmount -= oldInDb.getNgAmount();
				if (woGnAmount < 0)
					throw new ResourceException(
							"Sau khi xóa, số lượng NG của LSX %s nhỏ hơn 0, cần kiểm tra lại dữ liệu gia công của LSX !",
							FormatNumber.num2Str(new BigDecimal(woGnAmount)));

			}
			// Xoa chuyen gia cong ngoai
			if ("OS".equals(woExe.getWorkOrderId().getQuotationItemExe().getExeStepId().getStepType().getCode()))
				qcChkOutSrcDao.del(woExe);
		}
		super.del(woExe);
		// Luu du thua so luong chi tiet hoan thanh
		quotationItemDao.flush();
		if ("OS".equals(woExe.getWorkOrderId().getQuotationItemExe().getExeStepId().getStepType().getCode())) {
			// Kiem tra so luong da mang di ra cong ngoai
			workOrderDao.refresh(woExe.getWorkOrderId());
			if (woExe.getWorkOrderId().getToOsedAmount() < woExe.getWorkOrderId().getTotalToOs())
				throw new ResourceException("Sau khi xóa, số lượng đã gia công ngoài %s lớn hơn số lượng tại QC %s",
						new Object[] { woExe.getWorkOrderId().getTotalToOs(), woExe.getWorkOrderId().getToOsedAmount() });
		}
		QuotationItem qi = woExe.getWorkOrderId().getQuotationItemExe().getQuotationItemId();
		// Luu du thua so luong chi tiet hoan thanh
		qi.setNmOfFnsItem(quotationItemDao.numOfFinishItem(qi.getId()));
	}
}
