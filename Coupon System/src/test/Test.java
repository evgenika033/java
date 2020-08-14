package test;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import beans.Category;
import beans.Company;
import beans.Coupon;
import configuration.DatabaseCreator;
import configuration.PropertiesController;
import dao.CouponsDao;
import exceptions.DaoException;
import exceptions.DatabaseException;
import exceptions.PropertiesExceptions;
import fasade.AdminFasade;
import fasade.ClientFasade;
import fasade.CompanyFacade;
import fasade.CustomerFasade;
import job.CouponExpirationDailyJob;
import loginManager.ClientType;
import loginManager.LoginManager;
import utils.StringHelper;

public class Test {
	private static CouponExpirationDailyJob job;

	public static void testAll() throws DaoException, PropertiesExceptions, DatabaseException, SQLException {
		System.out.println("This is a test method");
		// start app
		init();
		// read properties from file
		if (PropertiesController.PROPERTIES_LOAD_SUCCESSFULLY) {
			checkParameters();
			// job();
			customerFasadeTest();
			companyFacadeTest();
			// companiesTest();
			// loginManagerTest();
			// check function
			// customersTest();
			// couponsTest();

			// adminFacadeTest();
			// job.stop();
			// ConnectionPool.getInstance().closeAllConnections();
			System.out.println("--------end program---------");
		} else {
			System.out.println(StringHelper.EXCEPTION_PROPERTIES_NOT_READ);
		}
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

	private static void checkParameters() throws DaoException, PropertiesExceptions {
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
				System.out.println("save property: " + StringHelper.DB_CREATE_DATABASE + "=false");
				PropertiesController.write(StringHelper.DB_CREATE_DATABASE, Boolean.toString(false));
			} catch (DatabaseException e) {
				System.out.println(e);
			}
		}
		System.out.println("read configuration file successfully");
		System.out.println("regular connection string: " + PropertiesController.getSqlConnection());
		System.out.println("===================================");
	}

	private static void job() {
		job = new CouponExpirationDailyJob(new CouponsDao(), false);
		// job.run();
		Thread thread = new Thread(job);
		thread.start();
	}

	private static ClientFasade loginManagerTest(String email, String password, ClientType clientType)
			throws DaoException {
		return LoginManager.getInstance().login(email, password, clientType);
	}

	private static void adminFacadeTest() throws DaoException {
		AdminFasade adminFasade = new AdminFasade();
		System.out.println("login success: " + adminFasade.login("admin@admin.com", "admin"));
		System.out.println("login failed: " + adminFasade.login("admin@admin.com", "wdmin"));
		// System.out.println("try add company");
		// adminFasade.addCompany(new Company(1001, "ibm122", "email122@ibm.com",
		// "ibm11"));
		System.out.println("try update company");
		adminFasade.updateCompany(new Company(1001, "ibm12", "4321", "ibm14"));
		// System.out.println("get all companies " + adminFasade.getCompanies());
		//

	}

	private static void companyFacadeTest() throws DaoException {

		System.out.println("company test start:");
//		String email = "Angel.Bakeries@company.com";
		String email = "Elledi@company.com";
		String password = "123456";

		System.out.println("login with company:");
		CompanyFacade companyFacade = (CompanyFacade) loginManagerTest(email, password, ClientType.Company);
		if (companyFacade != null) {
			System.out.println("login is successfull. email: " + email + " password: " + password);
			// add coupon
			System.out.println("add coupon company:");
			Coupon coupon = new Coupon(companyFacade.getCompanyID(), Category.AUTOMOTIVE, "fasadTest", "desk", null,
					null, 4, 20.2, null);
			companyFacade.addCoupon(coupon);
			// update coupon
			coupon = companyFacade.getCompanyCoupon(coupon.getTitle());
			if (coupon != null) {
				System.out.println("update coupon company:");
				coupon.setPrice(50);
				companyFacade.updateCoupon(coupon);
				// delete coupon
				System.out.println("delete coupon company:");
				companyFacade.deleteCoupon(coupon.getID());
				// get all company coupons
				System.out.println("get all company coupons:");
				List<Coupon> coupons = companyFacade.getCompanyCoupons();
				System.out.println("coupons size: " + coupons.size() + ". " + coupons);
//				// get company coupons by category
				System.out.println("get company coupons by category:");
				coupons = companyFacade.getCompanyCoupons(Category.GAMES);
				System.out.println("coupons size: " + coupons.size() + ". " + "category" + coupons);

//				// get company coupons by maxPrice
//				System.out.println("get company coupons by maxPrice:");
//				System.out.println(companyFacade.getCompanyCoupons(10));
//				// get company details
//				System.out.println("get company details:");
//				System.out.println(companyFacade.getCompanyDetails());

			}

//			
//			
//			
		} else {
			System.out.println("----login is failed. email: " + email + " password: " + password);
		}
		System.out.println("----company test end------\r\n\r\n");

//		CompanyFacade companyFasade = new CompanyFacade(1001);
//		System.out.println("Is company exist " + companyFasade.login("4321", "999"));
		// companyFasade.getCompanyCoupons();
		// System.out.println(companyFasade.getCompanyCoupons());
		// Coupon coupon = new Coupon(1001, Category.ACCESSORIES, "bbb", "222", null,
		// null, 15, 20, null);
		// companyFasade.addCoupon(coupon);
		// System.out.println(" Add coupon");
//		System.out
//				.println("Get coupons of company by categories " + companyFasade.getCompanyCoupons(Category.ELECTRIC));
//		System.out.println("Get coupons of company by price " + companyFasade.getCompanyCoupons(10));
//		System.out.println("get company details " + companyFasade.getCompanyDetails());

	}

	private static void customerFasadeTest() throws DaoException {
		System.out.println("customer test start:");
		String email = "alexander.amsler@customer.com";
		String password = "123456";
		System.out.println("login with customer:");
		CustomerFasade customerFasade = (CustomerFasade) loginManagerTest(email, password, ClientType.Customer);
		if (customerFasade != null) {
			System.out.println("login is successfull. email: " + email + " password: " + password);
			System.out.println("get all customer coupons:");
			System.out.println(customerFasade.getCustomerCoupons());
			System.out.println("get customer coupons by category:");
			System.out.println(customerFasade.getCustomerCoupons(Category.GAMES));
			System.out.println("get customer coupons by maxPrice:");
			System.out.println(customerFasade.getCustomerCoupons(10));
			System.out.println("get customer details:");
			System.out.println(customerFasade.getCustomerDetails());
		} else {
			System.out.println("----login is failed. email: " + email + " password: " + password);
		}
		System.out.println("----customer test end------\r\n\r\n");

	}

}
