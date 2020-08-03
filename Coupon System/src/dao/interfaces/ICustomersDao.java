package dao.interfaces;

import exceptions.DaoException;

public interface ICustomersDao<T> extends IDaoCrud<T> {
	T customerLogin(String email, String password) throws DaoException;
}
