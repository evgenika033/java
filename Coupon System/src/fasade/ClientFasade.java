package fasade;

import dao.CompaniesDao;
import dao.CouponsDao;
import dao.CustomersDao;
import exceptions.DaoException;

public abstract class ClientFasade {
	protected CompaniesDao companiesDao = new CompaniesDao();
	protected CustomersDao customersDao = new CustomersDao();
	protected CouponsDao couponsDao = new CouponsDao();

	/**
	 * login
	 * 
	 * @param email    the user's email
	 * @param password the user's password
	 * @return boolean
	 * @throws DaoException on database work
	 */
	public abstract boolean login(String email, String password) throws DaoException;
}
