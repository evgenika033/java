package bank;

public class Bank {
	private Client[] clients = new Client[100];
	private Logger logService;
	private String accountUpdater;
	private Logger logger;

	public Bank() {
		// An empty constructor that instantiates the clients array and logService.
		super();
	}

	public void setBalance() {
		/*
		 * this operation returns the bank balance. The balance is calculated by summing
		 * the total clients balance and the total accounts balance – you should use
		 * Client.getFortune() method of each client.
		 */
	}
	public float getBalance() {
		//float – just returns bank current value.
		return 0;
	}
	public void addClient(Client client) {
		//add the client to the array and log the operation.
		//You should seek the array and place the Client where the first null value is found.
	}
	public void removeClient(int id) {
	//remove the client with the same id from the array (by assigning a 'null' value to the array[position]). Log the operation.	
		
	}
	public Client[] getClients() {
		return clients;
	}
	public void viewLogs() {
		//prints all logs that are stored in the logger – leave empty for now
	}
	private void startAccountUpdater() {
		
	}
	
	
}
