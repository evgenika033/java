package dao.interfaces;

import exceptions.DaoException;

public interface ICompaniesDao<T> extends IDaoCrud<T> {
	boolean isCompanyExist(String email, String password) throws DaoException;
}
