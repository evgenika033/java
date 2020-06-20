package bank.test;

import bank.core.Bank;
import bank.core.Client;
import bank.core.exceptions.BankException;

public class Testsave {

	public static void main(String[] args) {
		Bank bank;
		try {
			bank = Bank.getInstance();
//			bank.addClient(new ClientGold(100, "Jeka", 1000));
//			bank.addClient(new ClientGold(101, "Moti", 2000));
//			bank.addClient(new ClientGold(102, "Adam", 3000));
//			bank.store();
			for (Client client : bank.getClients()) {
				System.out.println(client);
			}
		} catch (BankException e) {

			e.printStackTrace();
		}
	}

}
