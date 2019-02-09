package GUI.screens.misc;

import GUI.Menu;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author CodingAir
 * @verions: 1.0.0
 **/

public enum SortStyle {
	
	ALPHABETICALLY,
	CREATION_DATE;
	
	SortStyle() {
	
	}
	
	public String getName() {
		switch(this){
			case CREATION_DATE:
				return Menu.lang.getLanguage().CreationDate;
			case ALPHABETICALLY:
				return Menu.lang.getLanguage().Alphabetically;
		}
		return Menu.lang.getLanguage().Alphabetically;
	}
	
	public static SortStyle getByName(String name){
		for(SortStyle styles : SortStyle.values()) {
			if(styles.getName().equalsIgnoreCase(name))
				return styles;
		}
		return ALPHABETICALLY;
	}
}
