package GUI.Utils;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class StringUtils {
	
	public static String format(String s) {
		return s.trim().replaceAll(" +", " ");
	}
	
	public static String removeChars(String s, String... remove){
		for(String toRemove:remove){
			s = s.replaceAll(toRemove, "");
		}
		return s;
	}
	
	public static boolean containsList(String s, String[] strings){
		for(String contains : strings){
			if(s.toLowerCase().contains(contains.toLowerCase()))
				return true;
		}
		return false;
	}
	
	public static String getContainedString(String s, String[] strings){
		for(String contains : strings){
			if(s.toLowerCase().contains(contains.toLowerCase()))
				return contains;
		}
		return "";
	}
}
