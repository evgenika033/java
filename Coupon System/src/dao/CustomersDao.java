package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Customer;
import connectionPool.ConnectionPool;
import dao.interfaces.ICustomersDao;
import exceptions.DaoException;
import utils.StringHelper;

public class CustomersDao implements ICustomersDao<Customer> {

	private Connection connection;
	private final String table = "customers";
	private final String UPDATE_PARAMETERS = "firstName=?,lastName=?,customerEmail=?,customerPassword=? where customerId=?";
	private final String ADD_PARAMETERS = "?,?,?,?";
	private final String GET_PARAMETERS = "customerId=?";
	private final String DELETE_PARAMETERS = "customerId=?";

	private void getConnection() throws DaoException {
		try {
			connection = ConnectionPool.getInstance().getConnection();
		} catch (SQLException e) {
			throw new DaoException(StringHelper.DAO_EXEPTION_CONNECTION, e);
		}
	}

	private void returnConnection() throws DaoException {
		try {
			ConnectionPool.getInstance().returnConnection(connection);
			connection = null;
		} catch (SQLException e) {
			throw new DaoException(StringHelper.DAO_EXEPTION_CONNECTION, e);
		}
	}

	@Override
	public void add(Customer addObject) throws DaoException {
		String sql = StringHelper.SQL_ADD;// "insert into _TABLE_NAME_ values(_ADD_PARAMETERS_)";
		// replace place_holders
		sql = sql.replaceAll("_TABLE_NAME_", table).replaceAll("_ADD_PARAMETERS_", ADD_PARAMETERS);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, addObject.getFirstName());
			preparedStatement.setString(2, addObject.getLastName());
			preparedStatement.setString(3, addObject.getEmail());
			preparedStatement.setString(4, addObject.getPassword());
			preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("inserted");
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException("insert exception: ", e);
		}

	}

	@Override
	public void update(Customer updateObject) throws DaoException {
		String sql = StringHelper.SQL_UPDATE;
		sql = sql.replaceAll("_TABLE_NAME_", table).replaceAll("_UPDATE_PARAMETERS_", UPDATE_PARAMETERS);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, updateObject.getFirstName());
			preparedStatement.setString(2, updateObject.getLastName());
			preparedStatement.setString(3, updateObject.getEmail());
			preparedStatement.setString(4, updateObject.getPassword());
			preparedStatement.setInt(5, updateObject.getID());
			int result = preparedStatement.executeUpdate();
			returnConnection();
			if (result > 0) {
				System.out.println("updated true");
			} else {
				System.out.println("updated false");
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException("updated exception: ", e);
		}

	}

	@Override
	public Customer get(int id) throws DaoException {
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll("_TABLE_NAME_", table).replaceAll("_GET_PARAMETERS_", GET_PARAMETERS);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int ID = resultSet.getInt("customerId");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String email = resultSet.getString("customerEmail");
				String password = resultSet.getString("customerPassword");
				returnConnection();
				return new Customer(ID, firstName, lastName, email, password);
			}
			returnConnection();
			return null;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException("get exception: ", e);
		}

	}

	@Override
	public List<Customer> getAll() throws DaoException {
		List<Customer> list = new ArrayList<>();
		String sql = StringHelper.sql_GET_ALL;
		sql = sql.replaceAll("_TABLE_NAME_", table);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// preparedStatement.setString(1, table);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int ID = resultSet.getInt("customerId");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String email = resultSet.getString("customerEmail");
				String password = resultSet.getString("customerPassword");
				list.add(new Customer(ID, firstName, lastName, email, password));
			}
			returnConnection();
			return list;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException("getAll exception: ", e);
		}

	}

	@Override
	public boolean isCustomerExist(String email, String password) throws DaoException {
		String sql = "select COUNT (*)from _TABLE_NAME_ where customerEmail like ? and customerPassword like ?";
		sql = sql.replaceAll("_TABLE_NAME_", table);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int counter = resultSet.getInt(1);
				returnConnection();
				return counter > 0;
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException("getAll exception: ", e);

		}
		returnConnection();
		return false;

	}

	@Override
	public void delete(int objectID) throws DaoException {
		String sql = StringHelper.SQL_DELETE;
		sql = sql.replaceAll("_TABLE_NAME_", table).replaceAll("_DELETE_PARAMETERS_", DELETE_PARAMETERS);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, objectID);
			int result = preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("deleted: " + result);
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException("delete exception: ", e);
		}

	}

}
