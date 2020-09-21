package coupon.core.entity;

import java.util.Arrays;

public enum Category {
	ELECTRIC, ACCESSORIES, HOME, KITCHEN, ELECTRONICS, HEALTH, BEAUTY, BABY, TOYS, GAMES, AUTOMOTIVE, TRAVEL, MOVIES,
	MUSIC, MEDIA, SPORT, FOOD, DEFAULT;

	/**
	 * get category list
	 * 
	 * @param category
	 * @return
	 */
	public static int getCategoryID(Category category) {
		return Arrays.asList(Category.values()).indexOf(category);

	}

	/**
	 * method for fill database(needed ID)
	 * 
	 * @param id
	 * @return
	 */
	public static Category category(int id) {
		switch (id) {
		case 1001:
			return Category.ACCESSORIES;
		case 1002:
			return Category.AUTOMOTIVE;
		case 1003:
			return Category.BABY;
		case 1004:
			return Category.BEAUTY;
		case 1005:
			return Category.DEFAULT;
		case 1006:
			return Category.ELECTRIC;
		case 1007:
			return Category.ELECTRONICS;
		case 1008:
			return Category.FOOD;
		case 1009:
			return Category.GAMES;
		case 1010:
			return Category.HEALTH;
		case 1011:
			return Category.HOME;
		case 1012:
			return Category.KITCHEN;
		case 1013:
			return Category.MEDIA;
		case 1014:
			return Category.MOVIES;
		case 1015:
			return Category.MUSIC;
		case 1016:
			return Category.SPORT;
		case 1017:
			return Category.TOYS;
		case 1018:
			return Category.TRAVEL;

		}
		return Category.DEFAULT;
	}

}
