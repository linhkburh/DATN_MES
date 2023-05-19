package cic.h2h.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import common.sql.DataSourceConfiguration;
import common.util.Formater;
import common.util.ResourceException;
import entity.AstMachine;
import entity.Company;
import entity.DashBroadNG;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.WorkOrder;

@Repository(value = "workOrderDao")
public class WorkOrderDao extends H2HBaseDao<WorkOrder> {

	private static Logger log = Logger.getLogger(WorkOrderDao.class);

	@Override
	public void del(WorkOrder wo) throws Exception {
		if (!wo.getWorkOrderExes().isEmpty())
			throw new ResourceException(
					"Hình thức gia công %s, công đoạn %s, đã tồn tại lệnh sản xuất %s, đã được thực hiện, không thể xóa!",
					new Object[] { wo.getQuotationItemExe().getExeStepId().getStepName(),
							wo.getQuotationItemExe().getExeStepId().getStepType().getName(), wo.getCode() });
		getCurrentSession().delete(wo);
	}

	@Override
	public void save(WorkOrder wo) throws ResourceException {
		if (!Formater.isNull(wo.getId())) {
			WorkOrder woIndb = this.get(wo.getId());
			if (woIndb != null) {
				if (wo.getAmount().doubleValue() < woIndb.getNumOfFinishChildren().doubleValue())
					throw new ResourceException(
							"Số lượng chi tiết của LSX %s: %s, nhỏ hơn số lượng đã được sản xuất: %s",
							new Object[] { wo.getCode(), wo.getAmount(), woIndb.getAmount() });
				getCurrentSession().merge(wo);
			}

		} else
			getCurrentSession().save(wo);
	}

	@SuppressWarnings("unchecked")
	public List<WorkOrder> getLstWorkOrderByQuotationItemExeId(String quotationItemExeId) {
		List<WorkOrder> lst = null;
		try {
			Criteria criteria = getCurrentSession().createCriteria(WorkOrder.class);
			criteria.add(Restrictions.eq("quotationItemExe.id", quotationItemExeId));
			lst = criteria.list();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return lst;
	}

	public WorkOrder getWorkOrderByCode(String code, Company company) {
		Criteria c = getCurrentSession().createCriteria(WorkOrder.class).add(Restrictions.eq("code", code));
		c.createAlias("quotationItemExe", "qie");
		c.createAlias("qie.quotationItemId", "qi");
		return (WorkOrder) c.add(Restrictions.eq("qi.company", company)).uniqueResult();
	}

	public HashMap<String, List<?>> getDashBroad(Date formDate, Date toDate, String companyId) throws Exception {
		HashMap<String, List<?>> mRs = new HashMap<String, List<?>>();
		Connection connection = getConnection();
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			call = connection.prepareCall("{call hdtg_report.orderDashBoard(?,?,?,?) }");
			if (Formater.isNull(companyId))
				call.setNull("p_company_id", OracleTypes.VARCHAR);
			else
				call.setString("p_company_id", companyId);
			if (formDate != null)
				call.setDate("p_from_date", new java.sql.Date(formDate.getTime()));
			else
				call.setNull("p_from_date", OracleTypes.DATE);
			if (toDate != null)
				call.setDate("p_to_date", new java.sql.Date(toDate.getTime()));
			else
				call.setNull("p_to_date", OracleTypes.DATE);
			if (Formater.isNull(companyId))
				call.setNull("p_company_id", OracleTypes.VARCHAR);
			else
				call.setString("p_company_id", companyId);
			call.registerOutParameter("cResult", OracleTypes.CURSOR);
			call.execute();
			rs = (ResultSet) call.getObject("cResult");
			List<String[]> orderStatus = new ArrayList<String[]>();
			mRs.put("orderStatus", orderStatus);
			List<String[]> workStatus = new ArrayList<String[]>();
			mRs.put("workStatus", workStatus);
			if (rs != null) {
				while (rs.next()) {
					// trang thai don hang
					String lsxChuaThucHien = rs.getString("SL_CHUA_THUC_HIEN_SX");
					orderStatus.add(new String[] { "Chưa Thực Hiện", lsxChuaThucHien });
					String donHangConThoiHan = rs.getString("SL_CON_THOI_HAN");
					orderStatus.add(new String[] { "Còn Thời Hạn", donHangConThoiHan });
					String donHangConThoiHanCham = rs.getString("sl_con_thoi_han_cham");
					orderStatus.add(new String[] { "Còn Thời Hạn (chậm)", donHangConThoiHanCham });
					String donHangHetThoiHan = rs.getString("SL_HET_THOI_HAN");
					orderStatus.add(new String[] { "Hết Thời Hạn", donHangHetThoiHan });
					String donHangHoanThanh = rs.getString("SL_HOAN_THANH");
					orderStatus.add(new String[] { "Hoàn Thành", donHangHoanThanh });

					// trang thai san xuat
					String lsxChamTienDo = rs.getString("SL_CHAM_TIEN_DO");
					workStatus.add(new String[] { "Chậm Tiến Độ", lsxChamTienDo });
					String lsxDungTienDo = rs.getString("SL_DUNG_TIEN_DO");
					workStatus.add(new String[] { "Đúng Tiến Độ", lsxDungTienDo });
					String lsxVuotTienDo = rs.getString("SL_VUOT_TIEN_DO");
					workStatus.add(new String[] { "Vượt Tiến Độ", lsxVuotTienDo });

				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
		}
		return mRs;
	}

	public HashMap<String, HashMap<String, List<?>>> getDashBroadLine(String reportType, String companyId) {
		HashMap<String, HashMap<String, List<?>>> mRs = new HashMap<String, HashMap<String, List<?>>>();
		Connection connection = getConnection();
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			call = connection.prepareCall("{call hdtg_report.rpSale(?,?,?,?,?) }");
			call.setString("p_report_type", reportType);
			if (Formater.isNull(companyId))
				call.setNull("p_company_id", OracleTypes.VARCHAR);
			else
				call.setString("p_company_id", companyId);
			call.setNull("p_from_date", OracleTypes.DATE);
			call.setNull("p_to_date", OracleTypes.DATE);
			call.registerOutParameter("c_result", OracleTypes.CURSOR);
			call.execute();
			rs = (ResultSet) call.getObject("c_result");
			HashMap<String, List<?>> map = new HashMap<String, List<?>>();
			List<Integer> lstbaogia = new ArrayList<Integer>();
			List<Integer> lstdonhang = new ArrayList<Integer>();
			List<Integer> lsttong = new ArrayList<Integer>();
			List<String> lstthoigian = new ArrayList<String>();
			map.put("Báo giá", lstbaogia);
			map.put("Đơn hàng", lstdonhang);
			map.put("Tổng", lsttong);
			map.put("Thời gian", lstthoigian);
			mRs.put("line", map);
			if (rs != null) {
				while (rs.next()) {
					int donGia = rs.getInt("BAO_GIA");
					lstbaogia.add(donGia);
					int donHang = rs.getInt("DON_HANG");
					lstdonhang.add(donHang);
					int tong = rs.getInt("TONG");
					lsttong.add(tong);
					String thoiGian = rs.getString("THOI_GIAN");
					lstthoigian.add(thoiGian);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
		}
		return mRs;
	}

	public Map<String, List<DashBroadNG>> getDashBroadNg(String reportType, String companyId) {
		LinkedHashMap<String, List<DashBroadNG>> map = new LinkedHashMap<String, List<DashBroadNG>>();
		Connection connection = getConnection();
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			call = connection.prepareCall("{call hdtg_report.ng(?,?,?,?,?) }");
			if (Formater.isNull(companyId))
				call.setNull("p_company_id", OracleTypes.VARCHAR);
			else
				call.setString("p_company_id", companyId);
			call.setString("p_report_type", reportType);
			call.setNull("p_from_date", OracleTypes.DATE);
			call.setNull("p_to_date", OracleTypes.DATE);
			call.registerOutParameter("c_result", OracleTypes.CURSOR);
			call.execute();
			rs = (ResultSet) call.getObject("c_result");

			if (rs != null) {
				while (rs.next()) {
					List<DashBroadNG> ngs = new ArrayList<DashBroadNG>();
					String key = rs.getString("THOI_GIAN");
					DashBroadNG broadNG = new DashBroadNG();
					broadNG.setAmount(new BigDecimal(rs.getString("amount")));
					broadNG.setLevel(rs.getString("value"));
					broadNG.setNgAmount(new BigDecimal(rs.getString("ng_amount")));
					broadNG.setTotalAmount(new BigDecimal(rs.getString("total_amount")));
					broadNG.setTime(key);
					if (map.get(key) == null) {
						ngs.add(broadNG);
						map.put(key, ngs);
					} else {
						ngs = map.get(key);
						ngs.add(broadNG);
						map.put(key, ngs);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
		}
		return map;
	}

	public WorkOrder getByCode(String code) {
		return (WorkOrder) getThreadSession().createCriteria(WorkOrder.class).add(Restrictions.eq("code", code))
				.uniqueResult();
	}

	public WorkOrder getSumaryWO(Serializable recordId) {
		WorkOrder rs = null;
		Session s = openNewSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			rs = s.get(getModelClass(), recordId);
			rs.sumary();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			s.close();
		}
		return rs;
	}

	@Autowired
	private QuotationItemExeDao quotationItemExeDao;

	public void save(List<WorkOrder> workOrders) throws Exception {
		QuotationItem qi = quotationItemExeDao.get(workOrders.get(0).getQuotationItemExe().getId())
				.getQuotationItemId();
		// Xoa WO chua thuc hien
		for (QuotationItemExe qie : qi.getQuotationItemAllExeList()) {
			if (Boolean.TRUE.equals(qie.getExeStepId().getProgram()))
				continue;
			Iterator<WorkOrder> woIter = qie.getWorkOrders().iterator();
			while (woIter.hasNext()) {
				WorkOrder woInDb = woIter.next();
				if (woInDb.getWorkOrderExes().isEmpty()) {
					del(woInDb);
					woIter.remove();
				}
			}
		}
		flush();

		// Luu danh sach
		// Cac LSX tao cung thoi diem
		Date createDate = Calendar.getInstance().getTime();
		for (WorkOrder wo : workOrders) {
			QuotationItemExe qie = quotationItemExeDao.get(wo.getQuotationItemExe().getId());
			if (qie.getUnitExeTime() == null)
				throw new ResourceException("Công đoạn %s-%s, chưa được lưu thông tin thời gian gia công",
						new Object[] { qie.getExeStepId().getStepType().getName(), qie.getExeStepId().getStepName() });

			if ("OS".equals(qie.getExeStepId().getStepType().getCode())) {
				wo.setAstMachine(null);
				wo.setWaitTime(null);
			}
			wo.setCreator(getSessionUser());
			// Tat ca cac lenh cung ngay tao
			wo.setCreateDate(createDate);
			save(wo);
		}
	}

	public WorkOrder get(QuotationItemExe qie, AstMachine astMachine) {
		return (WorkOrder) getCurrentSession().createCriteria(WorkOrder.class).add(Restrictions.eq("quotationItemExe", qie))
				.add(Restrictions.eq("astMachine", astMachine)).uniqueResult();
	}
}
