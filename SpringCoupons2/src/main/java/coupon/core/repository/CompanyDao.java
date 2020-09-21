package coupon.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import coupon.core.entity.Company;

public interface CompanyDao extends JpaRepository<Company, Integer> {

	@Query(value = "select count(*) from Company where name = :name or email= :email", nativeQuery = true)
	Integer countOfCompanyByNameAndEmailForAdd(String name, String email);

	@Query(value = "select count(*) from Company where email= :email and id!=:id", nativeQuery = true)
	// count of companies with the same email and different id
	Integer countOfOtherCompanyByEmail(String email, Integer id);
}
