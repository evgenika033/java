package connectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import configuration.PropertiesController;
import exceptions.DatabaseException;
import utils.StringHelper;

public class ConnectionPool {
	private Set<java.sql.Connection> connections;
	private static ConnectionPool instance;
	private static int poolSize = 0;

	/**
	 * 
	 * @throws SQLException
	 */
	private ConnectionPool() throws SQLException {
		init();
		for (int i = 0; i < poolSize; i++) {
			connections.add(DriverManager.getConnection(PropertiesController.getSqlConnection()));
		}
		System.out.println("connectionPool created with " + connections.size() + " connections ");
	}

	/**
	 * 
	 * @return ConnectionPool
	 * @throws SQLException
	 */
	public static ConnectionPool getInstance() throws SQLException {
		if (instance == null) {
			instance = new ConnectionPool();
		}

		return instance;

	}

	/**
	 * init
	 */
	private void init() {
		connections = new HashSet<>();

		poolSize = Integer.valueOf(PropertiesController.getProperties().getProperty(StringHelper.CONNECTION_POOL));

	}

	/**
	 * 
	 * @return Connection
	 */
	public synchronized Connection getConnection() {
		while (connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Iterator<Connection> it = connections.iterator();
		java.sql.Connection con = it.next();
		it.remove();
//		System.out.println("get connection. size " + connections.size());
		return con;

	}

	/**
	 * 
	 * @param connection
	 */
	public synchronized void returnConnection(Connection connection) {
		connections.add(connection);
		notifyAll();
//		System.out.println("return connection. size " + connections.size());
	}

	/**
	 * close all opened connection. for stop application or maintenance
	 * 
	 * @throws DatabaseException
	 */
	public synchronized void closeAllConnections() throws DatabaseException {
		while (connections.size() < poolSize) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new DatabaseException(StringHelper.EXCEPTION_CONNECTION_CLOSE_WAIT, e);
			}
		}
		Iterator<Connection> iterator = connections.iterator();
		while (iterator.hasNext()) {
			try {
				iterator.next().close();
				iterator.remove();
			} catch (SQLException e) {
				throw new DatabaseException(StringHelper.EXCEPTION_CONNECTION_CLOSE, e);
			}
		}
		if (connections.size() == 0) {
			System.out.println(StringHelper.CONNECTION_POOL_CLOSED);
		} else {
			System.out.println(StringHelper.CONNECTION_POOL_CLOSE_FAILED);
		}
	}

}
