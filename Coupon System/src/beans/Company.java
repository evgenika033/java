package beans;

import java.util.ArrayList;
import java.util.List;

public class Company {
	private int ID;
	private String name;
	private String email;
	private String password;
	private List<Coupon> coupons;

	/**
	 * ctor
	 */
	public Company() {
		init();
	}

	/*
	 * ctor
	 */
	public Company(String name, String email, String password) {
		this();
		this.name = name;
		this.email = email;
		this.password = password;

	}

	/*
	 * ctor
	 */
	public Company(int id, String name, String email, String password) {

		this(name, email, password);
		this.ID = id;

	}

	/*
	 * init
	 */
	private void init() {
		name = "";
		email = "";
		password = "";
		coupons = new ArrayList<>();
	}

	/*
	 * getters/setters
	 */
	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Company [ID=" + ID + ", name=" + name + ", email=" + email + ", password=" + password + ", coupons="
				+ coupons + "]";
	}

}
