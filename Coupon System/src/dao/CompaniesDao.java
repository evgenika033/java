package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Company;
import connectionPool.ConnectionPool;
import dao.interfaces.ICompaniesDao;
import exceptions.DaoException;
import utils.StringHelper;

public class CompaniesDao implements ICompaniesDao<Company> {
	private Connection connection;
	private final String table = "companies";
	private final String UPDATE_PARAMETERS = "companyName=?,companyEmail=?,companyPassword=? where companyId=?";
	private final String ADD_PARAMETERS = "?,?,?";
	private final String GET_PARAMETERS = "companyId=?";
	private final String DELETE_PARAMETERS = "companyId=?";

	public CompaniesDao() throws DaoException {
		init();
	}

	private void init() throws DaoException {
		try {
			connection = ConnectionPool.getInstance().getConnection();
		} catch (SQLException e) {
			throw new DaoException(StringHelper.DAO_EXEPTION_CONNECTION, e);
		}
	}

	@Override
	public void add(Company addObject) throws DaoException {
		String sql = StringHelper.SQL_ADD;// "insert into _TABLE_NAME_ values(_ADD_PARAMETERS_)";
		// replace place_holders
		sql = sql.replaceAll("_TABLE_NAME_", table).replaceAll("_ADD_PARAMETERS_", ADD_PARAMETERS);
		System.out.println(sql);
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, addObject.getName());
			preparedStatement.setString(2, addObject.getEmail());
			preparedStatement.setString(3, addObject.getPassword());
			preparedStatement.executeUpdate();
			System.out.println("inserted");
		} catch (SQLException e) {
			throw new DaoException("insert exception: ", e);
		}

	}

	@Override
	public void update(Company updateObject) throws DaoException {
		String sql = StringHelper.SQL_UPDATE;
		sql = sql.replaceAll("_TABLE_NAME_", table).replaceAll("_UPDATE_PARAMETERS_", UPDATE_PARAMETERS);
		System.out.println(sql);
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, updateObject.getName());
			preparedStatement.setString(2, updateObject.getEmail());
			preparedStatement.setString(3, updateObject.getPassword());
			preparedStatement.setInt(4, updateObject.getID());
			int result = preparedStatement.executeUpdate();
			// TODO need to remove
			if (result > 0) {
				System.out.println("updated true");
			} else {
				System.out.println("updated false");
			}
		} catch (SQLException e) {
			throw new DaoException("updated exception: ", e);
		}

	}

	@Override
	public Company get(int id) throws DaoException {
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll("_TABLE_NAME_", table).replaceAll("_GET_PARAMETERS_", GET_PARAMETERS);
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int ID = resultSet.getInt("companyId");
				String name = resultSet.getString("companyName");
				String email = resultSet.getString("companyEmail");
				String password = resultSet.getString("companyPassword");
				return new Company(ID, name, email, password);
			}
			return null;
		} catch (SQLException e) {
			throw new DaoException("get exception: ", e);
		}

	}

	@Override
	public List<Company> getAll() throws DaoException {
		List<Company> list = new ArrayList<>();
		String sql = StringHelper.sql_GET_ALL;
		sql = sql.replaceAll("_TABLE_NAME_", table);
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// preparedStatement.setString(1, table);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int ID = resultSet.getInt("companyId");
				String name = resultSet.getString("companyName");
				String email = resultSet.getString("companyEmail");
				String password = resultSet.getString("companyPassword");
				list.add(new Company(ID, name, email, password));
			}
			return list;
		} catch (SQLException e) {
			throw new DaoException("getAll exception: ", e);
		}

	}

	@Override
	public boolean isCompanyExist(String email, String password) throws DaoException {
		String sql = "select COUNT (*)from _TABLE_NAME_ where companyEmail like ? and companyPassword like ?";
		sql = sql.replaceAll("_TABLE_NAME_", table);
		System.out.println(sql);

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int counter = resultSet.getInt(1);
				return counter > 0;
			}
		} catch (SQLException e) {
			throw new DaoException("getAll exception: ", e);

		}

		return false;
	}

	@Override
	public void delete(int objectID) throws DaoException {
		String sql = StringHelper.SQL_DELETE;
		sql = sql.replaceAll("_TABLE_NAME_", table).replaceAll("_DELETE_PARAMETERS_", DELETE_PARAMETERS);
		System.out.println(sql);
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, objectID);
			System.out.println("deleted: " + preparedStatement.executeUpdate());
		} catch (SQLException e) {
			throw new DaoException("delete exception: ", e);
		}
	}

}
