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
	 * @param email
	 * @param password
	 * @return
	 * @throws DaoException
	 */
	public abstract boolean login(String email, String password) throws DaoException;
}
