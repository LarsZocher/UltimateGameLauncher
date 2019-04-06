package GUI.screens.AddGame;

import GUI.screens.AddGame.BattleNET.EditBattleNETGame;
import GUI.screens.AddGame.Steam.EditSteamGame;
import GUI.screens.AddGame.Steam.NewSteamUser;
import GUI.screens.Notification.*;
import api.GameLauncher.AppTypes;
import api.GameLauncher.Application;
import api.GameLauncher.BattleNET.BattleNETGames;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Steam.SteamApp;
import javafx.stage.Stage;

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
		AddGame addGame = new AddGame() {
			@Override
			public void onContinue(AppTypes type) {
				switch(type){
					case STEAM:{
						if(launcher.getSteam().getUsernames().size()==0){
							Notification noUserFound = new Notification();
							noUserFound.setIcon(NotificationIcon.ERROR);
							noUserFound.setTitle("Steam");
							noUserFound.setText("Um ein Steam Spiel hinzufügen zu können müssen Sie erst ein Steam Benutzer hinzufügen!\n\nMöchten Sie jetzt einen Benutzer hinzufügen?");
							noUserFound.addOption(ButtonOption.ADD, ButtonAlignment.RIGHT, new ButtonCallback() {
								@Override
								public void onClick() {
									NewSteamUser user = new NewSteamUser() {
										@Override
										public void onFinish() {
											newSteamGame();
										}
										
										@Override
										public void onCancel() {
											createNew();
										}
									};
									try {
										user.start(new Stage());
									} catch(Exception e) {
										e.printStackTrace();
									}
								}
							}, true);
							noUserFound.addOption(ButtonOption.CANCEL, ButtonAlignment.RIGHT, new ButtonCallback() {
								@Override
								public void onClick() {
									createNew();
								}
							}, true);
							noUserFound.show();
							return;
						}
						newSteamGame();
						break;
					}
					case BATTLENET:{
						
						break;
					}
				}
			}
		};
		try {
			addGame.start(new Stage());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void newSteamGame(){
		EditSteamGame editSteamGame = new EditSteamGame(launcher) {
			@Override
			public void onContinue(Application app) {
				launcher.getSteam().addApp(app);
				onSuccessfullyCreated(app.getContent(SteamApp.class));
			}
		};
		try {
			editSteamGame.start(new Stage());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void editSteamGame(Application app){
		EditSteamGame editSteamGame = new EditSteamGame(launcher) {
			@Override
			public void onContinue(Application app) {
				launcher.getSteam().addApp(app);
				onSuccessfullyCreated(app.getContent(SteamApp.class));
			}
		};
		try {
			editSteamGame.start(new Stage());
		} catch(Exception e) {
			e.printStackTrace();
		}
		editSteamGame.loadSteamApp(app);
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
