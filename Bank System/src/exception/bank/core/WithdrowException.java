package exception.bank.core;

public class WithdrowException extends Exception{
	private int clientID;
	private float currentBalance;
	private float withdrawAmount;
	
	
	public WithdrowException(  String message,  int clientID, float currentBalance, float withdrawAmount) {
		super(message);
		this.clientID = clientID;
		this.currentBalance = currentBalance;
		this.withdrawAmount = withdrawAmount;
		
	}


	public int getClientID() {
		return clientID;
	}


	public float getCurrentBalance() {
		return currentBalance;
	}


	public float getWithdrawAmount() {
		return withdrawAmount;
	}
	
	
	

}
