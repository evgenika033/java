package bank.core;

public class Logger {
	private String driverName;

	public Logger(String driverName) {
		super();
		this.driverName = driverName;
		
	}
	//implement to print Log (�����)on screen
	public static void log(Log log) {
		System.out.println(log.getData());
	}
	
	public Log[] getLogs() {
		return null;
	}
}