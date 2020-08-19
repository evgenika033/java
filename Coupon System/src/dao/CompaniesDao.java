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

	// get connection
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
	public void add(Company addObject) throws DaoException {
		String sql = StringHelper.SQL_ADD;
		// replace place_holders
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES)
				.replaceAll(StringHelper.PARAMETERS_ADD_PLACE_HOLDER, StringHelper.ADD_PARAMETERS_COMPANIES);
		if (isCompanyValid(addObject.getName(), addObject.getEmail())) {
			System.out.println(sql);
			getConnection();
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
				companyToStatement(preparedStatement, addObject, true);
				preparedStatement.executeUpdate();
				System.out.println("inserted");
				returnConnection();
			} catch (SQLException e) {
				returnConnection();
				throw new DaoException(StringHelper.EXCEPTION_INSERT, e);
			}
		}

	}

	// convert company to statement
	private void companyToStatement(PreparedStatement preparedStatement, Company company, boolean isAdd)
			throws DaoException {
		try {
			if (isAdd) {
				// for new company
				preparedStatement.setString(1, company.getName());
				preparedStatement.setString(2, company.getEmail());
				preparedStatement.setString(3, company.getPassword());
			} else {
				// for update company
				preparedStatement.setString(1, company.getName());
				preparedStatement.setString(2, company.getEmail());
				preparedStatement.setString(3, company.getPassword());
				preparedStatement.setInt(4, company.getID());
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_INSERT, e);
		}

	}

	@Override
	public void update(Company updateObject) throws DaoException {
		String sql = StringHelper.SQL_UPDATE;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES)
				.replaceAll(StringHelper.PARAMETERS_UPDATE_PLACE_HOLDER, StringHelper.UPDATE_PARAMETERS_COMPANIES);
		System.out.println(sql);
		Company companyDB = get(updateObject.getID());
		companyCheck(companyDB);
		updateObject.setName(companyDB.getName());
		if (isCompanyValid(updateObject.getEmail(), updateObject.getID())) {
			getConnection();
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
				companyToStatement(preparedStatement, updateObject, false);
				int result = preparedStatement.executeUpdate();
				returnConnection();
				if (result > 0) {
					System.out.println("updated true");
				} else {
					System.out.println("updated false");
				}
			} catch (SQLException e) {
				returnConnection();
				throw new DaoException(StringHelper.EXCEPTION_UPDATE, e);
			}

		}

	}

	private void companyCheck(Company company) throws DaoException {
		if (company == null) {
			throw new DaoException(StringHelper.EXCEPTION_COMPANY_NOT_FOUND);
		}
	}

	@Override
	public Company get(int id) throws DaoException {
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES)
				.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER, StringHelper.GET_PARAMETERS_COMPANIES);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			if (resultSet.next()) {
				return resultToCompany(resultSet);
			}
			return null;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}

	}

	@Override
	public Company get(String companyName) throws DaoException {
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES)
				.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER, StringHelper.GET_PARAMETERS_COMPANIES_BY_NAME);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, companyName);
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			if (resultSet.next()) {
				return resultToCompany(resultSet);
			}
			return null;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
	}

	// set company from resultSet
	private Company resultToCompany(ResultSet resultSet) throws DaoException {
		Company company = null;
		try {
			company = new Company();
			company.setID(resultSet.getInt("companyId"));
			company.setName(resultSet.getString("companyName"));
			company.setEmail(resultSet.getString("companyEmail"));
			company.setPassword(resultSet.getString("companyPassword"));
			return company;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}

	}

	@Override
	public List<Company> getAll() throws DaoException {
		List<Company> list = new ArrayList<>();
		String sql = StringHelper.SQL_GET_ALL;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// preparedStatement.setString(1, table);
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			while (resultSet.next()) {
				list.add(resultToCompany(resultSet));
			}

			return list;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET_ALL, e);
		}

	}

	@Override
	public Company companyLogin(String email, String password) throws DaoException {
		String sql = StringHelper.SQL_GET.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES);
		sql = sql.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER,
				StringHelper.GET_PARAMETERS_COMPANY_BY_EMAIL_AND_PASSWORD);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			if (resultSet.next()) {
				return resultToCompany(resultSet);
			}
			return null;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET_ALL, e);
		}
	}

	@Override
	public void delete(int objectID) throws DaoException {
		String sql = StringHelper.SQL_DELETE;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COMPANIES)
				.replaceAll(StringHelper.PARAMETERS_DELETE_PLACE_HOLDER, StringHelper.DELETE_PARAMETERS_COMPANIES);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, objectID);
			int result = preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("deleted: " + result);
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_DELETE, e);
		}
	}

	@Override
	public boolean isCompanyValid(String companyName, String companyEmail) throws DaoException {
		String sql = StringHelper.SQL_GET_COUNT.replaceAll(StringHelper.TABLE_PLACE_HOLDER,
				StringHelper.TABLE_COMPANIES);
		sql = sql.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER,
				StringHelper.GET_PARAMETERS_COMPANY_BY_NAME_OR_EMAIL);

		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, companyName);
			preparedStatement.setString(2, companyEmail);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (resultSet.getInt(1) > 0) {
					returnConnection();
					throw new DaoException(StringHelper.EXCEPTION_COMPANY_ADD_ALREADY_EXIST);
				}
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		returnConnection();
		return true;
	}

	@Override
	public boolean isCompanyValid(String companyEmail, int companyID) throws DaoException {
		String sql = StringHelper.SQL_GET_COUNT.replaceAll(StringHelper.TABLE_PLACE_HOLDER,
				StringHelper.TABLE_COMPANIES);
		sql = sql.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER,
				StringHelper.GET_PARAMETERS_COMPANY_BY_EMAIL_AND_ID);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setString(1, companyEmail);
			preparedStatement.setInt(2, companyID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (resultSet.getInt(1) > 0) {
					returnConnection();
					throw new DaoException(StringHelper.EXCEPTION_COMPANY_UPDATE_ALREADY_EXIST);
				}
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		returnConnection();
		return true;

	}

}
