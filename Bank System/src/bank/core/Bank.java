package bank.core;

public class Bank {
	private Client[] clients = new Client[100];
	private Logger logService;
	private String accountUpdater;
	private Logger logger= new Logger(null);
	private float balance;

	public Bank() {
		// An empty constructor that instantiates the clients array and logService.
		super();
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
		for (int i = 0; i < clients.length; i++) {
			if (clients[i] != null) {
				totalFortune += clients[i].getFortune();

			}
		}
		return totalFortune;
	}

	public void addClient(Client client) {
		// add the client to the array and log the operation.
		// You should seek the array and place the Client where the first null value is
		// found.
		for (int i = 0; i < clients.length; i++) {
			if (clients[i] == null) {
				clients[i] = client;
				long timeStamp = System.currentTimeMillis();
				String description = "addClient";
				float amount = client.getFortune();
				Log log = new Log(timeStamp, client.getId(), description, amount);
				logger.log(log);
				break;
			}
		}
	}

	public void removeClient(int id) {
		// remove the client with the same id from the array (by assigning a 'null'
		// value to the array[position]). Log the operation.
		for (int i = 0; i < clients.length; i++) {
			if (clients[i] != null && clients[i].getId() == id) {
				Client client = clients[i];
				clients[i] = null;
				long timeStamp = System.currentTimeMillis();
				String description = "removeClient";
				float amount = client.getFortune();
				Log log = new Log(timeStamp, client.getId(), description, amount);
				logger.log(log);
				break;
			}
		}
	}

	public Client[] getClients() {
		int numberOfClients = 0;
		for (int i = 0; i < clients.length; i++) {
			if (clients[i] != null) {
				numberOfClients++;
			}

		}

		Client[] clients = new Client[numberOfClients];
		int index = 0;
		for (int i = 0; i < this.clients.length; i++) {
			if (this.clients[i] != null) {
				clients[index++] = this.clients[i];
			}
		}
		return clients;
	}

	public void viewLogs() {
		// prints all logs that are stored in the logger – leave empty for now
		System.out.println("not supported yet");
	}

	private void startAccountUpdater() {
		System.out.println("not supported yet");
	}

}
