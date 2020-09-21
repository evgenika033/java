package coupon.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coupon.core.entity.Company;
import coupon.core.repository.CompanyDao;

@Transactional
@Service
public class CompanyServiceImpl implements CompanyService {
	@Autowired
	private CompanyDao companyDao;

	@Override
	public void add(Company addObject) {
		boolean isNewObject = true;
		if (isValidCompany(addObject, isNewObject)) {
			if (companyDao.countOfCompanyByNameAndEmailForAdd(addObject.getName(), addObject.getEmail()) == 0) {
				companyDao.save(addObject);
				System.out.println("added company:  successful " + addObject.getName());
			} else {

				System.out.println("added company: already exist: " + addObject.getName());
			}
		} else {
			System.out.println("added company is not valid object");
		}

	}

	@Override
	public Company get(Integer id) {
		Optional<Company> opt = companyDao.findById(id);
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;

	}

	@Override
	public List<Company> get() {
		return companyDao.findAll();
	}

	@Override
	public void update(Company updateObject) {
		boolean isNewObject = false;
		// testing that company not null
		// validate that company id is not null
		// validate that company id > -1
		if (isValidCompany(updateObject, isNewObject)) {
			Company companyDB = get(updateObject.getId());
			if (companyDB != null) {
				// updateObject.setName(companyDB.getName());
				if (companyDao.countOfOtherCompanyByEmail(updateObject.getEmail(), updateObject.getId()) == 0) {
					// companyDao.save(updateObject);
					String email = updateObject.getEmail();
					String password = updateObject.getPassword();
					companyDB.setEmail(email);
					companyDB.setPassword(password);
					companyDao.save(companyDB);
					System.out.println("update company: updated " + companyDB.getName());
				} else {
					System.out.println("update company: the company email exist in other company");
				}
			} else {
				System.out.println("update company: the company not exist");
			}
		}

	}

	private boolean isValidCompany(Company company, boolean isNewObject) {
		// for update company
		if (!isNewObject) {
			return company != null && company.getId() != null && company.getId() > -1;
		}
		// for new company
		return company != null && company.getId() == null;
	}

	@Override
	public void delete(Integer id) {
		companyDao.deleteById(id);

	}

}
