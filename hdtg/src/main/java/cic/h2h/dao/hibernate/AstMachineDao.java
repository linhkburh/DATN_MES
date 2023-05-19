package cic.h2h.dao.hibernate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.sql.DataSourceConfiguration;
import common.util.Formater;
import entity.AstMachine;
import entity.Company;
import entity.MachineExeStepType;
import entity.QuotationItemExe;
import entity.WorkOrder;
import oracle.jdbc.OracleTypes;

@Repository("astMachineDao")
public class AstMachineDao extends H2HBaseDao<AstMachine> {

	private static final Logger lg = Logger.getLogger(AstMachineDao.class);

	public void save(AstMachine astMachine) throws Exception {
		if (Formater.isNull(astMachine.getId())) {
			if (!Formater.isNull(astMachine.getExeStepTypes())) {
				for (MachineExeStepType item : astMachine.getExeStepTypes()) {
					if (item == null)
						continue;
					item.setId(null);
					item.setAstMachine(astMachine);
				}
			}
		} else {
			AstMachine oldAstMachine = getObject(AstMachine.class, astMachine.getId());
			List<MachineExeStepType> delObj = new ArrayList<MachineExeStepType>();
			Map<String, MachineExeStepType> map = new HashMap<String, MachineExeStepType>();

			for (MachineExeStepType item : astMachine.getExeStepTypes()) {
				item.setAstMachine(astMachine);
				if (Formater.isNull(item.getId()))
					continue;
				map.put(item.getId(), item);
			}
			if (oldAstMachine.getExeStepTypes() != null) {
				for (MachineExeStepType item : oldAstMachine.getExeStepTypes()) {
					if (map.get(item.getId()) == null) {
						delObj.add(item);
					}
				}
			}
			for (MachineExeStepType funEndpoint : delObj) {
				getCurrentSession().delete(funEndpoint);
			}
		}

		super.save(astMachine);
	}

//	public AstMachine getAstMachineByCode(String code) {
//		AstMachine astMachine = new AstMachine();
//		Criteria criteria = getCurrentSession().createCriteria(AstMachine.class);
//		criteria.add(Restrictions.eq(propertyName, value))
//		
//		
//		return astMachine;
//	}

	public void getInfoMachine(AstMachine machine) throws SQLException {
		Connection connection = getConnection();
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			call = connection.prepareCall("{call MES_EXE.machineSts(?,?,?,?,?,?,?,?) }");
			call.setString("p_machine_id", machine.getId());
			call.setNull("p_machine_code", OracleTypes.VARCHAR);
			call.setNull("p_machine_name", OracleTypes.VARCHAR);
			call.setNull("p_exe_status", OracleTypes.FLOAT);
			call.setNull("i_From", OracleTypes.FLOAT);
			call.setNull("i_To", OracleTypes.FLOAT);
			call.registerOutParameter("o_Total", Types.INTEGER);
			call.registerOutParameter("cResult", OracleTypes.CURSOR);
			call.execute();
			rs = (ResultSet) call.getObject("cResult");
			if (rs.next()) {
				machine.setWaitTime(rs.getBigDecimal("tg_con_lai_thuc_te"));
				if(machine.getWaitTime()!=null)
					machine.setWaitTime(machine.getWaitTime().divide(new BigDecimal(60),3,RoundingMode.HALF_UP));
			} else
				machine.setWaitTime(new BigDecimal(0));
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
		}
	}

	public List<AstMachine> getTopFreeMachine(int iTop, Company company) throws SQLException {
		List<AstMachine> lstMachine = new ArrayList<AstMachine>();
		Connection connection = getConnection();
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			call = connection.prepareCall("{call MES_EXE.getMachineForWo(?,?,?) }");
			call.setInt("p_num_of_machine", iTop);
			call.setString("p_companyId", company.getId());
			call.registerOutParameter("cResult", OracleTypes.CURSOR);
			call.execute();
			rs = (ResultSet) call.getObject("cResult");
			while (rs.next()) {
				AstMachine machine = this.get(AstMachine.class, rs.getString("id"));
				machine.setWaitTime(rs.getBigDecimal("tg_con_lai_thuc_te"));
				lstMachine.add(machine);
			}
			return lstMachine;
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
		}
	}

	@SuppressWarnings("unchecked")
	public List<AstMachine> getAll(Company company) {
		Criteria c = getCurrentSession().createCriteria(AstMachine.class);
		if (company != null)
			c.add(Restrictions.eq("company", company));
		return c.addOrder(Order.asc("code")).list();
	}

	/**
	 * Cap nhat thoi gian cho doi voi tong lsx cua mot may, loai tru cong doan qieId
	 * 
	 * @param iTop
	 * @param qieId Id cong doan loai tru
	 * @return
	 * @throws SQLException
	 */
	public List<AstMachine> getTopFreeMachineExcludeQie(int iTop, String qieId, Company company) throws SQLException {
		List<AstMachine> lstMachine = new ArrayList<AstMachine>();
		Connection connection = getConnection();
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			call = connection.prepareCall("{call MES_EXE.getMachineForWoExcludeQie(?,?,?,?) }");
			call.setInt("p_num_of_machine", iTop);
			if (Formater.isNull(qieId))
				call.setNull("p_qieId", OracleTypes.VARCHAR);
			else
				call.setString("p_qieId", company.getId());
			call.setString("p_companyId", company.getId());
			call.registerOutParameter("cResult", OracleTypes.CURSOR);
			call.execute();
			rs = (ResultSet) call.getObject("cResult");
			while (rs.next()) {
				AstMachine machine = this.get(AstMachine.class, rs.getString("id"));
				BigDecimal waitTime = rs.getBigDecimal("tg_con_lai_thuc_te");
				if (waitTime == null)
					machine.setWaitTime(new BigDecimal(0));
				else
					machine.setWaitTime(waitTime.divide(new BigDecimal(60), 3, RoundingMode.HALF_UP));
				lstMachine.add(machine);
			}
			return lstMachine;
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
		}
	}

	public List<AstMachine> getTopFreeMachineForMakeWo(int iTop, String qiId, Company company) throws SQLException {
		List<AstMachine> lstMachine = new ArrayList<AstMachine>();
		Connection connection = getConnection();
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			call = connection.prepareCall("{call MES_EXE.getTopFreeMachineForMakeWo(?,?,?,?) }");
			call.setInt("p_num_of_machine", iTop);
			if (Formater.isNull(qiId))
				call.setNull("p_qieId", OracleTypes.VARCHAR);
			else
				call.setString("p_qieId", qiId);
			call.setString("p_companyId", company.getId());
			call.registerOutParameter("cResult", OracleTypes.CURSOR);
			call.execute();
			rs = (ResultSet) call.getObject("cResult");
			while (rs.next()) {
				AstMachine machine = this.get(AstMachine.class, rs.getString("id"));
				BigDecimal waitTime = rs.getBigDecimal("tg_con_lai_thuc_te");
				if (waitTime == null)
					machine.setWaitTime(new BigDecimal(0));
				else
					machine.setWaitTime(waitTime.divide(new BigDecimal(60), 3, RoundingMode.HALF_UP));
				lstMachine.add(machine);
			}
			return lstMachine;
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
		}
	}

	@SuppressWarnings("unchecked")
	public void updateWaitTime(AstMachine astMachine, QuotationItemExe qie) {
		astMachine.setWaitTime(new BigDecimal(0));
		Criteria c = getCurrentSession().createCriteria(WorkOrder.class).add(Restrictions.eq("astMachine", astMachine))
				.add(Restrictions.not(Restrictions.eq("quotationItemExe", qie)));
		List<WorkOrder> lstMachineWo = c.list();
		for (WorkOrder wo : lstMachineWo) {
			if (wo.getNumOfFinishChildren() != null
					&& wo.getNumOfFinishChildren().longValue() == wo.getAmount().longValue())
				continue;
			long remainAmount = wo.getNumOfFinishChildren() == null ? wo.getAmount().longValue()
					: wo.getAmount().longValue() - wo.getNumOfFinishChildren().longValue();
			BigDecimal addWaitTime = astMachine.getWaitTime()
					.add(new BigDecimal(remainAmount)
							.multiply(wo.getQuotationItemExe().getQuotationItemId().getExeTime())
							.divide(new BigDecimal(60), 3, RoundingMode.HALF_UP));
			astMachine.setWaitTime(addWaitTime);
		}

	}
	@SuppressWarnings("deprecation")
	public AstMachine getMachineByCode(String code) {
		return (AstMachine) getCurrentSession().createCriteria(AstMachine.class).add(Restrictions.eq("code", code)).uniqueResult();
	}

	public AstMachine getMachineByCode(String machineCode, Company company) {
		
		return (AstMachine) getCurrentSession().createCriteria(AstMachine.class).add(Restrictions.eq("code", machineCode)).add(Restrictions.eq("company", company)).uniqueResult();
		
	}
}
