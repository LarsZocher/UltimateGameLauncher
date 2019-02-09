package GUI.screens.AddGame.Steam;

import GUI.Menu;
import GUI.localization.Language;
import api.GameLauncher.GameLauncher;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class EditSteamUserController {
	
	@FXML
	private JFXTextField username;
	@FXML
	private JFXPasswordField password;
	@FXML
	private JFXTextField id;
	@FXML
	private Label error;
	@FXML
	private Label title;
	@FXML
	private Label usernameLabel;
	@FXML
	private Label passwordLabel;
	@FXML
	private JFXButton start;
	
	private EditSteamUser editSteamUser;
	private Stage stage;
	
	public void setEditSteamUser(EditSteamUser editSteamUser){
		this.editSteamUser = editSteamUser;
	}
	
	
	public void init(Stage stage) {
		this.stage = stage;
		error.setVisible(false);
		
		GameLauncher launcher = new GameLauncher();
		username.setText(launcher.getSteam().getUser(editSteamUser.editUser).getUsername());
		password.setText(launcher.getSteam().getUser(editSteamUser.editUser).getPassword());
		id.setText(launcher.getSteam().getUser(editSteamUser.editUser).getID());
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				title.setText(Language.format(Menu.lang.getLanguage().WindowTitleEditSteamUser));
				usernameLabel.setText(Language.format(Menu.lang.getLanguage().Username));
				passwordLabel.setText(Language.format(Menu.lang.getLanguage().Password));
				start.setText(Language.format(Menu.lang.getLanguage().ButtonFinish));
			}
		});
	}
	
	@FXML
	void onFinish(){
		if(username.getText().isEmpty()||password.getText().isEmpty()||id.getText().isEmpty()){
			error.setText(Language.format(Menu.lang.getLanguage().ErrorFillAllFields));
			error.setVisible(true);
			return;
		}
		GameLauncher launcher = new GameLauncher();
		launcher.steam.deleteUser(editSteamUser.editUser);
		launcher.steam.createUser(username.getText(), password.getText(), id.getText());
		stage.close();
		editSteamUser.onFinish();
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
