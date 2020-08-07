package dao.interfaces;

import beans.Category;
import exceptions.DaoException;

public interface IDaoSimple {
	String get(int id) throws DaoException;

	int get(String name) throws DaoException;

	void add(Category category) throws DaoException;
}
