package gui.css;

import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class CSSUtils {
	
	public static void setCSSSheet(Parent node, String path){
		node.getStylesheets().add(path);
	}
	public static void setCSSSheet(Parent node, String path, String... cssClasses){
		setCSSSheet(node, path);
		for(String cssClass : cssClasses) {
			node.getStyleClass().add(cssClass);
		}
	}
	public static void setCSS(Parent node, String... cssClasses){
		node.getStyleClass().setAll(cssClasses);
	}
	
	public static void addCSS(Parent node, String... cssClasses){
		for(String cssClass : cssClasses) {
			node.getStyleClass().add(cssClass);
		}
	}
	
	public static void addCustomCSSClass(Node node, String className){
		node.getStyleClass().add("CSSNAME-"+className);
	}
	public static void addCustomCSSClass(Node node, String... classNames){
		for(String name : classNames) {
			addCustomCSSClass(node, name);
		}
	}
	
	public static String getID(Node node){
		String id = "";
		for(String classes : node.getStyleClass()) {
			if(classes.contains("CSSNAME-")){
				id+=classes.replaceAll("CSSNAME-", "")+"-";
			}
		}
		return id.substring(0, id.length()-1);
	}
	public static String getID(Node node, CSSState state){
		return getID(node)+state.name().toLowerCase();
	}
}
