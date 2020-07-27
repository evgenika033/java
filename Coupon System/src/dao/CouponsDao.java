package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Category;
import beans.Coupon;
import connectionPool.ConnectionPool;
import dao.interfaces.ICouponsDao;
import exceptions.DaoException;
import utils.StringHelper;

public class CouponsDao implements ICouponsDao<Coupon> {
	private Connection connection;

	// private final String GET_PARAMETERS_IS_COMPANY_EXIST = "companyId=?,title";

	private CategoriesDao categoriesDao;

	public CouponsDao() {
		init();
	}

	private void init() {
		categoriesDao = new CategoriesDao();
	}

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
	public void add(Coupon addObject) throws DaoException {
		String sql = StringHelper.SQL_ADD;// "insert into _TABLE_NAME_ values(_ADD_PARAMETERS_)";
		// replace place_holders
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON)
				.replaceAll(StringHelper.PARAMETERS_ADD_PLACE_HOLDER, StringHelper.ADD_PARAMETERS_COUPON);

		if (!isCouponExsist(addObject.getCompanyID(), addObject.getTitle())) {
			getConnection();

			// continue add coupon
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
				System.out.println(sql);
				preparedStatement.setInt(1, addObject.getCompanyID());
				int categoryId = categoriesDao.get(addObject.getCategory().name().toUpperCase());
				preparedStatement.setInt(2, categoryId);
				preparedStatement.setString(3, addObject.getTitle());
				preparedStatement.setString(4, addObject.getDescription());
				preparedStatement.setDate(5, addObject.getStartDate());
				preparedStatement.setDate(6, addObject.getEndDate());
				preparedStatement.setInt(7, addObject.getAmount());
				preparedStatement.setDouble(8, addObject.getPrice());
				preparedStatement.setString(9, addObject.getImage());
				preparedStatement.executeUpdate();
				returnConnection();
				System.out.println("inserted");
			} catch (SQLException e) {
				returnConnection();
				throw new DaoException(StringHelper.EXCEPTION_INSERT, e);
			}
		}

	}

	@Override
	public void update(Coupon updateObject) throws DaoException {
		String sql = StringHelper.SQL_UPDATE;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON)
				.replaceAll(StringHelper.PARAMETERS_UPDATE_PLACE_HOLDER, StringHelper.UPDATE_PARAMETERS_COUPON);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, updateObject.getCompanyID());
			int categoryId = categoriesDao.get(updateObject.getCategory().toString().toUpperCase());
			preparedStatement.setInt(2, categoryId);
			preparedStatement.setString(3, updateObject.getTitle());
			preparedStatement.setString(4, updateObject.getDescription());
			preparedStatement.setDate(5, updateObject.getStartDate());
			preparedStatement.setDate(6, updateObject.getEndDate());
			preparedStatement.setInt(7, updateObject.getAmount());
			preparedStatement.setDouble(8, updateObject.getPrice());
			preparedStatement.setString(9, updateObject.getImage());
			preparedStatement.setInt(10, updateObject.getID());
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

	@Override
	public Coupon get(int id) throws DaoException {
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON)
				.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER, StringHelper.GET_PARAMETERS_COUPON);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int ID = resultSet.getInt("couponId");
				int companyID = resultSet.getInt("companyId");
				int categoryId = resultSet.getInt("categoryId");
				String categoryName = categoriesDao.get(categoryId);
				Category category = Category.valueOf(categoryName);
				String title = resultSet.getString("title");
				String description = resultSet.getString("description");
				Date startDate = resultSet.getDate("startDate");
				Date endDate = resultSet.getDate("endDate");
				int amount = resultSet.getInt("amount");
				double price = resultSet.getDouble("price");
				String image = resultSet.getString("image");

				returnConnection();
				return new Coupon(ID, companyID, category, title, description, startDate, endDate, amount, price,
						image);
			}
			return null;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}

	}

	@Override
	public List<Coupon> getAll() throws DaoException {
		List<Coupon> list = new ArrayList<>();
		String sql = StringHelper.sql_GET_ALL;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// preparedStatement.setString(1, table);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int ID = resultSet.getInt("couponId");
				int companyID = resultSet.getInt("companyId");
				int categoryId = resultSet.getInt("categoryId");
				String categoryName = categoriesDao.get(categoryId);
				Category category = Category.valueOf(categoryName);
				String title = resultSet.getString("title");
				String description = resultSet.getString("description");
				Date startDate = resultSet.getDate("startDate");
				Date endDate = resultSet.getDate("endDate");

				int amount = resultSet.getInt("amount");
				double price = resultSet.getDouble("price");
				String image = resultSet.getString("image");
				// Coupon coupon=
				list.add(new Coupon(ID, companyID, category, title, description, startDate, endDate, amount, price,
						image));
			}
			returnConnection();
			return list;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET_ALL, e);
		}

	}

	@Override
	public void addCouponPurchase(int customerID, int couponID) throws DaoException {
		String sql = StringHelper.SQL_ADD;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTVSCOUPONS)
				.replaceAll(StringHelper.PARAMETERS_ADD_PLACE_HOLDER, StringHelper.ADD_PARAMETERS_CUSTVSCOUPONS_COUPON);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, customerID);
			preparedStatement.setInt(2, couponID);
			preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("inserted");
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_INSERT, e);
		}

	}

	@Override
	public void deleteCouponPurchase(int customerID, int couponID) throws DaoException {
		String sql = StringHelper.SQL_DELETE;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTVSCOUPONS)
				.replaceAll(StringHelper.PARAMETERS_DELETE_PLACE_HOLDER, StringHelper.DELETE_PARAMETERS_CUSTVSCOUPONS);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, customerID);
			preparedStatement.setInt(2, couponID);
			int result = preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("deleted: " + result);
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_DELETE, e);
		}

	}

	@Override
	public void delete(int objectID) throws DaoException {
		// get query text
		String sql = StringHelper.SQL_DELETE;
		// replace all place holders
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON)
				.replaceAll(StringHelper.PARAMETERS_DELETE_PLACE_HOLDER, StringHelper.DELETE_PARAMETERS_COUPON);
		System.out.println(sql);

		getConnection();
		// get statement
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// set parameters
			preparedStatement.setInt(1, objectID);
			// execute query
			int result = preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("deleted: " + result);
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_DELETE, e);
		}

	}

	@Override
	public List<Coupon> getCustomerCoupons(int customerID) throws DaoException {
		List<Coupon> coupons = new ArrayList<>();
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTVSCOUPONS)
				.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER,
						StringHelper.GET_PARAMETERS_CUSTVSCOUPONS_CUSTOMER)
				.replaceAll(" \\* ", " couponId ");
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// set parameters
			preparedStatement.setInt(1, customerID);
			// execute query
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int ID = resultSet.getInt("couponId");
				coupons.add(get(ID));
			}

			returnConnection();

		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		return coupons;

	}

	@Override
	public List<Coupon> getCustomerCoupons(int customerID, Category category) throws DaoException {
		{
			List<Coupon> coupons = new ArrayList<>();
			String sql = StringHelper.SQL_QUERY_GET_COUPONS_OF_CUSTOMER_BY_CATEGORY;
			System.out.println(sql);
			int categoryId = categoriesDao.get(category.name().toUpperCase());
			getConnection();
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				// set parameters
				preparedStatement.setInt(1, customerID);
				preparedStatement.setInt(2, categoryId);
				// execute query
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					int ID = resultSet.getInt("couponId");
					int companyID = resultSet.getInt("companyId");
					categoryId = resultSet.getInt("categoryId");
					String categoryName = categoriesDao.get(categoryId);
					category = Category.valueOf(categoryName);
					String title = resultSet.getString("title");
					String description = resultSet.getString("description");
					Date startDate = resultSet.getDate("startDate");
					Date endDate = resultSet.getDate("endDate");
					int amount = resultSet.getInt("amount");
					double price = resultSet.getDouble("price");
					String image = resultSet.getString("image");
					coupons.add(new Coupon(ID, companyID, category, title, description, startDate, endDate, amount,
							price, image));

				}

				returnConnection();

			} catch (SQLException e) {
				returnConnection();
				throw new DaoException(StringHelper.EXCEPTION_GET, e);
			}
			return coupons;

		}

	}

	@Override
	public List<Coupon> getCustomerCoupons(int customerID, double maxPrice) throws DaoException {
		List<Coupon> coupons = new ArrayList<>();
		String sql = StringHelper.SQL_QUERY_GET_COUPONS_OF_CUSTOMER_BY_PRICE;
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// set parameters
			preparedStatement.setInt(1, customerID);
			preparedStatement.setDouble(2, maxPrice);
			// execute query
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int ID = resultSet.getInt("couponId");
				int companyID = resultSet.getInt("companyId");
				int categoryId = resultSet.getInt("categoryId");
				String categoryName = categoriesDao.get(categoryId);
				Category category = Category.valueOf(categoryName);
				String title = resultSet.getString("title");
				String description = resultSet.getString("description");
				Date startDate = resultSet.getDate("startDate");
				Date endDate = resultSet.getDate("endDate");
				int amount = resultSet.getInt("amount");
				double price = resultSet.getDouble("price");
				String image = resultSet.getString("image");
				coupons.add(new Coupon(ID, companyID, category, title, description, startDate, endDate, amount, price,
						image));

			}

			returnConnection();

		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		return coupons;

	}

	@Override
	public List<Coupon> getCompanyCoupons(int companyID) throws DaoException {
		List<Coupon> coupons = new ArrayList<>();
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON).replaceAll("_GET_PARAMETERS_",
				StringHelper.GET_PARAMETERS_COUPONS_OF_COMPANY);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// set parameters
			preparedStatement.setInt(1, companyID);
			// execute query
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int ID = resultSet.getInt("couponId");
				companyID = resultSet.getInt("companyId");
				int categoryId = resultSet.getInt("categoryId");
				String categoryName = categoriesDao.get(categoryId);
				Category category = Category.valueOf(categoryName);
				String title = resultSet.getString("title");
				String description = resultSet.getString("description");
				Date startDate = resultSet.getDate("startDate");
				Date endDate = resultSet.getDate("endDate");

				int amount = resultSet.getInt("amount");
				double price = resultSet.getDouble("price");
				String image = resultSet.getString("image");
				// Coupon coupon=
				coupons.add(new Coupon(ID, companyID, category, title, description, startDate, endDate, amount, price,
						image));
			}

			returnConnection();

		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		return coupons;

	}

	@Override
	public List<Coupon> getCompanyCoupons(int companyID, Category category) throws DaoException {
		List<Coupon> coupons = new ArrayList<>();
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON).replaceAll("_GET_PARAMETERS_",
				StringHelper.GET_PARAMETERS_COUPONS_OF_COMPANY__BY_CATEGORY);
		System.out.println(sql);
		int categoryId = categoriesDao.get(category.name().toUpperCase());
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// set parameters
			preparedStatement.setInt(1, companyID);
			preparedStatement.setInt(2, categoryId);
			// execute query
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int ID = resultSet.getInt("couponId");
				companyID = resultSet.getInt("companyId");
				categoryId = resultSet.getInt("categoryId");
				String categoryName = categoriesDao.get(categoryId);
				category = Category.valueOf(categoryName);
				String title = resultSet.getString("title");
				String description = resultSet.getString("description");
				Date startDate = resultSet.getDate("startDate");
				Date endDate = resultSet.getDate("endDate");
				int amount = resultSet.getInt("amount");
				double price = resultSet.getDouble("price");
				String image = resultSet.getString("image");
				coupons.add(new Coupon(ID, companyID, category, title, description, startDate, endDate, amount, price,
						image));
			}

			returnConnection();

		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		return coupons;

	}

	@Override
	public List<Coupon> getCompanyCoupons(int companyID, double maxPrice) throws DaoException {
		List<Coupon> coupons = new ArrayList<>();
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON).replaceAll("_GET_PARAMETERS_",
				StringHelper.GET_PARAMETERS_COUPONS_OF_COMPANY_BY_PRICE);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// set parameters
			preparedStatement.setInt(1, companyID);
			preparedStatement.setDouble(2, maxPrice);
			// execute query
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int ID = resultSet.getInt("couponId");
				companyID = resultSet.getInt("companyId");
				int categoryId = resultSet.getInt("categoryId");
				String categoryName = categoriesDao.get(categoryId);
				Category category = Category.valueOf(categoryName);
				String title = resultSet.getString("title");
				String description = resultSet.getString("description");
				Date startDate = resultSet.getDate("startDate");
				Date endDate = resultSet.getDate("endDate");
				int amount = resultSet.getInt("amount");
				double price = resultSet.getDouble("price");
				String image = resultSet.getString("image");
				coupons.add(new Coupon(ID, companyID, category, title, description, startDate, endDate, amount, price,
						image));
			}
			returnConnection();

		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		return coupons;

	}

	@Override
	public boolean isCouponExsist(int companyID, String title) throws DaoException {
		String sql = StringHelper.SQL_QUERY_GET_COUPONS_OF_COMPANY_BY_TITLE;
		System.out.println(sql);
		getConnection();
		// check if current coupon is not exist with some title for this company
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, companyID);
			preparedStatement.setString(2, title);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (resultSet.getInt(1) > 0) {
					returnConnection();
					throw new DaoException(StringHelper.EXCEPTION_COUPON_ADD_ALREADY_EXIST);
				}
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		return false;
	}

}
