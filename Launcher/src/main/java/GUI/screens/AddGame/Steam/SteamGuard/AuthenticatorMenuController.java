package GUI.screens.AddGame.Steam.SteamGuard;

import GUI.Menu;
import GUI.localization.Language;
import GUI.menuController;
import GUI.screens.misc.initMenuController;
import api.GameLauncher.GameLauncher;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class AuthenticatorMenuController extends AuthenticatorInit{
	
	@FXML
	private BorderPane borderpane;
	
	private AuthenticatorMenu am;
	private Stage stage;
	
	public void setAuthenticatorMenu(AuthenticatorMenu am){
		this.am = am;
	}
	
	
	public void init(Stage stage) {
		this.stage = stage;
		
	}
	
	private HashMap<String, Parent> roots = new HashMap<>();
	public HashMap<String, AuthenticatorInit> controllers = new HashMap<>();
	
	public void loadFXML(String name) {
		loadFXML(name, true);
	}
	
	public void loadFXML(String name, boolean save) {
		try {
			Parent root;
			AuthenticatorInit controller = null;
			if(roots.containsKey(name.toLowerCase())) {
				root = roots.get(name.toLowerCase());
				controller = controllers.get(name.toLowerCase());
			} else {
				FXMLLoader loader = new FXMLLoader(menuController.class.getClassLoader().getResource("fxml/" + name + ".fxml"));
				root = loader.load();
				try {
					controller = loader.getController();
					if(save)
						controllers.put(name.toLowerCase(), controller);
					controller.init(am);
					System.out.println("[FXML] Loaded FXML " + name + " - " + controller.getClass().getName());
				} catch(NullPointerException e) {
					e.printStackTrace();
				}
				if(save)
					roots.put(name.toLowerCase(), root);
			}
			System.out.println("[FXML] Switching scene to " + name);
			borderpane.setCenter(root);
			for(AuthenticatorInit value : controllers.values()) {
				value.setCurrentlyShown(false);
			}
			controller.setCurrentlyShown(true);
			controller.onShow();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void onExit() {
		stage.close();
	}
	@FXML
	void onMinimize() {
		stage.setIconified(true);
	}
}
