package dao.interfaces;

import exceptions.DaoException;

public interface IDaoSimple {
	String get(int id) throws DaoException;

	int get(String name) throws DaoException;
}
