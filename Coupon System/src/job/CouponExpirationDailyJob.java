package job;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import beans.Coupon;
import configuration.PropertiesController;
import dao.CouponsDao;
import exceptions.DaoException;
import exceptions.PropertiesExceptions;
import utils.StringHelper;

public class CouponExpirationDailyJob implements Runnable {

	private CouponsDao couponsDao;
	private boolean quit;
	private int jobInterval;

	public CouponExpirationDailyJob(CouponsDao couponsDao, boolean quit) {
		init();
		this.couponsDao = couponsDao;
		this.quit = quit;
	}

	public void stop() {

	}

	private void init() {
		jobInterval = Integer.valueOf(PropertiesController.getProperties().getProperty(StringHelper.JOB_COUPON_EXPIRATION_INTERVAL))
				* 60000;
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
		while (!quit) {
			try {
				job();
				Thread.sleep(jobInterval);
			} catch (InterruptedException | PropertiesExceptions | DaoException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * delete old coupons and history
	 * 
	 * @throws PropertiesExceptions
	 * @throws DaoException
	 */
	private void job() throws PropertiesExceptions, DaoException {
		// run if job date last run from configuration file != today
		if (!PropertiesController.getProperties().getProperty(StringHelper.JOB_COUPON_EXPIRATION).equals(getDate().toString())) {
			List<Coupon> listCoupons = couponsDao.get(getDate());
			System.out.println("-------delete job execute");
			// get old coupons
			for (Coupon coupon : listCoupons) {
				// delete old coupon with history
				couponsDao.delete(coupon.getID());
			}
			System.out.println("------delete job done");
			// save last run to configuration file
			PropertiesController.write(StringHelper.JOB_COUPON_EXPIRATION, getDate().toString());
		} else {
			System.out.println("job not need to execute");
		}
	}

}
