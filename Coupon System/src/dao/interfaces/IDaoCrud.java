package dao.interfaces;

import java.util.List;

import exceptions.DaoException;

public interface IDaoCrud<T> {

	void add(T addObject) throws DaoException;

	void delete(int objectID) throws DaoException;

	void update(T updateObject) throws DaoException;

	T get(int id) throws DaoException;

	List<T> getAll() throws DaoException;

}
