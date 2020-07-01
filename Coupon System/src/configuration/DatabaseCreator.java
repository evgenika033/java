package configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import exceptions.DatabaseException;
import utils.StringHelper;

/**
 * 
 * @author Evgenia use this class for create new database and tables
 *
 */
public class DatabaseCreator {
	private String sqlConnection;
	private Properties properties;
	// private String sqlTable1;

	/**
	 * Constructor
	 */
	public DatabaseCreator() {
		sqlConnection = PropertiesController.getSqlConnectionMaster();
		properties = PropertiesController.getProperties();
	}

	/**
	 * check if datatbase exist
	 * 
	 * @return
	 * @throws DatabaseException
	 */
	private boolean isDatabaseExist() throws DatabaseException {
		String sql = "IF EXISTS (SELECT name FROM master.sys.databases WHERE name = N'"
				+ properties.getProperty(StringHelper.DB_DATABASE_NAME) + "')";
		try (Connection con = DriverManager.getConnection(sqlConnection);) {
			ResultSet rs = con.getMetaData().getCatalogs();
			while (rs.next()) {
				String catalogs = rs.getString(1);
				if (properties.getProperty(StringHelper.DB_DATABASE_NAME).equals(catalogs)) {
					System.out.println(
							"the database " + properties.getProperty(StringHelper.DB_DATABASE_NAME) + " exists");
					return true;
				}
			}
			return false;

		} catch (SQLException e) {
			throw new DatabaseException(StringHelper.DATABASE_EXEPTION_CREATE + e);
		}
	}

	/**
	 * drop database
	 * 
	 * @throws DatabaseException
	 */
	private void dropDatabase() throws DatabaseException {
		String sql = StringHelper.SQL_DROP_DATABASE + properties.getProperty(StringHelper.DB_DATABASE_NAME);
		try (Connection con = DriverManager.getConnection(sqlConnection);) {
			Statement statement = con.createStatement();
			int k = statement.executeUpdate(sql);
			System.out.println("database is droped");

		} catch (SQLException e) {
			throw new DatabaseException(StringHelper.DATABASE_EXEPTION_CREATE + e);
		}
	}

	/**
	 * 
	 * @return
	 * @throws DatabaseException
	 */
	public int createDatabase() throws DatabaseException {
		if (Boolean.valueOf(properties.getProperty(StringHelper.DB_CREATE_DATABASE))) {
			// check if database exist
			if (isDatabaseExist()) {
				// delete database
				dropDatabase();
			}
			// create new database
			try (Connection con = DriverManager.getConnection(sqlConnection);) {
				Statement statement = con.createStatement();
				System.out.println("sql query execution: " + StringHelper.SQL_CREATE_DATABASE
						+ properties.getProperty(StringHelper.DB_DATABASE_NAME));
				return statement.executeUpdate(
						StringHelper.SQL_CREATE_DATABASE + properties.getProperty(StringHelper.DB_DATABASE_NAME));
			} catch (SQLException e) {
				throw new DatabaseException(StringHelper.DATABASE_EXEPTION_CREATE + e);
			}
		} else {
			throw new DatabaseException(StringHelper.DATABASE_EXEPTION_CREATE_PARAMERTER
					+ StringHelper.DB_CREATE_DATABASE + "=" + properties.getProperty(StringHelper.DB_CREATE_DATABASE));
		}
	}

	public void createTables() throws DatabaseException {
		try (Connection con = DriverManager.getConnection(PropertiesController.getSqlConnection());) {
			System.out.println("connected to " + properties.getProperty(StringHelper.DB_DATABASE_NAME));
			Statement statement = con.createStatement();
			statement.executeUpdate(StringHelper.SQL_CREATE_TABLE_COMPANIES);
			System.out.println("Created table: companies");
			statement.executeUpdate(StringHelper.SQL_CREATE_TABLE_CUSTOMERS);
			System.out.println("Created table: customers");
			statement.executeUpdate(StringHelper.SQL_CREATE_TABLE_CATEGORIES);
			System.out.println("Created table: categories");
			statement.executeUpdate(StringHelper.SQL_CREATE_TABLE_COUPONS);
			System.out.println("Created table: coupons");
			statement.executeUpdate(StringHelper.SQL_CREATE_TABLE_CUSTOMERSVSCOUPONS);
			System.out.println("Created table: couponscustomersvscoupons");
		} catch (SQLException e) {
			throw new DatabaseException(StringHelper.DATABASE_EXEPTION_CREATE + e);
		}

	}
}
