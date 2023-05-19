package cic.h2h.dao.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;
import org.hibernate.jdbc.Work;

public class CheckApproveData implements Work {
	private static final Logger logger = Logger.getLogger(CheckApproveData.class);
	private String recordId;
	private Boolean existApproveData;
	private String tableName;

	public Boolean getExistApproveData() {
		return existApproveData;
	}

	public CheckApproveData(String recordId, String tableName) {
		this.recordId = recordId;
		this.tableName = tableName;
	}

	@Override
	public void execute(Connection connection) throws SQLException {
		CallableStatement call = connection.prepareCall("{? = call mes_quotation.checkApproveData(?,?) }");
		call.registerOutParameter(1, Types.INTEGER);
		call.setString(2, tableName);
		call.setString(3, recordId);
		call.execute();
		existApproveData = call.getInt(1) == 1;
		logger.info("existApproveData: " + existApproveData);
	}
}
