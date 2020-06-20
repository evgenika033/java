package bank.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bank.core.exceptions.BankException;
import bank.core.exceptions.WithdrowException;

public abstract class Client implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private float balance;
	// private Account[] accounts = new Account[5];
	private List<Account> accounts;
	protected float commissionRate;
	protected float interestRate;

	public Client(int id, String name, float balance) {
		super();
		this.id = id;
		this.name = name;
		this.balance = balance;
		init();
	}

	private void init() {
		accounts = new ArrayList<Account>();
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

	public List<Account> getAccounts() {

		return accounts;
	}

	// add the account to the array and log the operation
	public void addAccount(Account account) {
		// You should seek the array and place the Account where the first null value is
		// found.
		if (account != null) {
			accounts.add(account);
			Date timeStamp = new Date();
			String description = "account added: " + account.getId();
			float amount = account.getBalance();
			Log log = new Log(timeStamp, id, description, amount);
			Logger.log(log);

		} else {
			System.out.println(" Account not added because it was null");
		}

	}

	public Account getAccount(int index) {
		if (index < accounts.size() - 1) {
			return accounts.get(index);
		}
		return null;
	}

	public void removeAccount(Account account) {
		/*
		 * remove account (int id) : void - remove the account with the same id from the
		 * array (by assigning a 'null' value to the array[position]) & transfers the
		 * money to the clients balance. Log the operation via creating Log object with
		 * appropriate data and sending it to the Logger.log(..) method.
		 */
		int index = accounts.indexOf(account);

		if (index != -1) {
			Account currAccount = accounts.get(index);
			accounts.remove(index);
			balance += currAccount.getBalance();
			Date timeStamp = new Date();
			String description = "account removed: " + currAccount.getId();
			float amount = currAccount.getBalance();
			Log log = new Log(timeStamp, currAccount.getId(), description, amount);
			Logger.log(log);
		} else {
			System.out.println(" Account id " + account.getId() + " was not removed(not found)");
		}

	}

	public void deposit(float balance) {
		/*
		 * void - implement to add of remove the amount from clients balance according
		 * to the commission (which is now zero). Use the commission data member in your
		 * calculation)
		 * 
		 */
		float commission = balance * commissionRate;
		this.balance += balance;
		this.balance -= commission;
		try {
			Bank.getInstance().addCommission(commission);
		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date timeStamp = new Date();
		String description = "deposit";
		float amount = balance;
		Log log = new Log(timeStamp, id, description, amount);
		Logger.log(log);

	}

	public void withdraw(float withdrawAmount) throws WithdrowException {
		float commission = withdrawAmount * commissionRate;
		if (withdrawAmount + commission > this.balance) {
			throw new WithdrowException("withdrau error - you do not have enough money", id, this.balance,
					withdrawAmount + commission);
		}
		this.balance -= withdrawAmount;
		this.balance -= commission;
		Date timeStamp = new Date();
		String description = "withdraw";
		float amount = withdrawAmount;
		Log log = new Log(timeStamp, id, description, amount);
		Logger.log(log);
	}

	public void autoUpdateAccounts() {
		/*
		 * : void – run over the accounts, calculate the amount to add according to the
		 * client interest (meanwhile it is zero) and add it to each account balance.
		 * Use the interest data member in your calculation. Log this operation.
		 */
		for (Account account : accounts) {

			if (account != null) {
				float interest = account.getBalance() * interestRate;
				account.setBalance(account.getBalance() + interest);
				Date timeStamp = new Date();
				String description = "autoUpdateAccount: " + account.getId();
				float amount = interest;
				Log log = new Log(timeStamp, id, description, amount);
				Logger.log(log);
			}
		}

	}

	public float getFortune() {
		// sum account balance
		// returns the sum of client balance +
		// total account balance.
		float fortune = balance;
		for (Account account : accounts) {
			fortune += account.getBalance();
		}

		return fortune;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Client)) {
			return false;
		}
		Client other = (Client) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
