package bank.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bank.core.exceptions.BankException;
import bank.core.exceptions.BankLoadException;
import bank.core.exceptions.BankStoreException;

public class Bank {
	// private Client[] clients = new Client[100];
	private List<Client> clients;
	// private Logger logService;
	private String accountUpdater;
	private float balance;
	private File dataFile;
	private static Bank instance;

	private Bank() throws BankException {
		init();
		load();
		System.out.println("Bank constructor: clients loaded from file ");
	}

	public static Bank getInstance() throws BankException {
		if (instance == null) {
			instance = new Bank();
		}
		return instance;
	}

	private void init() {
		dataFile = new File("files/bank.data");
		clients = new ArrayList<Client>();
	}

	public void addCommission(float commissionAmount) {
		balance += commissionAmount;
	}

	public void printClientList() {
		Client[] clients = getClientsArray();
		System.out.println("=== Client List ====");
		for (int i = 0; i < clients.length; i++) {
			System.out.println(clients[i]);

		}
		System.out.println("=================");
	}

	public void setBalance(float balance) {
		/*
		 * this operation returns the bank balance. The balance is calculated by summing
		 * the total clients balance and the total accounts balance – you should use
		 * Client.getFortune() method of each client.
		 */
		this.balance = balance;
	}

	public float getBalance() {
		// float – just returns bank current value.
		return balance;

	}

	public float getClientFortune() {
		float totalFortune = 0;
		for (Client client : clients) {
			if (client != null) {
				totalFortune += client.getFortune();
			}
		}

		return totalFortune;
	}

	public void addClient(Client client) throws NullPointerException {
		// add the client to the array and log the operation.
		// You should seek the array and place the Client where the first null value is
		// found.
		if (client != null) {
			clients.add(client);
			Date timeStamp = new Date();
			String description = "addClient";
			float amount = client.getFortune();
			Log log = new Log(timeStamp, client.getId(), description, amount);
			Logger.log(log);
		} else {
			throw new NullPointerException("sorry , the client was not added, because no details where given");
		}

	}

	public boolean removeClient(Client client) {
		// remove the client with the same id from the array (by assigning a 'null'
		// value to the array[position]). Log the operation.

		int index = clients.indexOf(client);
		if (index > -1) {
			Client currentClient = clients.get(index);

			clients.remove(index);
			Date timeStamp = new Date();
			String description = "removeClient";
			float amount = client.getFortune();
			Log log = new Log(timeStamp, client.getId(), description, amount);
			Logger.log(log);
			return true;
		} else {
			System.out.println("client not removed");
			return false;
		}

	}

	public List<Client> getClients() {
		return clients;
	}

	public Client[] getClientsArray() {
		return clients.toArray(new Client[clients.size()]);
	}

	public void viewLogs() {
		// prints all logs that are stored in the logger – leave empty for now
		System.out.println("not supported yet");
	}

	private void startAccountUpdater() {
		System.out.println("not supported yet");
	}

	public void store() throws BankStoreException {
		try (ObjectOutputStream out = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(dataFile)));) {
			out.writeObject(clients);
			System.out.println("client stored in: " + dataFile);
		} catch (IOException e) {
			// System.out.println(e.getMessage());
			// String causeMsg=e.getCause().getMessage();
			throw new BankStoreException("Bank store clients faild", e);
		}

	}

	public void load() throws BankException {
		try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dataFile)));) {
			clients = (List<Client>) in.readObject();
		} catch (EOFException e) {
			System.out.println("no previuos clients in file");
		} catch (IOException e) {
			throw new BankLoadException("The Bank could not load the stored clients", e);
		} catch (ClassNotFoundException e) {
			throw new BankLoadException("Class not found.", e);
		}
	}

	public Client getClient(int clientID) throws BankException {
		try {
			Client client = new ClientRegular(clientID, null, 0);
			client = clients.get(clients.indexOf(client));
			return client;

		} catch (IndexOutOfBoundsException e) {
			throw new BankException("client with ID" + clientID + " not found");
		}

	}

}
