package bank.core;

public class ClientRegular extends Client {

	public ClientRegular(int id, String name, float balance) {
		super(id, name, balance);
		this.commissionRate=0.03f;
		this.interestRate=0.001f;
	}
	
	@Override
	public String toString() {
		return "ClientGold [id =" + getId() + "]";
	}
}
