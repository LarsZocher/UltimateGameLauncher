package gui.screens;

import gui.Menu;
import gui.localization.Language;
import gui.screens.addgame.steam.NewSteamUser;
import gui.screens.misc.initMenuController;
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

public class steamAddUserController extends initMenuController {
	
	private steamController controller;
	
	@FXML
	private Label add;
	
	@Override
	public void init(Menu menu) {
		super.init(menu);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				add.setText(Language.format(Menu.lang.getLanguage().AddSteamUser));
			}
		});
	}
	
	public void setSteamController(steamController controller){
		this.controller = controller;
	}
	
	@Override
	public void onRefresh() {
	
	}
	
	@FXML
	void onClick() {
		try {
			NewSteamUser user = new NewSteamUser() {
				@Override
				public void onFinish() {
					controller.forceRefreshList();
				}
				
				@Override
				public void onCancel() {
				
				}
			};
			user.start(new Stage());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
