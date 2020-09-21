package test;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import configuration.DatabaseCreator;
import configuration.PropertiesController;
import connectionPool.ConnectionPool;
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
			// read all parameters
			checkParameters();
			// start job
			job();
			// execute tests
			customerFasadeTest();
			companyFacadeTest();
			adminFacadeTest();
			// stop job
			job.stop();
			// job stop monitoring
			jobStopMonitor();
			// close all connections
			ConnectionPool.getInstance().closeAllConnections();
			System.out.println("--------end program---------");

		} else {
			System.out.println(StringHelper.EXCEPTION_PROPERTIES_NOT_READ);
		}
	}

	/**
	 * monitoring for stop job
	 */
	private static void jobStopMonitor() {
		int counter = 0;
		// read status from job
		while (!job.isQuitReaded()) {
			try {
				Thread.sleep(10000);
				counter++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("wait for stopping job: 10/" + counter);
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

	/**
	 * read parameters, create database if need, job interval, connection
	 * properties...
	 * 
	 * @throws DaoException
	 * @throws PropertiesExceptions
	 */
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

	/**
	 * start daily job
	 */
	private static void job() {
		job = new CouponExpirationDailyJob(new CouponsDao(), false);
		Thread thread = new Thread(job);
		thread.start();
	}

	/**
	 * test login manager
	 * 
	 * @param email
	 * @param password
	 * @param clientType
	 * @return ClientFasade
	 * @throws DaoException
	 */
	private static ClientFasade loginManagerTest(String email, String password, ClientType clientType)
			throws DaoException {
		return LoginManager.getInstance().login(email, password, clientType);
	}

	/**
	 * test admin facade
	 * 
	 * @throws DaoException
	 */
	private static void adminFacadeTest() throws DaoException {
		// login
		System.out.println("admin test start:");
		String email = "admin@admin.com";
		String password = "admin";
		// login with admin
		AdminFasade adminFasade = (AdminFasade) loginManagerTest(email, password, ClientType.Administrator);
		if (adminFasade != null) {
			// after login success start tests
			// Company test
			System.out.println("login is successfull. email: " + email + " password: " + password);
			// addCompany
			System.out.println("add  company:");
			Company company = new Company("Versis", "Versis@com", "12345");
			adminFasade.addCompany(company);
			// get company
			System.out.println("get company");
			company = adminFasade.getCompany(company.getName());
			if (company != null) {
				// update company
				String name = company.getName() + "1";
				company.setName(name);
				System.out.println("update  company name: " + name);
				adminFasade.updateCompany(company);
				// delete company
				System.out.println("delete  company:");
				adminFasade.deleteCompany(company.getID());

				// getCompanies
				System.out.println("get companies: ");
				List<Company> companies = adminFasade.getCompanies();
				// System.out.println(adminFasade.getCompanies());
				System.out.println(companies);
			}
			// ----------------------------------------------

			// customer test
			// add customer
			System.out.println("add customer: ");
			Customer customer = new Customer("Moti", "Ostrovski", "111@222", "123456");
			adminFasade.addCustomer(customer);
			// get customer
			System.out.println("get customer");
			customer = adminFasade.getCustomer(customer.getEmail());

			// update customer
			if (customer != null) {

				String customerEmail = customer.getEmail() + "1";
				customer.setEmail(customerEmail);
				System.out.println("update  customer customerEmail: " + customerEmail);
				adminFasade.updateCustomer(customer);

				// delete customer
				System.out.println("delete customer:");
				adminFasade.deleteCustomer(customer.getID());
				// get customers
				System.out.println("get customers: ");
				List<Customer> customers = adminFasade.getCustomers();
				System.out.println("customers count: " + customers.size());
				System.out.println(customers);

			}

		} else {
			// login failed
			System.out.println("----login is failed. email: " + email + " password: " + password);
		}
		System.out.println("----admin test end------\r\n\r\n");

	}

	/**
	 * test company facade
	 * 
	 * @throws DaoException
	 */
	private static void companyFacadeTest() throws DaoException {
		// login
		System.out.println("company test start:");
		String email = "Elledi@company.com";
		String password = "123456";
		System.out.println("login with company:");
		CompanyFacade companyFacade = (CompanyFacade) loginManagerTest(email, password, ClientType.Company);
		if (companyFacade != null) {
			// after login success start tests
			System.out.println("login is successfull. email: " + email + " password: " + password);
			System.out.println("company id: " + companyFacade.getCompanyID());
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
				// get company coupons by category
				System.out.println("get company coupons by category:");
				coupons = companyFacade.getCompanyCoupons(Category.GAMES);
				System.out.println("coupons size: " + coupons.size() + ". " + "category" + coupons);
				// get company coupons by maxPrice
				System.out.println("get company coupons by maxPrice:");
				coupons = companyFacade.getCompanyCoupons(19);
				System.out.println("coupons size: " + coupons.size() + ". " + "maxPrice" + coupons);
				// get company details
				System.out.println("get company details:");
				Company company = companyFacade.getCompanyDetails();
				System.out.println(company);

			}
		} else {
			// login failed
			System.out.println("----login is failed. email: " + email + " password: " + password);
		}
		System.out.println("----company test end------\r\n\r\n");
	}

	/**
	 * test customer facade
	 * 
	 * @throws DaoException
	 */
	private static void customerFasadeTest() throws DaoException {
		// login
		System.out.println("customer test start:");
		String email = "alexander.amsler@customer.com";
		String password = "123456";
		System.out.println("login with customer:");
		CustomerFasade customerFasade = (CustomerFasade) loginManagerTest(email, password, ClientType.Customer);
		if (customerFasade != null) {
			// after login success start tests
			System.out.println("login is successfull. email: " + email + " password: " + password);
			System.out.println("customer id: " + customerFasade.getCustomerID());
			System.out.println("get all customer coupons:");
			System.out.println(customerFasade.getCustomerCoupons());
			System.out.println("get customer coupons by category:");
			System.out.println(customerFasade.getCustomerCoupons(Category.GAMES));
			System.out.println("get customer coupons by maxPrice:");
			System.out.println(customerFasade.getCustomerCoupons(10));
			System.out.println("purchase customer coupon:");
			// try to purchase coupon. randomize coupon ID: range 1001-1035
			int couponID = ThreadLocalRandom.current().nextInt(1001, 1035 + 1);
			// check if randomized coupon was not purchased earlier,else randomize next
			// get existing list of coupons ids
			List<Integer> listIDs = customerFasade.getCouponsID();
			int[] ids = getIDsArray(listIDs);
			System.out.println("list of coupons id: " + Arrays.toString(ids));
			while (!customerFasade.couponCanBePurchase(couponID)) {
				System.out.println("coupon was purchased earlier by customer: " + couponID);
				int randomIndex = ThreadLocalRandom.current().nextInt(0, ids.length);
				couponID = ids[randomIndex];
				System.out.println("check if this coupon can be purchase: " + couponID);
			}
			System.out.println("true. coupon id: " + couponID);
			// current coupon id can be purchase
			customerFasade.purchaseCoupon(couponID);
			System.out.println("get customer details:");
			System.out.println(customerFasade.getCustomerDetails());
		} else {
			// login failed
			System.out.println("----login is failed. email: " + email + " password: " + password);
		}
		System.out.println("----customer test end------\r\n\r\n");

	}

	/**
	 * convert List of ids to array
	 * 
	 * @param listIDs
	 * @return array of id
	 */
	private static int[] getIDsArray(List<Integer> listIDs) {
		int[] ids = new int[listIDs.size()];
		int counter = 0;
		for (int val : listIDs) {
			ids[counter++] = val;
		}
		return ids;
	}

}
