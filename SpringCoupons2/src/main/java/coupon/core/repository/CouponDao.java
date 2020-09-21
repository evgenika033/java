package coupon.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import coupon.core.entity.Coupon;

public interface CouponDao extends JpaRepository<Coupon, Integer> {

	@Query(value = "select count(*) from Coupon where title = :title and companyid= :companyId", nativeQuery = true)
	Integer countOfCouponByTitleForAdd(String title, Integer companyId);
}
