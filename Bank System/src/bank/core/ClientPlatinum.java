package bank.core;

public class ClientPlatinum extends Client {

	public ClientPlatinum(int id, String name, float balance) {
		super(id, name, balance);
		this.commissionRate=0.01f;
		this.interestRate=0.005f;
	}
	
	@Override
	public String toString() {
		return "ClientGold [id =" + getId() + "]";
	}
}


