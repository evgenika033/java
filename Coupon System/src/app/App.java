package app;

import java.io.File;

import configuration.DatabaseCreator;
import configuration.PropertiesController;
import exceptions.DatabaseException;
import exceptions.PropertiesExceptions;
import utils.StringHelper;

public class App {

	public static void main(String[] args) {
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

		// read properties from file
		if (PropertiesController.PROPERTIES_LOAD_SUCCESSFULLY) {
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
			System.out.println("end");
		} else {
			System.out.println(StringHelper.PROPERTIES_NOT_READ);
		}

	}
}
