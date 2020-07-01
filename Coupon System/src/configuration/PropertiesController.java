package configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import exceptions.PropertiesExceptions;
import utils.StringHelper;

/**
 * 
 * @author Evgenia use this class for work with properties from config file
 */
public class PropertiesController {

	private static Properties properties;
	// indicator for loading properties
	public static boolean PROPERTIES_LOAD_SUCCESSFULLY = false;

	// static init
	static {
		try {
			readPropertiesFromFile();
		} catch (PropertiesExceptions e) {
			e.printStackTrace();
			PROPERTIES_LOAD_SUCCESSFULLY = false;
		}
	}

	/**
	 * read properties from configuration file
	 * 
	 * @throws PropertiesExceptions
	 */
	private static void readPropertiesFromFile() throws PropertiesExceptions {
		properties = new Properties();
		try (InputStream input = new FileInputStream(StringHelper.CONFIG)) {
			properties.load(input);
			PROPERTIES_LOAD_SUCCESSFULLY = true;
		} catch (IOException e) {
			throw new PropertiesExceptions(StringHelper.PROPERTIES_READ_EXCEPTION + StringHelper.CONFIG);
		}
	}

	/**
	 * 
	 * @return SqlConnection with Master database for create new database
	 */
	public static String getSqlConnectionMaster() {
		return "jdbc:" + properties.getProperty(StringHelper.DB_DATABASE_DRIVER) + "://"
				+ properties.getProperty(StringHelper.DB_SERVER) + ":" + properties.getProperty(StringHelper.DB_PORT)
				+ ";databaseName=" + properties.getProperty(StringHelper.DB_DATABASE_MASTER) + ";user="
				+ properties.getProperty(StringHelper.DB_USER) + ";password="
				+ properties.getProperty(StringHelper.DB_PASSWORD);
	}

	/**
	 * 
	 * @return SqlConnection
	 */
	public static String getSqlConnection() {
		return "jdbc:" + properties.getProperty(StringHelper.DB_DATABASE_DRIVER) + "://"
				+ properties.getProperty(StringHelper.DB_SERVER) + ":" + properties.getProperty(StringHelper.DB_PORT)
				+ ";databaseName=" + properties.getProperty(StringHelper.DB_DATABASE_NAME) + ";user="
				+ properties.getProperty(StringHelper.DB_USER) + ";password="
				+ properties.getProperty(StringHelper.DB_PASSWORD);
	}

	/**
	 * 
	 * @return Properties
	 */
	public static Properties getProperties() {
		return properties;
	}

	/**
	 * save properties to file load properties from file after save
	 * 
	 * @throws PropertiesExceptions
	 */
	public static void write() throws PropertiesExceptions {

		// create configuration file
		try (OutputStream output = new FileOutputStream(StringHelper.CONFIG)) {
			Properties prop = new Properties();
			// set the properties value
			// server name or URL or FQDN(full) name: localhost, server.domain.com,
			prop.setProperty(StringHelper.DB_SERVER, PropertiesHelper.DB_SERVER_VALUE);
			// database driver: sqlserver, derby, oracle
			prop.setProperty(StringHelper.DB_DATABASE_DRIVER, PropertiesHelper.DB_DATABASE_DRIVER_VALUE);
			prop.setProperty(StringHelper.DB_PORT, PropertiesHelper.DB_PORT_VALUE);
			// if need to create new Database change value to: true.
			// after creating changed to false automatically
			prop.setProperty(StringHelper.DB_CREATE_DATABASE, PropertiesHelper.DB_CREATE_DATABASE_VALUE);
			prop.setProperty(StringHelper.DB_DATABASE_NAME, PropertiesHelper.DB_DATABASE_NAME_VALUE);
			// the property need for create a new database
			// in connection must set any database to connect
			prop.setProperty(StringHelper.DB_DATABASE_MASTER, PropertiesHelper.DB_DATABASE_MASTER_VALUE);
			prop.setProperty(StringHelper.DB_USER, PropertiesHelper.DB_USER_VALUE);
			prop.setProperty(StringHelper.DB_PASSWORD, PropertiesHelper.DB_PASSWORD_VALUE);
			// save properties to file
			prop.store(output, StringHelper.PROPERTIES_OWNER);
			readPropertiesFromFile();
		} catch (IOException io) {
			throw new PropertiesExceptions(StringHelper.PROPERTIES_SAVE_EXCEPTION + io);
		}

	}

}
