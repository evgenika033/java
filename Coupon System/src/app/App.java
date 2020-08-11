package app;

public class App {

//	public static void main(String[] args) throws DaoException, PropertiesExceptions {
//		// start app
//		init();
//		// read properties from file
//		if (PropertiesController.PROPERTIES_LOAD_SUCCESSFULLY) {
//			checkParameters();
//			// job();
//			// check function
//			// companiesTest();
//			// customersTest();
//			// couponsTest();
//			// customerFasadeTest();
//			// companyFacadeTest();
//			// adminFacadeTest();
//			// loginManagerTest();
//			System.out.println("end");
//		} else {
//			System.out.println(StringHelper.EXCEPTION_PROPERTIES_NOT_READ);
//		}
//
//	}

//	// start app
//	private static void init() {
//		// configuration file: if not exist=> create it.
//		// on create configuration can be throw exception because file is not found
//		// check if folder exist
//		File file = new File(StringHelper.CONFIG_DIRECTORY);
//		if (!file.exists()) {
//			// folder not exist, create folder
//			file.mkdir();
//		}
//		file = new File(StringHelper.CONFIG);
//		if (!file.exists()) {
//			try {
//				PropertiesController.write();
//				System.out.println("create configuration file successfully");
//			} catch (PropertiesExceptions e1) {
//				e1.printStackTrace();
//			}
//		}
//	}
//
//	private static void checkParameters() throws DaoException, PropertiesExceptions {
//		// check property: create database. if true => go to create database
//		if (Boolean.valueOf(PropertiesController.getProperties().getProperty(StringHelper.DB_CREATE_DATABASE))) {
//			System.out.println("start create new database");
//			System.out.println("master connection: " + PropertiesController.getSqlConnectionMaster());
//			DatabaseCreator databaseCreator = new DatabaseCreator();
//
//			try {
//				// steps to create database:
//				// 1. check if database exist
//				// 2. drop exist database
//				// 3. create new database
//				databaseCreator.createDatabase();
//				System.out.println("database create successfully");
//				// 4. create tables
//				databaseCreator.createTables();
//				System.out.println("all tables are created");
//				System.out.println("save property: " + StringHelper.DB_CREATE_DATABASE + "=false");
//				PropertiesController.write(StringHelper.DB_CREATE_DATABASE, Boolean.toString(false));
//			} catch (DatabaseException e) {
//				System.out.println(e);
//			}
//		}
//		System.out.println("read configuration file successfully");
//		System.out.println("regular connection string: " + PropertiesController.getSqlConnection());
//		System.out.println("===================================");
//	}
//
//	private static void job() {
//		CouponExpirationDailyJob job = new CouponExpirationDailyJob(new CouponsDao(), false);
//		job.run();
//	}
//
//	private static void loginManagerTest() throws DaoException {
//		AdminFasade adminFasade = (AdminFasade) LoginManager.getInstance().login("admin@admin.com", "admin",
//				ClientType.Administrator);
//		System.out.println("AdminFasad login success: " + adminFasade);
//		adminFasade = (AdminFasade) LoginManager.getInstance().login("$dmin@admin.com", "admin",
//				ClientType.Administrator);
//		System.out.println("AdminFasad  login failed: " + adminFasade);
//		System.out.println("---------------");
//
//		CustomerFasade customerFasade = (CustomerFasade) LoginManager.getInstance().login("w@124", "xx22",
//				ClientType.Customer);
//
//		System.out.println("customerFasad login success: " + customerFasade);
//		customerFasade = (CustomerFasade) LoginManager.getInstance().login("w@120", "xx22", ClientType.Customer);
//
//		System.out.println("customerFasad login failed: " + customerFasade);
//		System.out.println("---------------");
//
//		CompanyFacade companyFasade = (CompanyFacade) LoginManager.getInstance().login("4321", "999",
//				ClientType.Company);
//
//		System.out.println("companyFasad login success: " + companyFasade);
//		companyFasade = (CompanyFacade) LoginManager.getInstance().login("w@120", "xx212", ClientType.Customer);
//
//		System.out.println("companyFasad login failed: " + companyFasade);
//
//	}
//
//	private static void adminFacadeTest() throws DaoException {
//		AdminFasade adminFasade = new AdminFasade();
//		System.out.println("login success: " + adminFasade.login("admin@admin.com", "admin"));
//		System.out.println("login failed: " + adminFasade.login("admin@admin.com", "wdmin"));
//		// System.out.println("try add company");
//		// adminFasade.addCompany(new Company(1001, "ibm122", "email122@ibm.com",
//		// "ibm11"));
//		System.out.println("try update company");
//		adminFasade.updateCompany(new Company(1001, "ibm12", "4321", "ibm14"));
//		// System.out.println("get all companies " + adminFasade.getCompanies());
//
//	}
//
//	private static void companyFacadeTest() throws DaoException {
//		CompanyFacade companyFasade = new CompanyFacade(1001);
//		System.out.println("Is company exist " + companyFasade.login("4321", "999"));
//		// companyFasade.getCompanyCoupons();
//		// System.out.println(companyFasade.getCompanyCoupons());
//		// Coupon coupon = new Coupon(1001, Category.ACCESSORIES, "bbb", "222", null,
//		// null, 15, 20, null);
//		// companyFasade.addCoupon(coupon);
//		// System.out.println(" Add coupon");
//		System.out
//				.println("Get coupons of company by categories " + companyFasade.getCompanyCoupons(Category.ELECTRIC));
//		System.out.println("Get coupons of company by price " + companyFasade.getCompanyCoupons(10));
//		System.out.println("get company details " + companyFasade.getCompanyDetails());
//	}
//
//	private static void customerFasadeTest() throws DaoException {
//
//		CustomerFasade customerFasade = new CustomerFasade(1005);
//		System.out.println("Is customer exist " + customerFasade.login("w@124", "xx22"));
//		// System.out.println(customerFasade.getCustomerCoupons());
//		System.out.println("get customer coupons by customerID and category:\r\n"
//				+ customerFasade.getCustomerCoupons(Category.ELECTRIC));
//		System.out.println(
//				"get customer coupons by customerID and maxPrice:\r\n" + customerFasade.getCustomerCoupons(10));
//		System.out.println("get customer details by customerID " + customerFasade.getCustomerDetails());
//	}

//	// test companies:
//	private static void companiesTest() throws DaoException {
//		CompaniesDao companiesDao = new CompaniesDao();
//		// companiesDao.add(new Company("addCompany", "addCompany@addCompany",
//		// "addCompany"));
//		companiesDao.delete(1005);
//		companiesDao.update(new Company(1001, "first1", "email1@email.com", "password1"));
//		Company company = companiesDao.get(1001);
//		System.out.println("get: " + company);
//		System.out.println("get all: " + companiesDao.getAll());
//		System.out.println("is company exist: " + companiesDao.isCompanyExist("001", "999"));
//	}

	// test coupons:
//	private static void couponsTest() throws DaoException {
//		CouponsDao couponsDao = new CouponsDao();
//		// Coupon coupon = new Coupon(1001, Category.BEAUTY, "title2", "desc2",
//		// Date.valueOf("2020-07-12"),
//		// Date.valueOf("2020-07-12"), 10, 10, null);
//		Coupon coupon = new Coupon(1002, 1001, Category.ELECTRIC, "titleNew", "descNew", null, null, 10, 10, null);
//		System.out.println(coupon);
//		// couponsDao.add(coupon);
//		couponsDao.update(coupon);
//		System.out.println("get all coupons: " + couponsDao.getAll());
//		couponsDao.delete(1001);
//		System.out.println("get coupon: " + couponsDao.get(1002));
//		// System.out.println("add customerVScoupon: ");
//		// couponsDao.addCouponPurchase(1003, 1003);
//		couponsDao.deleteCouponPurchase(1003, 1002);
//
//	}

//	// test customers:
//	private static void customersTest() throws DaoException {
//		CustomersDao customersDao = new CustomersDao();
//		// customersDao.add(new Customer("Jeka", "Ostrovski", "w@123", "xx23"));
//		customersDao.update(new Customer(1002, "Jeka", "Ostrovski", "w@123.com", "xx24"));
//		System.out.println("get: " + customersDao.get(1010));
//		System.out.println("getAll: " + customersDao.getAll());
//		customersDao.delete(1002);
//		System.out.println("is customers exist: " + customersDao.isCustomerExist("w@123", "xx23"));
//	}

}
