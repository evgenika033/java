package dao.interfaces;

import exceptions.DaoException;

public interface ICustomersDao<T> extends IDaoCrud<T> {
	boolean isCustomerExist(String email, String password) throws DaoException;
}
