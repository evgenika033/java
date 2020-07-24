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
	public String get(int id) throws DaoException {
		String sql = "select categoryName from categories where categoryId=?";
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
			throw new DaoException("get exception: ", e);
		}

	}

	@Override
	public int get(String name) throws DaoException {
		String sql = "select categoryId from categories where categoryName like ?";
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
			throw new DaoException("get exception: ", e);
		}
	}

}
