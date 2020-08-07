package job;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import dao.CouponsDao;

public class CouponExpirationDailyJob implements Runnable {

	private CouponsDao couponsDao;
	private boolean quit;

	public CouponExpirationDailyJob(CouponsDao couponsDao, boolean quit) {
		this.couponsDao = couponsDao;
		this.quit = quit;
	}

	public void stop() {

	}

	private Date getDate() {
		// get DateTime
		Calendar calendar = Calendar.getInstance();
		// set format of dateTime
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// set formated date
		String formatted = simpleDateFormat.format(calendar.getTime());
		// converting string into sql date
		Date date = Date.valueOf(formatted);
		System.out.println("job date: " + date);
		return date;

	}

	@Override
	public void run() {
		getDate();
	}

}
