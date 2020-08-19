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

	/*
	 * ctor
	 */
	public Customer() {
		init();
	}

	/*
	 * ctor
	 */
	public Customer(String firstName, String lastName, String email, String password) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;

	}
	/*
	 * ctor
	 */

	public Customer(int ID, String firstName, String lastName, String email, String password) {
		this(firstName, lastName, email, password);
		this.ID = ID;

	}

	/*
	 * ctor
	 */
	public void init() {
		firstName = "";
		lastName = "";
		email = "";
		password = "";
		coupons = new ArrayList<Coupon>();
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
