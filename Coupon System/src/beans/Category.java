package beans;

import java.util.Arrays;

public enum Category {

	ELECTRIC, ACCESSORIES, HOME, KITCHEN, ELECTRONICS, HEALTH, BEAUTY, BABY, TOYS, GAMES, AUTOMOTIVE, TRAVEL, MOVIES,
	MUSIC, MEDIA, SPORT, FOOD, DEFAULT;

	public static int getCategoryID(Category category) {
		return Arrays.asList(Category.values()).indexOf(category);

	}

}
