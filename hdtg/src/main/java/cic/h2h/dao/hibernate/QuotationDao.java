package cic.h2h.dao.hibernate;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.ibm.icu.util.Calendar;

import cic.h2h.form.BookingStatusForm;
import common.sql.DataSourceConfiguration;
import common.util.Formater;
import common.util.JsonUtils;
import common.util.ResourceException;
import entity.Company;
import entity.ExeStepType;
import entity.Quotation;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.frwk.UserLog;
import oracle.jdbc.OracleTypes;

@Repository(value = "quotationDao")
public class QuotationDao extends H2HBaseDao<Quotation> {
	private static Logger log = Logger.getLogger(QuotationDao.class);

	public void updateSts(Quotation quotation) {
		getCurrentSession().doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				CallableStatement cStmt = null;
				ResultSet rs1 = null;
				try {
					cStmt = connection.prepareCall("{CALL MES_EXE.orderSts(?,?,?,?,?,?,?,?,?,?,?)}");
					cStmt.setString("p_or_id", quotation.getId());
					cStmt.setNull("p_customer_code", OracleTypes.VARCHAR);
					cStmt.setNull("p_customer_name", OracleTypes.VARCHAR);
					cStmt.setNull("p_order_from", OracleTypes.DATE);
					cStmt.setNull("p_order_to", OracleTypes.DATE);
					cStmt.setNull("p_exe_status", OracleTypes.FLOAT);
					cStmt.setNull("p_status", OracleTypes.FLOAT);
					cStmt.setNull("i_From", OracleTypes.FLOAT);
					cStmt.setNull("i_To", OracleTypes.FLOAT);
					cStmt.registerOutParameter("o_Total", Types.INTEGER);
					cStmt.registerOutParameter("cResult", OracleTypes.CURSOR);
					cStmt.execute();
					rs1 = (ResultSet) cStmt.getObject("cResult");
					if (rs1 != null) {
						while (rs1.next()) {
							quotation.setExeStatus(rs1.getShort("tinh_trang_gia_cong"));
							quotation.setOrderSts(rs1.getShort("tinh_trang_don_hang"));
						}
					}
				} catch (SQLException e) {
					throw e;
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				} finally {
					DataSourceConfiguration.releaseSqlResources(rs1, cStmt, connection);
				}
			}

		});
	}

	public void sts(Quotation quotation) {
		getCurrentSession().doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs1 = null;
				CallableStatement cStmt = null;
				try {
					cStmt = connection.prepareCall("{CALL hdtg_report.booking_sts4(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
					cStmt.setString("p_report_type", "q");
					cStmt.setNull("p_late_delivery", OracleTypes.INTEGER);
					cStmt.setNull("p_tobe_late", OracleTypes.INTEGER);
					cStmt.setNull("p_step_type", OracleTypes.VARCHAR);
					cStmt.setString("p_company_id", quotation.getCompany().getId());

					cStmt.setNull("p_from_date", OracleTypes.DATE);
					cStmt.setNull("p_to_date", OracleTypes.DATE);
					// Ma ban ve
					cStmt.setNull("p_drw_code", OracleTypes.VARCHAR);
					// Ma quan ly
					cStmt.setNull("p_manage_code", OracleTypes.VARCHAR);
					// Ma khach hang
					cStmt.setNull("p_customer_id", OracleTypes.VARCHAR);
					// Ma don hang
					cStmt.setString("p_order_code", quotation.getCode());
					cStmt.setInt("i_From", 0);
					cStmt.setInt("i_To", 1);
					cStmt.registerOutParameter("o_Total", OracleTypes.INTEGER);
					cStmt.registerOutParameter("cResult", OracleTypes.CURSOR);
					// Thu thi
					cStmt.execute();
					rs1 = (ResultSet) cStmt.getObject("cResult");
					BookingStatusForm.BookingStatus rs = new BookingStatusForm.BookingStatus();
					if (rs1.next()) {
						rs.setType("q");
						rs.read(rs1);
						quotation.setTotalEstTime(rs.getBooking().getPlan().getExeTime());
						quotation.setSetupTime(rs.getBooking().getTarget().getSetupTime());
						quotation.setStartTime(rs.getStartTime());
						quotation.setEndTime(rs.getEndTime());
						quotation.setExeTime(rs.getExe().getExe().getExeTime());
						quotation.setEstTime(rs.getExe().getPlan().getExeTime());
					}

				} catch (Exception e) {
					throw new SQLException(e);
				} finally {
					DataSourceConfiguration.releaseSqlResources(rs1, cStmt, null);
				}

			}
		});
	}

	public Quotation getQuotationByCode(String code, Company company) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Quotation.class);
		criteria.add(Restrictions.eq("code", code));
		criteria.add(Restrictions.eq("company", company));
		List<Quotation> tmp = criteria.setMaxResults(1).list();
		if (tmp.isEmpty())
			return null;
		return tmp.get(0);
	}

	@Autowired
	private QuotationItemDao quotationItemDao;

	public void del(Quotation quotation) throws Exception {
		for (QuotationItem qie : quotation.getQuotationItemList())
			quotationItemDao.del(qie);
		getCurrentSession().delete(quotation);
	}

	@SuppressWarnings("unchecked")
	public List<Quotation> getAllQuotation() {
		Criteria c = getCurrentSession().createCriteria(Quotation.class);
		c.add(Restrictions.sqlRestriction("exists (select 1 from quotation_item qi)"));
		return c.list();
	}
}
