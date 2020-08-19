package configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import dao.CategoriesDao;
import dao.CompaniesDao;
import dao.CouponsDao;
import dao.CustomersDao;
import exceptions.DaoException;
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

	/**
	 * Constructor
	 */
	public DatabaseCreator() {
		sqlConnection = PropertiesController.getSqlConnectionMaster();
		properties = PropertiesController.getProperties();
	}

	/**
	 * check if database exist
	 * 
	 * @return boolean
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
			throw new DatabaseException(StringHelper.EXEPTION_DATABASE_CREATE + e);
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
			throw new DatabaseException(StringHelper.EXEPTION_DATABASE_CREATE + e);
		}
	}

	/**
	 * create database
	 * 
	 * @return int
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
				throw new DatabaseException(StringHelper.EXEPTION_DATABASE_CREATE + e);
			}
		} else {
			throw new DatabaseException(StringHelper.EXEPTION_DATABASE_CREATE_PARAMERTER
					+ StringHelper.DB_CREATE_DATABASE + "=" + properties.getProperty(StringHelper.DB_CREATE_DATABASE));
		}
	}

	/*
	 * create tables in database
	 * 
	 */
	public void createTables() throws DatabaseException, DaoException {
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
			insertData();
		} catch (SQLException e) {
			throw new DatabaseException(StringHelper.EXEPTION_DATABASE_CREATE + e);
		}

	}

	/*
	 * insert Data
	 */
	private void insertData() throws DatabaseException, DaoException {

		System.out.println("--------start of fill database----------");

		// create companies from file

		try (BufferedReader in = new BufferedReader(new FileReader("config/companies.csv"))) {
			String line;
			CompaniesDao companiesDao = new CompaniesDao();
			while ((line = in.readLine()) != null) {
				String[] companyText = line.split(",");
				companiesDao.add(new Company(companyText[0], companyText[1], companyText[2]));
			}
		} catch (IOException e) {

			throw new DatabaseException(StringHelper.EXEPTION_DATABASE_CREATE + e);
		}
		System.out.println(">> companies filled");

		// create customers from file

		try (BufferedReader in = new BufferedReader(new FileReader("config/customers.csv"))) {
			String line;
			CustomersDao customersDao = new CustomersDao();
			while ((line = in.readLine()) != null) {
				String[] customerText = line.split(",");
				customersDao.add(new Customer(customerText[0], customerText[1], customerText[2], customerText[3]));
			}
		} catch (IOException e) {

			throw new DatabaseException(StringHelper.EXEPTION_DATABASE_CREATE + e);
		}
		System.out.println(">> customers filled");
		// create categories from file
		try (BufferedReader in = new BufferedReader(new FileReader("config/categories.csv"))) {
			String line;
			CategoriesDao categoriesDao = new CategoriesDao();
			while ((line = in.readLine()) != null) {

				categoriesDao.add(Category.valueOf(line));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DatabaseException(StringHelper.EXEPTION_DATABASE_CREATE + e);
		}
		System.out.println(">> categories filled");
		// create coupons from file
		try (BufferedReader in = new BufferedReader(new FileReader("config/coupons.csv"))) {
			String line;
			CouponsDao couponsDao = new CouponsDao();
			while ((line = in.readLine()) != null) {
				String[] couponText = line.split(",");
				int companyID = Integer.valueOf(couponText[0]);
				Category category = Category.categoty(Integer.valueOf(couponText[1]));
				String title = couponText[2];
				Date startDate = Date.valueOf(couponText[3]);
				Date endDate = Date.valueOf(couponText[4]);
				int amount = Integer.valueOf(couponText[5]);
				double price = Double.valueOf(couponText[6]);
				couponsDao.add(new Coupon(companyID, category, title, null, startDate, endDate, amount, price, null));
			}
		} catch (IOException e) {

			throw new DatabaseException(StringHelper.EXEPTION_DATABASE_CREATE + e);
		}

		System.out.println(">> coupons filled");
		// create customersVScoupons from file
		try (BufferedReader in = new BufferedReader(new FileReader("config/customersVScoupons.csv"))) {
			String line;
			CouponsDao couponsDao = new CouponsDao();
			while ((line = in.readLine()) != null) {
				String[] customersVScouponsText = line.split(",");
				couponsDao.addCouponPurchase(Integer.valueOf(customersVScouponsText[0]),
						Integer.valueOf(customersVScouponsText[1]));
			}
		} catch (IOException e) {

			throw new DatabaseException(StringHelper.EXEPTION_DATABASE_CREATE + e);
		}
		System.out.println("--------end of fill database----------");

	}
}
