package loginManager;

import exceptions.DaoException;
import fasade.AdminFasade;
import fasade.ClientFasade;
import fasade.CompanyFacade;
import fasade.CustomerFasade;

public class LoginManager {

	private static LoginManager instance;

	private LoginManager() {

	}

	public static LoginManager getInstance() {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	public ClientFasade login(String email, String password, ClientType clientType) throws DaoException {
		switch (clientType) {
		case Administrator:
			return loginAdmin(email, password);
		case Customer:
			return loginCustomer(email, password);
		case Company:
			return loginCompany(email, password);

		default:
			return null;
		}
	}

	private ClientFasade loginAdmin(String email, String password) throws DaoException {
		AdminFasade adminFasade = new AdminFasade();
		if (adminFasade.login(email, password)) {
			return adminFasade;
		} else {
			return null;
		}
	}

	private ClientFasade loginCustomer(String email, String password) throws DaoException {
		CustomerFasade customerFasade = new CustomerFasade();
		if (customerFasade.login(email, password)) {
			return customerFasade;
		} else {
			return null;
		}
	}

	private ClientFasade loginCompany(String email, String password) throws DaoException {
		CompanyFacade companyFacade = new CompanyFacade();
		if (companyFacade.login(email, password)) {
			return companyFacade;
		} else {
			return null;
		}
	}
}
