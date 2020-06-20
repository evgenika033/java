package bank.core;

import java.util.ArrayList;
import java.util.List;

public class Logger {
	private String driverName;

	public Logger(String driverName) {
		super();
		this.driverName = driverName;
		
	}
	//implement to print Log (פעולה)on screen
	public static void log(Log log) {
		System.out.println(log.getData());
	}
	
	public List<Log> getLogs() {
		List<Log> list= new ArrayList<>();
		return list;
	}
}
