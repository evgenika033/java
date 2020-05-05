package bank.core;

public class Account {
	private int id;
	private float balance;
	
	public Account(int id, float balance) {
		super();
		this.id = id;
		this.balance = balance;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public float getBalance() {
		return balance;
	}
	
	public void setBalance(float balance) {
		this.balance = balance;
	}
	
		
	

}
