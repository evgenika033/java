package app;

import java.io.File;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import clientFasade.CompanyFacade;
import clientFasade.CustomerFasade;
import configuration.DatabaseCreator;
import configuration.PropertiesController;
import dao.CompaniesDao;
import dao.CouponsDao;
import dao.CustomersDao;
import exceptions.DaoException;
import exceptions.DatabaseException;
import exceptions.PropertiesExceptions;
import utils.StringHelper;

public class App {

	public static void main(String[] args) {
		// start app
		init();
		// read properties from file
		if (PropertiesController.PROPERTIES_LOAD_SUCCESSFULLY) {
			checkParameters();
			// check function
			try {
				// companiesTest();
				// customersTest();
				// couponsTest();
				// customerFasadeTest();
				companyFacadeTest();

			} catch (DaoException e) {

				e.printStackTrace();
			}
			System.out.println("end");
		} else {
			System.out.println(StringHelper.PROPERTIES_NOT_READ);
		}

	}

	private static void companyFacadeTest() throws DaoException {
		CompanyFacade companyFasade = new CompanyFacade(1006);
		System.out.println("Is company exist " + companyFasade.login("4321", "999"));
	}

	private static void customerFasadeTest() throws DaoException {

		CustomerFasade customerFasade = new CustomerFasade(1005);
		System.out.println("Is customer exist " + customerFasade.login("w@124", "xx22"));
		// System.out.println(customerFasade.getCustomerCoupons());
		System.out.println("get customer coupons by customerID and category:\r\n"
				+ customerFasade.getCustomerCoupons(Category.ELECTRIC));
		System.out.println(
				"get customer coupons by customerID and maxPrice:\r\n" + customerFasade.getCustomerCoupons(10));
		System.out.println("get customer details by customerID " + customerFasade.getCustomerDetails());
	}

	// test companies:
	private static void companiesTest() throws DaoException {
		CompaniesDao companiesDao = new CompaniesDao();
		// companiesDao.add(new Company("addCompany", "addCompany@addCompany",
		// "addCompany"));
		companiesDao.delete(1005);
		companiesDao.update(new Company(1001, "first1", "email1@email.com", "password1"));
		Company company = companiesDao.get(1001);
		System.out.println("get: " + company);
		System.out.println("get all: " + companiesDao.getAll());
		System.out.println("is company exist: " + companiesDao.isCompanyExist("001", "999"));
	}

	// test coupons:
	private static void couponsTest() throws DaoException {
		CouponsDao couponsDao = new CouponsDao();
		// Coupon coupon = new Coupon(1001, Category.BEAUTY, "title2", "desc2",
		// Date.valueOf("2020-07-12"),
		// Date.valueOf("2020-07-12"), 10, 10, null);
		Coupon coupon = new Coupon(1002, 1001, Category.ELECTRIC, "titleNew", "descNew", null, null, 10, 10, null);
		System.out.println(coupon);
		// couponsDao.add(coupon);
		couponsDao.update(coupon);
		System.out.println("get all coupons: " + couponsDao.getAll());
		couponsDao.delete(1001);
		System.out.println("get coupon: " + couponsDao.get(1002));
		// System.out.println("add customerVScoupon: ");
		// couponsDao.addCouponPurchase(1003, 1003);
		couponsDao.deleteCouponPurchase(1003, 1002);

	}

	// test customers:
	private static void customersTest() throws DaoException {
		CustomersDao customersDao = new CustomersDao();
		// customersDao.add(new Customer("Jeka", "Ostrovski", "w@123", "xx23"));
		customersDao.update(new Customer(1002, "Jeka", "Ostrovski", "w@123.com", "xx24"));
		System.out.println("get: " + customersDao.get(1010));
		System.out.println("getAll: " + customersDao.getAll());
		customersDao.delete(1002);
		System.out.println("is customers exist: " + customersDao.isCustomerExist("w@123", "xx23"));
	}

	// start app
	private static void init() {
		// configuration file: if not exist=> create it.
		// on create configuration can be throw exception because file is not found
		// check if folder exist
		File file = new File(StringHelper.CONFIG_DIRECTORY);
		if (!file.exists()) {
			// folder not exist, create folder
			file.mkdir();
		}
		file = new File(StringHelper.CONFIG);
		if (!file.exists()) {
			try {
				PropertiesController.write();
				System.out.println("create configuration file successfully");
			} catch (PropertiesExceptions e1) {
				e1.printStackTrace();
			}
		}
	}

	private static void checkParameters() {
		// check property: create database. if true => go to create database
		if (Boolean.valueOf(PropertiesController.getProperties().getProperty(StringHelper.DB_CREATE_DATABASE))) {
			System.out.println("start create new database");
			System.out.println("master connection: " + PropertiesController.getSqlConnectionMaster());
			DatabaseCreator databaseCreator = new DatabaseCreator();

			try {
				// steps to create database:
				// 1. check if database exist
				// 2. drop exist database
				// 3. create new database
				databaseCreator.createDatabase();
				System.out.println("database create successfully");
				// 4. create tables
				databaseCreator.createTables();
				System.out.println("all tables are created");
				PropertiesController.getProperties().setProperty(StringHelper.DB_CREATE_DATABASE,
						Boolean.toString(false));
				System.out.println("set property: " + StringHelper.DB_CREATE_DATABASE + "=false");
				try {
					System.out.println("save properties to config file: " + StringHelper.CONFIG);
					PropertiesController.write();
					System.out.println("===================================");
				} catch (PropertiesExceptions e) {
					e.printStackTrace();
				}
			} catch (DatabaseException e) {
				System.out.println(e);
			}
		}
		System.out.println("read configuration file successfully");
		System.out.println("regular connection string: " + PropertiesController.getSqlConnection());
		System.out.println("===================================");
	}
}
