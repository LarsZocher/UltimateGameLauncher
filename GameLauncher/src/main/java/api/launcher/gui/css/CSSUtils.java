package gui.css;

import javafx.scene.Parent;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class CSSUtils {
	
	public static void setCSS(Parent node, String path){
		node.getStylesheets().add(path);
	}
	public static void setCSS(Parent node, String path, String cssClass){
		setCSS(node, path);
		node.getStyleClass().add(cssClass);
	}
}
