package beans;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	private int ID;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private List<Coupon> coupons;

	public Customer() {
		init();
	}

	public Customer(int iD, String firstName, String lastName, String email, String password) {
		this();
		ID = iD;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;

	}

	public void init() {
		firstName = "";
		lastName = "";
		email = "";
		password = "";
		coupons = new ArrayList<Coupon>();
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
		return "Customer [ID=" + ID + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", coupons=" + coupons + "]";
	}

}
