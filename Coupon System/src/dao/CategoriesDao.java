package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Category;
import connectionPool.ConnectionPool;
import dao.interfaces.IDaoSimple;
import exceptions.DaoException;
import utils.StringHelper;

public class CategoriesDao implements IDaoSimple {

	private Connection connection;

	/**
	 * get connection
	 * 
	 * @throws DaoException
	 */
	private void getConnection() throws DaoException {
		try {
			connection = ConnectionPool.getInstance().getConnection();
		} catch (SQLException e) {
			throw new DaoException(StringHelper.EXEPTION_DAO_CONNECTION, e);
		}
	}

	/**
	 * return connection
	 * 
	 * @throws DaoException
	 */
	private void returnConnection() throws DaoException {
		try {
			ConnectionPool.getInstance().returnConnection(connection);
			connection = null;
		} catch (SQLException e) {
			throw new DaoException(StringHelper.EXEPTION_DAO_CONNECTION, e);
		}
	}

	@Override
	public String get(int id) throws DaoException {
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CATEGORIES)
				.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER, StringHelper.GET_PARAMETERS_CATEGORY_BY_ID);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String categoryName = resultSet.getString("categoryName");
				returnConnection();
				return categoryName;
			}
			returnConnection();
			return Category.DEFAULT.toString();
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}

	}

	@Override
	public int get(String name) throws DaoException {
		String sql = StringHelper.SQL_GET.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CATEGORIES);
		sql = sql.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER, StringHelper.GET_PARAMETERS_CATEGORY_BY_NAME);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, name);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int categoryId = resultSet.getInt("categoryId");
				returnConnection();
				return categoryId;
			}
			returnConnection();
			return Category.getCategoryID(Category.DEFAULT);
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
	}

	@Override
	public void add(Category category) throws DaoException {
		String sql = StringHelper.SQL_ADD;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CATEGORIES)
				.replaceAll(StringHelper.PARAMETERS_ADD_PLACE_HOLDER, StringHelper.ADD_PARAMETERS_CATEGORY);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, category.name().toUpperCase());
			preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("category inserted: " + category.name());
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_INSERT, e);
		}
	}

}
