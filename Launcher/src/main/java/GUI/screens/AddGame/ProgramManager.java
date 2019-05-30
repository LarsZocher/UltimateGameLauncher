package gui.screens.addgame;

import gui.Menu;
import gui.localization.Language;
import gui.screens.addgame.battlenet.EditBattleNETGame;
import gui.screens.addgame.steam.*;
import gui.screens.alert.Alert;
import gui.screens.alert.SimpleAlert;
import gui.screens.notification.*;
import gui.screens.misc.Callback;
import api.launcher.AppTypes;
import api.launcher.Application;
import api.launcher.battlenet.BattleNETGames;
import api.launcher.GameLauncher;
import api.launcher.steam.SteamApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class ProgramManager {
	
	private GameLauncher launcher;
	
	public ProgramManager(GameLauncher launcher) {
		this.launcher = launcher;
	}
	
	public void createNew(){
		try {
			FXMLLoader loader = new FXMLLoader(ProgramManager.class.getClassLoader().getResource("fxml/AddGame.fxml"));
			Parent root = loader.load();
			
			AddGameController controller = loader.getController();
			controller.init();
			controller.setCallback(new Callback<AppTypes>() {
				@Override
				public void onCallback(AppTypes type) {
					switch(type){
						case STEAM:{
							if(launcher.getSteam().getUsernames().size()==0){
								SimpleAlert sa = new SimpleAlert();
								sa.setTitle(Language.format(Menu.lang.getLanguage().AlertSteamUserAskCreateTitle));
								sa.setMessage(Language.format(Menu.lang.getLanguage().AlertSteamUserAskCreateMessage));
								sa.addOption(Language.format(Menu.lang.getLanguage().ButtonAdd), ButtonAlignment.RIGHT, new ButtonCallback() {
									@Override
									public void onClick() {
										try {
											FXMLLoader userLoader = new FXMLLoader(ProgramManager.class.getClassLoader().getResource("fxml/NewSteamUser.fxml"));
											Parent userRoot = userLoader.load();
											Alert userAlert = new Alert(Menu.root);
											
											NewSteamUserController userController = userLoader.getController();
											userController.init(userAlert, EditSteamUserMode.NEW);
											userController.setLauncher(launcher);
											userController.setCallback(() -> {
												newSteamGame();
											});
											
											userAlert.setContent((Region)userRoot);
											userAlert.setBackground(Menu.rootAnchor);
											userAlert.setBackgroundBlur(10);
											userAlert.setBackgroundColorAdjust(0, 0, -0.3, 0);
											userAlert.show();
										}catch(IOException e){
											e.printStackTrace();
										}
									}
								}, true, ButtonStyle.GHOST);
								sa.addOption(Language.format(Menu.lang.getLanguage().ButtonCancel), ButtonAlignment.RIGHT, new ButtonCallback() {
									@Override
									public void onClick() {
										createNew();
									}
								}, true, ButtonStyle.GHOST);
								
								Alert alert = new Alert(Menu.root);
								alert.setContent(sa);
								alert.setBackground(Menu.rootAnchor);
								alert.setBackgroundBlur(10);
								alert.setBackgroundColorAdjust(0, 0, -0.3, 0);
								alert.show();
								return;
							}
							newSteamGame();
							break;
						}
					}
				}
			});
			Alert alert = new Alert(Menu.root);
			alert.setContent((Region)root);
			alert.setBackground(Menu.rootAnchor);
			alert.setBackgroundBlur(10);
			alert.setBackgroundColorAdjust(0, 0, -0.3, 0);
			alert.show();
			controller.setAlert(alert);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void newSteamGame(){
		try {
			FXMLLoader loader = new FXMLLoader(EditSteamGame.class.getClassLoader().getResource("fxml/EditSteamGame3.fxml"));
			Parent root = loader.load();
			Alert alert = new Alert(Menu.root);
			
			EditSteamGameController controller = loader.getController();
			controller.init(alert);
			controller.setLauncher(launcher);
			controller.setOnContinue(new EditSteamGameCallback() {
				@Override
				public void onContinue(Application app) {
					launcher.getSteam().addApp(app);
					onSuccessfullyCreated(app.getContent(SteamApp.class));
				}
			});
			
			alert.setContent((Region)root);
			alert.setBackground(Menu.rootAnchor);
			alert.setBackgroundBlur(10);
			alert.setBackgroundColorAdjust(0, 0, -0.3, 0);
			alert.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editSteamGame(Application app){
		try {
			FXMLLoader loader = new FXMLLoader(EditSteamGame.class.getClassLoader().getResource("fxml/EditSteamGame3.fxml"));
			Parent root = loader.load();
			Alert alert = new Alert(Menu.root);
			
			EditSteamGameController controller = loader.getController();
			controller.init(alert);
			controller.setLauncher(launcher);
			controller.loadSteamApp(app);
			controller.setOnContinue(new EditSteamGameCallback() {
				@Override
				public void onContinue(Application app) {
					launcher.getSteam().addApp(app);
					onSuccessfullyCreated(app.getContent(SteamApp.class));
				}
			});
			
			alert.setContent((Region)root);
			alert.setBackground(Menu.rootAnchor);
			alert.setBackgroundBlur(10);
			alert.setBackgroundColorAdjust(0, 0, -0.3, 0);
			alert.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void editBattleNETGame(BattleNETGames game){
		EditBattleNETGame editBattleNETGame = new EditBattleNETGame(launcher);
		try {
			editBattleNETGame.start(new Stage());
		} catch(Exception e) {
			e.printStackTrace();
		}
		editBattleNETGame.loadApp(game);
	}
	
	public abstract void onSuccessfullyCreated(SteamApp app);
}
