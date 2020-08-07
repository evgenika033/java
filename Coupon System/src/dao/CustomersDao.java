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

	private void getConnection() throws DaoException {
		try {
			connection = ConnectionPool.getInstance().getConnection();
		} catch (SQLException e) {
			throw new DaoException(StringHelper.EXEPTION_DAO_CONNECTION, e);
		}
	}

	private void returnConnection() throws DaoException {
		try {
			ConnectionPool.getInstance().returnConnection(connection);
			connection = null;
		} catch (SQLException e) {
			throw new DaoException(StringHelper.EXEPTION_DAO_CONNECTION, e);
		}
	}

	@Override
	public void add(Customer addObject) throws DaoException {
		String sql = StringHelper.SQL_ADD;// "insert into _TABLE_NAME_ values(_ADD_PARAMETERS_)";
		// replace place_holders
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTOMER)
				.replaceAll("_ADD_PARAMETERS_", StringHelper.ADD_PARAMETERS_CUSTOMER);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, addObject.getFirstName());
			preparedStatement.setString(2, addObject.getLastName());
			preparedStatement.setString(3, addObject.getEmail());
			preparedStatement.setString(4, addObject.getPassword());
			preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("customer inserted: " + addObject.getFirstName() + " " + addObject.getLastName());
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException("insert exception: ", e);
		}

	}

	@Override
	public void update(Customer updateObject) throws DaoException {
		String sql = StringHelper.SQL_UPDATE;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTOMER)
				.replaceAll("_UPDATE_PARAMETERS_", StringHelper.UPDATE_PARAMETERS_COUSTOMER);
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
				System.out.println(String.format("customer updated %s %s true", updateObject.getFirstName(),
						updateObject.getLastName()));
			} else {
				System.out.println(String.format("customer updated %s %s false", updateObject.getFirstName(),
						updateObject.getLastName()));
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_UPDATE, e);
		}

	}

	@Override
	public Customer get(int id) throws DaoException {
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTOMER)
				.replaceAll("_GET_PARAMETERS_", StringHelper.GET_PARAMETERS_CUSTOMER);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			if (resultSet.next()) {
				return resultToCustomer(resultSet);
			}
			returnConnection();
			return null;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}

	}

	// convert resultSet to Customer
	private Customer resultToCustomer(ResultSet resultSet) throws DaoException {
		Customer customer = new Customer();
		try {
			customer.setID(resultSet.getInt("customerId"));
			customer.setFirstName(resultSet.getString("firstName"));
			customer.setLastName(resultSet.getString("lastName"));
			customer.setEmail(resultSet.getString("customerEmail"));
			customer.setPassword(resultSet.getString("customerPassword"));
			return customer;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
	}

	@Override
	public List<Customer> getAll() throws DaoException {
		List<Customer> list = new ArrayList<>();
		String sql = StringHelper.SQL_GET_ALL;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTOMER);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// preparedStatement.setString(1, table);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				list.add(resultToCustomer(resultSet));
			}
			returnConnection();
			return list;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET_ALL, e);
		}

	}

	@Override
	public Customer customerLogin(String email, String password) throws DaoException {
		String sql = StringHelper.SQL_GET.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTOMER);
		sql = sql.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER,
				StringHelper.GET_PARAMETERS_CUSTOMER_BY_EMAIL_AND_PASSWORD);

		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultToCustomer(resultSet);
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);

		}
		returnConnection();
		return null;

	}

	@Override
	public void delete(int objectID) throws DaoException {
		String sql = StringHelper.SQL_DELETE;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTOMER)
				.replaceAll("_DELETE_PARAMETERS_", StringHelper.DELETE_PARAMETERS_CUSTOMER);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, objectID);
			int result = preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("customer deleted id " + objectID + ": " + result);
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_DELETE, e);
		}

	}

}
