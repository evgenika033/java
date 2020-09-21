package coupon.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import coupon.core.entity.Customer;

public interface CustomerDao extends JpaRepository<Customer, Integer> {
	// List<Customer> getByName(String name);
}
