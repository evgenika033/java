package test;

import java.sql.SQLException;

import exceptions.DaoException;
import exceptions.DatabaseException;
import exceptions.PropertiesExceptions;

public class Program {

	public static void main(String[] args) throws DaoException, PropertiesExceptions, DatabaseException, SQLException {

		Test.testAll();
	}

}
