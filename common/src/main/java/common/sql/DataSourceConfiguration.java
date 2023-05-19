package common.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class DataSourceConfiguration {
	private static Logger logger = Logger.getLogger(DataSourceConfiguration.class);
	private static Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
	private static Object lock = new Object();

	static Connection connection = null;
	static String driverName = "oracle.jdbc.driver.OracleDriver";

	static String portNumber = "1521";

	/**
	 * Tra ve gia tri datasource duoc luu trong map, neu chua co se khoi tao
	 *
	 * @throws Exception
	 * @return
	 * @param dataSourceName : ten cua dataSource duoc doc trong file bvprop.config.Lookup.properties
	 */
	public static DataSource getDataSource(String dataSourceName) {

		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");

		DataSource dsRes = (DataSource) dataSourceMap.get(dataSourceName);
		try {
			Context ctx = new InitialContext(env);

			synchronized (lock) {
				if (dsRes == null) {
					try {
						dsRes = (DataSource) ctx.lookup(dataSourceName);
						dataSourceMap.put(dataSourceName, dsRes);
					} catch (NamingException e1) {
						logger.error("Loi", e1);
					}

				}
				return dsRes;
			}

		} catch (Exception ex1) {
			logger.error("Loi", ex1);
			return null;
		}

	}

	/**
	 * Tra ve connection tuong ung dataSourceName
	 *
	 * @throws Exception
	 * @return
	 * @param dataSourceName : ten cua dataSource duoc doc trong file bvprop.config.Lookup.properties
	 */

	/**
	 * Tra ve connection tuong ung dataSourceName=DATASOURCE
	 *
	 * @throws Exception
	 * @return
	 */

	public static void releaseSqlResources(ResultSet rs, PreparedStatement ps, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Loi", e);
			}
			rs = null;
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				logger.error("Loi", e);
			}
			ps = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Loi", e);
			}
			conn = null;
		}
	}

	public static void releaseSqlResources(ResultSet rs, Statement ps, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Loi", e);
			}
			rs = null;
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				logger.error("Loi", e);
			}
			ps = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Loi", e);
			}
			conn = null;
		}
	}

	public static void releaseSqlResources(ResultSet rs, CallableStatement callableStatement, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Loi", e);
			}
			rs = null;
		}
		if (callableStatement != null) {
			try {
				callableStatement.close();
			} catch (SQLException e) {
				logger.error("Loi", e);
			}
			callableStatement = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Loi", e);
			}
			conn = null;
		}
	}
}
