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
	private CategoriesDao categoriesDao;

	// ctor
	public CouponsDao() {
		init();
	}

	/**
	 * Initializing
	 */
	private void init() {
		categoriesDao = new CategoriesDao();
	}

	/**
	 * get connection from connectionPool
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
	 * return connection to connectionPool
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
	public void add(Coupon addObject) throws DaoException {
		String sql = StringHelper.SQL_ADD;// "insert into _TABLE_NAME_ values(_ADD_PARAMETERS_)";
		// replace place_holders
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON)
				.replaceAll(StringHelper.PARAMETERS_ADD_PLACE_HOLDER, StringHelper.ADD_PARAMETERS_COUPON);
		if (isCouponValid(addObject.getCompanyID(), addObject.getTitle())) {
			getConnection();
			// continue add coupon
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
				System.out.println(sql);
				couponToStatement(preparedStatement, addObject, true);
				preparedStatement.executeUpdate();
				returnConnection();
				System.out.println("coupon inserted: " + addObject.getTitle());
			} catch (SQLException e) {
				returnConnection();
				throw new DaoException(StringHelper.EXCEPTION_INSERT, e);
			}
		}
	}

	/**
	 * convert preparedStatement values from coupon
	 * 
	 * @param preparedStatement
	 * @param coupon
	 * @param isAdd
	 * @throws DaoException
	 */
	private void couponToStatement(PreparedStatement preparedStatement, Coupon coupon, boolean isAdd)
			throws DaoException {
		try {
			if (isAdd) {
				// for new coupon
				preparedStatement.setInt(1, coupon.getCompanyID());
				int categoryId = categoriesDao.get(coupon.getCategory().name().toUpperCase());
				preparedStatement.setInt(2, categoryId);
				preparedStatement.setString(3, coupon.getTitle());
				preparedStatement.setString(4, coupon.getDescription());
				preparedStatement.setDate(5, coupon.getStartDate());
				preparedStatement.setDate(6, coupon.getEndDate());
				preparedStatement.setInt(7, coupon.getAmount());
				preparedStatement.setDouble(8, coupon.getPrice());
				preparedStatement.setString(9, coupon.getImage());
			} else {
				// for update coupon
				int categoryId = categoriesDao.get(coupon.getCategory().toString().toUpperCase());
				preparedStatement.setInt(1, coupon.getCompanyID());
				preparedStatement.setInt(2, categoryId);
				preparedStatement.setString(3, coupon.getTitle());
				preparedStatement.setString(4, coupon.getDescription());
				preparedStatement.setDate(5, coupon.getStartDate());
				preparedStatement.setDate(6, coupon.getEndDate());
				preparedStatement.setInt(7, coupon.getAmount());
				preparedStatement.setDouble(8, coupon.getPrice());
				preparedStatement.setString(9, coupon.getImage());
				preparedStatement.setInt(10, coupon.getID());
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_INSERT, e);
		}

	}

	@Override
	public void update(Coupon updateObject) throws DaoException {
		boolean newCoupon = false;
		String sql = StringHelper.SQL_UPDATE.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON);
		sql = sql.replaceAll(StringHelper.PARAMETERS_UPDATE_PLACE_HOLDER, StringHelper.UPDATE_PARAMETERS_COUPON);
		System.out.println(sql);
		Coupon couponDB = get(updateObject.getID());
		isCouponValidate(couponDB);
		updateObject.setCompanyID(couponDB.getCompanyID());
		updateObject.setTitle(couponDB.getTitle());
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			// preparedStatement.setInt(1, updateObject.getCompanyID());
			couponToStatement(preparedStatement, updateObject, newCoupon);
			int result = preparedStatement.executeUpdate();
			returnConnection();
			if (result > 0) {
				System.out.println("coupon updated true: " + updateObject.getTitle());
			} else {
				System.out.println("coupon updated false: " + updateObject.getTitle());
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_UPDATE, e);
		}
	}

	/**
	 * validate coupon for null
	 * 
	 * @param coupon
	 * @throws DaoException
	 */
	private void isCouponValidate(Coupon coupon) throws DaoException {
		if (coupon == null) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_COUPON_NOT_FOUND);
		}

	}

	@Override
	public Coupon get(int id) throws DaoException {
		String sql = StringHelper.SQL_GET.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON);
		sql = sql.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER, StringHelper.GET_PARAMETERS_COUPON);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			if (resultSet.next()) {
				return resultToCoupon(resultSet);
			}
			return null;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}

	}

	@Override
	public Coupon get(int companyID, String title) throws DaoException {
		String sql = StringHelper.SQL_GET.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON);
		sql = sql.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER,
				StringHelper.GET_PARAMETERS_COUPONS_OF_COMPANY_BY_TITLE);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, companyID);
			preparedStatement.setString(2, title);
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			if (resultSet.next()) {
				return resultToCoupon(resultSet);
			}
			return null;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
	}

	@Override
	public List<Coupon> get(Date date) throws DaoException {
		List<Coupon> list = new ArrayList<>();
		String sql = StringHelper.SQL_GET;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON)
				.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER, StringHelper.GET_PARAMETERS_COUPONS_BY_DATE);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setDate(1, date);
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			while (resultSet.next()) {
				list.add(resultToCoupon(resultSet));
			}
			return list;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET);
		}
	}

	@Override
	public List<Coupon> getAll() throws DaoException {
		List<Coupon> list = new ArrayList<>();
		String sql = StringHelper.SQL_GET_ALL;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			while (resultSet.next()) {
				list.add(resultToCoupon(resultSet));
			}
			return list;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET_ALL, e);
		}
	}

	@Override
	public void addCouponPurchase(int customerID, int couponID) throws DaoException {
		boolean forCustomerTest = false;
		String sql = StringHelper.SQL_ADD.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTVSCOUPONS);
		sql = sql.replaceAll(StringHelper.PARAMETERS_ADD_PLACE_HOLDER,
				StringHelper.ADD_PARAMETERS_CUSTVSCOUPONS_COUPON);
		System.out.println(sql);
		if (isPurchaseCouponAmountValid(couponID)
				&& isPurchaseCouponCustomerValid(couponID, customerID, forCustomerTest)) {
			Coupon coupon = get(couponID);
			coupon.setAmount(coupon.getAmount() - 1);
			update(coupon);
			getConnection();
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
				preparedStatement.setInt(1, customerID);
				preparedStatement.setInt(2, couponID);
				preparedStatement.executeUpdate();
				returnConnection();
				System.out.println(
						String.format("Coupon Purchase inserted: customer(%s), coupon(%s)", customerID, couponID));
			} catch (SQLException e) {
				returnConnection();
				throw new DaoException(StringHelper.EXCEPTION_INSERT, e);
			}
		}

	}

	@Override
	public void deleteCouponsPurchase(int couponID) throws DaoException {
		String sql = StringHelper.SQL_DELETE;
		sql = sql.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_CUSTVSCOUPONS)
				.replaceAll(StringHelper.PARAMETERS_DELETE_PLACE_HOLDER, StringHelper.DELETE_PARAMETERS_COUPON);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, couponID);
			int result = preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("delete all coupons Purchase: " + result);
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_DELETE, e);
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
			System.out.println(
					String.format("coupon Purchase deleted: customer(%s), coupon(%s)", customerID, couponID) + result);
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
		deleteCouponsPurchase(objectID);
		getConnection();
		// get statement
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// set parameters
			preparedStatement.setInt(1, objectID);
			// execute query
			int result = preparedStatement.executeUpdate();
			returnConnection();
			System.out.println("coupon deleted id " + objectID + ": " + result);
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
			returnConnection();
			while (resultSet.next()) {
				int ID = resultSet.getInt("couponId");
				coupons.add(get(ID));
			}
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
				returnConnection();
				while (resultSet.next()) {
					coupons.add(resultToCoupon(resultSet));
				}
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
			returnConnection();
			while (resultSet.next()) {
				coupons.add(resultToCoupon(resultSet));
			}
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
			returnConnection();
			while (resultSet.next()) {
				coupons.add(resultToCoupon(resultSet));
			}
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
				StringHelper.GET_PARAMETERS_COUPONS_OF_COMPANY_BY_CATEGORY);
		System.out.println(sql);
		int categoryId = categoriesDao.get(category.name().toUpperCase());
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// set parameters
			preparedStatement.setInt(1, companyID);
			preparedStatement.setInt(2, categoryId);
			// execute query
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			while (resultSet.next()) {
				coupons.add(resultToCoupon(resultSet));
			}
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
			returnConnection();
			while (resultSet.next()) {
				coupons.add(resultToCoupon(resultSet));
			}

		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		return coupons;

	}

	/**
	 * convert coupon from resultSet
	 * 
	 * @param resultSet
	 * @return Coupon
	 * @throws DaoException
	 */
	private Coupon resultToCoupon(ResultSet resultSet) throws DaoException {
		Coupon coupon = new Coupon();
		try {
			coupon.setID(resultSet.getInt("couponId"));
			coupon.setCompanyID(resultSet.getInt("companyId"));
			int categoryId = resultSet.getInt("categoryId");
			String categoryName = categoriesDao.get(categoryId);
			coupon.setCategory(Category.valueOf(categoryName));
			coupon.setTitle(resultSet.getString("title"));
			coupon.setDescription(resultSet.getString("description"));
			coupon.setStartDate(resultSet.getDate("startDate"));
			coupon.setEndDate(resultSet.getDate("endDate"));
			coupon.setAmount(resultSet.getInt("amount"));
			coupon.setPrice(resultSet.getDouble("price"));
			coupon.setImage(resultSet.getString("image"));
			return coupon;
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}

	}

	@Override
	public boolean isCouponValid(int companyID, String title) throws DaoException {
		String sql = StringHelper.SQL_GET_COUNT.replaceAll(StringHelper.TABLE_PLACE_HOLDER, StringHelper.TABLE_COUPON);
		sql = sql.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER,
				StringHelper.GET_PARAMETERS_COUPONS_OF_COMPANY_BY_TITLE);

		if (title.equals("accessories7")) {
			System.out.println(title);
		}
		System.out.println(sql);
		getConnection();
		// check if current coupon is not exist with some title for this company
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, companyID);
			preparedStatement.setString(2, title);
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			if (resultSet.next()) {
				if (resultSet.getInt(1) > 0) {
					throw new DaoException(StringHelper.EXCEPTION_COUPON_ADD_ALREADY_EXIST);
				}
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		return true;
	}

	@Override
	public boolean isPurchaseCouponAmountValid(int couponID) throws DaoException {
		Coupon coupon = get(couponID);
		isCouponValidate(coupon);
		if (coupon.getAmount() < 1) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_COUPON_AMOUNT_EMPTY);
		}
		return true;
	}

	// validate if current coupon is not purchased by current user
	@Override
	public boolean isPurchaseCouponCustomerValid(int couponID, int customerID, boolean forCustomerTest)
			throws DaoException {
		String sql = StringHelper.SQL_GET_COUNT.replaceAll(StringHelper.TABLE_PLACE_HOLDER,
				StringHelper.TABLE_CUSTVSCOUPONS);
		sql = sql.replaceAll(StringHelper.PARAMETERS_GET_PLACE_HOLDER,
				StringHelper.GET_PARAMETERS_CUSTVSCOUPONS_CUSTOMERID_AND_COUPONID);
		System.out.println(sql);
		getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
			preparedStatement.setInt(1, customerID);
			preparedStatement.setInt(2, couponID);
			ResultSet resultSet = preparedStatement.executeQuery();
			returnConnection();
			if (resultSet.next()) {
				if (resultSet.getInt(1) > 0) {
					if (forCustomerTest) {
						return false;
					}
					throw new DaoException(StringHelper.EXCEPTION_COUPON_PURCHASE_ALREADY_EXIST_IN_CUSTOMER);
				}
			}
		} catch (SQLException e) {
			returnConnection();
			throw new DaoException(StringHelper.EXCEPTION_GET, e);
		}
		return true;
	}

}
