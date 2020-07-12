package utils;

/**
 * 
 * @author Evgenia
 *
 */
public class StringHelper {

	// configuration
	// ------------------
	public static final String CONFIG_DIRECTORY = "config";
	public static final String CONFIG_FILE = "config.properties";
	public static final String CONFIG = CONFIG_DIRECTORY + "/" + CONFIG_FILE;
	public static final String DB_SERVER = "db.server";
	public static final String DB_DATABASE_DRIVER = "db.database.driver";
	public static final String DB_PORT = "db.port";
	public static final String DB_CREATE_DATABASE = "db.createDatabase";
	public static final String DB_DATABASE_NAME = "db.database.name";
	public static final String DB_DATABASE_MASTER = "db.database.master";
	public static final String DB_USER = "db.user";
	public static final String DB_PASSWORD = "db.password";
	public static final String PROPERTIES_OWNER = "created by Evgenia Ostrovsi";

	// ----------------------------------
	// exception message
	public static final String PROPERTIES_SAVE_EXCEPTION = "save to file exception: ";
	public static final String PROPERTIES_READ_EXCEPTION = "exception read configuration from file: ";
	public static final String PROPERTIES_NOT_READ = "properties file cannot be read. program is stopped";
	public static final String DATABASE_EXEPTION_CREATE_PARAMERTER = "create database parameter is false. see to configuration: ";
	public static final String DATABASE_EXEPTION_CREATE = "create datatbase exception: ";
	public static final String DAO_EXEPTION_CONNECTION = "dao exception, get connection: ";
	// ----------------------------------
	// SQL queries
	// create/ drop database
	public static final String SQL_CREATE_DATABASE = "create database ";
	public static final String SQL_DROP_DATABASE = "drop database ";

	// create tables
//	public static final String SQL_CREATE_TABLE_COMPANIES = "create table companies(companyId int identity(1001,1),companyName text,companyEmail text,companyPassword text,constraint companies_companyId_pk primary key(companyId))";
//	public static final String SQL_CREATE_TABLE_CUSTOMERS = "create table customers(customerId int identity (1001,1),firstName text,lastName text,customerEmail text,customerPassword text,constraint customers_customerId_pk primary key(customerId))";
//	public static final String SQL_CREATE_TABLE_CATEGORIES = "create table categories(categoryId int identity (1001,1),categoryName text,constraint categories_categoryId_pk primary key(categoryId))";
//	public static final String SQL_CREATE_TABLE_COUPONS = "create table coupons(couponId int identity (1001,1),companyId int,categoryId int,title text,description text,startDate date,endDate date,amount int,price float,image text,constraint coupons_couponsId_pk primary key(couponId),constraint coupons_companyId_fk foreign key(companyId) references companies(companyId),constraint coupons_categoryID_fk foreign key(categoryId) references categories(categoryId))";
//	public static final String SQL_CREATE_TABLE_CUSTOMERSVSCOUPONS = "create table customersVSCoupons(customerId int,couponId int,constraint customersVSCoupons_customerId_couponId_pk primary key(customerId,couponId),constraint coupons_customerId_fk foreign key(customerId) references customers(customerId),constraint coupons_couponId_fk foreign key(couponId) references coupons (couponId))";

	public static final String SQL_CREATE_TABLE_COMPANIES = "create table companies(companyId int identity(1001,1),companyName text,companyEmail text,companyPassword text,constraint companies_companyId_pk primary key(companyId))";
	public static final String SQL_CREATE_TABLE_CUSTOMERS = "create table customers(customerId int identity (1001,1),firstName text,lastName text,customerEmail text,customerPassword text,constraint customers_customerId_pk primary key(customerId))";
	public static final String SQL_CREATE_TABLE_CATEGORIES = "create table categories(categoryId int identity (1001,1),categoryName text,constraint categories_categoryId_pk primary key(categoryId))";
	public static final String SQL_CREATE_TABLE_COUPONS = "create table coupons(couponId int identity (1001,1),companyId int,categoryId int,title text,description text,startDate date,endDate date,amount int,price float,image text,constraint coupons_couponsId_pk primary key(couponId))";
	public static final String SQL_CREATE_TABLE_CUSTOMERSVSCOUPONS = "create table customersVSCoupons(customerId int,couponId int,constraint customersVSCoupons_customerId_couponId_pk primary key(customerId,couponId))";

	// replace _TABLE_NAME_ with table name( companies)
	// replace _PARAMETERS_ with
	// parameters(companyName=?,companyEmail=?,companyPassword=? where companyId=?)
	public static final String SQL_UPDATE = "update _TABLE_NAME_ set _UPDATE_PARAMETERS_";

	// replace _TABLE_NAME_ with table name( companies)
	// replace _ADD_PARAMETERS_ with table parameters(?,?,?)
	public static final String SQL_ADD = "insert into _TABLE_NAME_ values(_ADD_PARAMETERS_)";

	// replace _TABLE_NAME_ with table name( companies)
	// replace _GET_PARAMETERS_ with table parameters(companyId=?)
	public static final String SQL_GET = "select * from _TABLE_NAME_ where _GET_PARAMETERS_";

	// replace _TABLE_NAME_ with table name( companies)
	public static final String sql_GET_ALL = "select * from _TABLE_NAME_";

	// replace _TABLE_NAME_ with table name( companies)
	// replace _DELETE_PARAMETERS_ with table parameters(companyId=?)
	public static final String SQL_DELETE = "delete from _TABLE_NAME_ where _DELETE_PARAMETERS_";

}
