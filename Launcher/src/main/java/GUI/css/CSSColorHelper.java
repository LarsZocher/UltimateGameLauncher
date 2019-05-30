package gui.css;

import gui.Menu;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.converters.ColorConverter;
import com.sun.javafx.css.parser.CSSParser;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class CSSColorHelper {
	
	public static Color getColorFromCSS(String stylesheet, String colorName){
		CssToColorHelper helper = new CssToColorHelper();
		helper.getStylesheets().add(stylesheet);
		helper.setStyle("-named-color: " + colorName + ";");
		helper.applyCss();
		
		System.out.println(helper.getStyle());
		System.out.println("---");
		System.out.println(helper.getNamedColor());
		System.out.println("---");
		return helper.getNamedColor();
	}
	
	public static Color parseColor(String property) {
		CSSParser parser = new CSSParser();
		try {
			URL url = new URL(Menu.getStyleSheet());
			Stylesheet css = parser.parse(url);
			final Rule rootRule = css.getRules().get(0); // .root
			return (Color) rootRule.getDeclarations().stream()
					.filter(d -> d.getProperty().equals(property))
					.findFirst()
					.map(d -> ColorConverter.getInstance().convert(d.getParsedValue(), null))
					.get();
		} catch (IOException ex) { }
		return Color.WHITE;
	}
}
