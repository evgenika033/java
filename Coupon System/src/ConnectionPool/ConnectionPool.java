package ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import configuration.PropertiesController;

public class ConnectionPool {
	private Set<java.sql.Connection> connections;
	private static ConnectionPool instance;
	private static final int SIZE = 10;

	private ConnectionPool() throws SQLException {
		init();
		for (int i = 0; i < SIZE; i++) {
			connections.add(DriverManager.getConnection(PropertiesController.getSqlConnection()));
		}
	}

	public static ConnectionPool getInstance() throws SQLException {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;

	}

	private void init() {
		connections = new HashSet<>();
	}

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
		return con;

	}

}
