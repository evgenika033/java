package bank.core;

import java.util.Date;

public class Log {
	

	private Date timestamp;
	private int clientId;
	private String description;
	private float amount;

	public Log(Date timestamp, int clientId, String description, float amount) {
		super();
		this.timestamp = timestamp;
		this.clientId = clientId;
		this.description = description;
		this.amount = amount;
	}

	public Log(Date timestamp) {
		super();
		this.timestamp = timestamp;
	}

	public String getData() {
		return "Log [timestamp=" + timestamp + ", clientId=" + clientId+", description="
					+description+", amount="+amount+"]";
	}
	
	@Override
	public String toString() {
		return getData();
	}
}
