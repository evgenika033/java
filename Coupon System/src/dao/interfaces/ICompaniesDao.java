package dao.interfaces;

import exceptions.DaoException;

public interface ICompaniesDao<T> extends IDaoCrud<T> {

	/**
	 * company login
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws DaoException
	 */
	T companyLogin(String email, String password) throws DaoException;

	/**
	 * validate unique company by name and email method for add new company: check
	 * existing company with same name or email
	 * 
	 * @param companyName
	 * @param companyEmail
	 * @return
	 * @throws DaoException
	 */
	boolean isCompanyValid(String companyName, String companyEmail) throws DaoException;

	/**
	 * validate unique company by name and email. method for update company: check
	 * existing company with same email but other id
	 * 
	 * @param companyEmail
	 * @param companyID
	 * @return
	 * @throws DaoException
	 */
	boolean isCompanyValid(String companyEmail, int companyID) throws DaoException;

	T get(String companyName) throws DaoException;

}
