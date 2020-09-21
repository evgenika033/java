package coupon.core;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import coupon.core.entity.Category;
import coupon.core.entity.Company;
import coupon.core.entity.Coupon;
import coupon.core.service.CompanyService;
import coupon.core.service.CouponService;
import coupon.core.service.CustomerService;

@SpringBootApplication
public class App implements ApplicationContextAware {
	private static ApplicationContext ctx;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		// =====services========================
		CompanyService companyService = ctx.getBean(CompanyService.class);
		CouponService couponService = ctx.getBean(CouponService.class);
		CustomerService customerService = ctx.getBean(CustomerService.class);
		// ==============company========================
		Company company = new Company("versis", "versis@mail.com", "123456");
		Company company1 = new Company("versisa", "versisa@mail.com", "123456");

		companyService.add(company);
		companyService.add(company1);

		// get company by id
		Company com = companyService.get(1);
		System.out.println("get company by id: " + com);

		companyService.update(com);

		// ======coupon=======================
		Coupon coupon = new Coupon(1, Category.ACCESSORIES, "title1", "desc1", null, null, 2, 20.02, null);
		Coupon coupon1 = new Coupon(1, Category.ACCESSORIES, "title2", "desc2", null, null, 5, 21.02, null);

		couponService.add(coupon1);
		// =-=============================
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;

	}

}
