package coupon.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coupon.core.entity.Customer;
import coupon.core.repository.CustomerDao;

@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerDao customerDao;

	@Override
	public void add(Customer addObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public Customer get(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Customer updateObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Integer id) {
		customerDao.deleteById(id);

	}

}
