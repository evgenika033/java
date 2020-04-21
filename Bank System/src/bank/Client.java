package bank;

public class Client {
	private int id;
	private String name;
	private float balance;
	private Account[] accounts = new Account[5];
	private float commissionRate;
	private float interestRate;
	private Logger logger;

	public Client(int id, String name, float balance) {
		super();
		this.id = id;
		this.name = name;
		this.balance = balance;

	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public Account[] getAccounts() {

		return accounts;
	}

	// add the account to the array and log the operation
	public void addAccount(Account account) {
		// You should seek the array and place the Account where the first null value is
		// found.

	}

	public Account getAccount(int index) {
		return accounts[index];
	}

	public void removeAccount(int id) {
		/*
		 * remove account (int id) : void - remove the account with the same id from the
		 * array (by assigning a 'null' value to the array[position]) & transfers the
		 * money to the clients balance. Log the operation via creating Log object with
		 * appropriate data and sending it to the Logger.log(..) method.
		 */

	}

	public void deposit(float balance) {
		/*
		 * void - implement to add of remove the amount from clients balance according
		 * to the commission (which is now zero). Use the commission data member in your
		 * calculation)
		 * 
		 */
	}

	public void withdraw(float balance) {

	}

	public void autoUpdateAccounts() {
		/*
		 * : void – run over the accounts, calculate the amount to add according to the
		 * client interest (meanwhile it is zero) and add it to each account balance.
		 * Use the interest data member in your calculation. Log this operation.
		 */

	}
	public float getFortune() {
		return 0;
	
		// sum account balance
		// returns the sum of client balance +
	//	total account balance.
	}	
}
