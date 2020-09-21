package coupon.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import coupon.core.entity.Coupon;
import coupon.core.repository.CouponDao;

@Transactional
@Service
public class CouponServiceImpl implements CouponService {
	@Autowired
	private CouponDao couponDao;

	@Override
	public void add(Coupon addObject) {
		if (addObject != null) {
			if (couponDao.countOfCouponByTitleForAdd(addObject.getTitle(), addObject.getCompanyID()) == 0) {
				couponDao.save(addObject);
				System.out.println("added coupon:  successful " + addObject.getTitle());

			} else {
				System.out.println("added coupon: already exist: " + addObject.getTitle());
			}

		} else {
			System.out.println("added coupon: is not valid object");
		}

	}

	@Override
	public Coupon get(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Coupon> get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Coupon updateObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub

	}

}
