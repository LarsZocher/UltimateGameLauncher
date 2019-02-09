package GUI.screens;

import GUI.Menu;
import GUI.Utils.ConfigFile;
import GUI.css.CSSUtils;
import GUI.screens.AddGame.ProgramManager;
import GUI.screens.Notification.ButtonAlignment;
import GUI.screens.Notification.ButtonCallback;
import GUI.screens.Notification.ButtonOption;
import GUI.screens.Notification.NotificationIcon;
import GUI.screens.misc.*;
import GUI.screens.misc.initMenuController;
import api.GameLauncher.AppTypes;
import api.GameLauncher.BattleNET.BattleNETGames;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Steam.SteamApp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class gamesController extends initMenuController {
	
	@FXML
	private JFXListView<HBox> list;
	@FXML
	private JFXButton add;
	@FXML
	private VBox steam;
	@FXML
	private VBox battleNET;
	@FXML
	private JFXComboBox<String> sort;
	
	private ConfigFile pluginInfo;
	private List<TabButton> buttons = new ArrayList<>();
	private GameLauncher launcher;
	private AppTypes currentAppType;
	private ProgramManager manager;
	private Thread loadingList;
	
	@Override
	public void init(Menu menu) {
		super.init(menu);
		
		this.launcher = new GameLauncher();
		this.manager = new ProgramManager(launcher) {
			@Override
			public void onSuccessfullyCreated(SteamApp app) {
				loadList(currentAppType);
			}
		};
		
		ConfigFile cfg = menu.config;
		cfg.load();
		cfg.setDefault("Games.Sort", SortStyle.ALPHABETICALLY.name());
		cfg.save();
		
		cfg.load();
		
		list.getStyleClass().add("mylistview");
		sort.getStyleClass().add("jfx-combo-box");
		
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cfg.load();
				CSSUtils.setCSS(sort, "jfx-combo-box");
				for(SortStyle style : SortStyle.values()) {
					sort.getItems().add(style.getName());
				}
				sort.getSelectionModel().select(SortStyle.valueOf(cfg.getString("Games.Sort")).getName());
			}
		});
		
		buttons.add(new TabButton(steam, AppTypes.STEAM.getGUIName()) {
			@Override
			public void onClick() {
				loadList(AppTypes.STEAM);
				resetButtons();
				focus();
			}
		});
		buttons.add(new TabButton(battleNET, AppTypes.BATTLENET.getGUIName()) {
			@Override
			public void onClick() {
				loadList(AppTypes.BATTLENET);
				resetButtons();
				focus();
			}
		});
		
		buttons.get(0).onClick();
		
	}
	
	public void resetButtons() {
		for(TabButton button : buttons) {
			button.unfocus();
		}
	}
	
	public void loadList(AppTypes type){
		if(loadingList!=null&&loadingList.isAlive()){
			try {
				loadingList.stop();
			}catch(Exception e){
			
			}
		}
		list.getItems().clear();
		list.setExpanded(true);
		Task task = new Task<Void>() {
			@Override public Void call() {
				currentAppType = type;
				switch(type){
					case STEAM:{
						List<String> apps = launcher.getSteam().getApps();
						switch(SortStyle.getByName(sort.getSelectionModel().getSelectedItem())){
							case ALPHABETICALLY:{
								Collections.sort(apps);
							}
							case CREATION_DATE:{
							
							}
						}
						for(int i = 0; i<apps.size();i++){
							SteamApp steamApp = launcher.getSteam().getApp(apps.get(i));
							SteamApplication app = new SteamApplication(list, steamApp.getName(), steamApp.getIconPath()) {
								@Override
								public void onDelete() {
									try {
										GUI.screens.Notification.Notification note = new GUI.screens.Notification.Notification();
										note.setText("Sind Sie sicher, dass Sie diese Spiele-Verknüpfung löschen möchten?");
										note.setTitle("Warnung");
										note.setIcon(NotificationIcon.QUESTION);
										note.addOption(ButtonOption.DELETE, ButtonAlignment.RIGHT, new ButtonCallback() {
											@Override
											public void onClick() {
												launcher.getSteam().removeApp(steamApp.getConfigName());
												loadList(currentAppType);
											}
										});
										note.addOption(ButtonOption.CANCEL, ButtonAlignment.RIGHT, new ButtonCallback() {
											@Override
											public void onClick() {
											
											}
										});
										note.show();
									} catch(Exception e) {
										e.printStackTrace();
									}
								}
								
								@Override
								public void onEdit() {
									manager.editSteamGame(steamApp);
								}
								
								@Override
								public void onLink() {
									GUI.screens.Notification.Notification note = new GUI.screens.Notification.Notification();
									note.setText("Die Desktopverknüpfung wurde erfolgreich erstellt!");
									note.setTitle("Information");
									note.setIcon(NotificationIcon.INFO);
									note.addOption(ButtonOption.OK, ButtonAlignment.RIGHT, new ButtonCallback() {
										@Override
										public void onClick() {
										
										}
									});
									if(steamApp.hasAlreadyAShortcut()){
										GUI.screens.Notification.Notification note2 = new GUI.screens.Notification.Notification();
										note2.setText("Es wurde bereits eine Verknüpfung von diesem Spiel gefunden! Möchten Sie diese ersetzten?");
										note2.setTitle("Warnung");
										note2.setIcon(NotificationIcon.QUESTION);
										note2.addOption(ButtonOption.YES, ButtonAlignment.RIGHT, new ButtonCallback() {
											@Override
											public void onClick() {
												steamApp.replaceShortcut(steamApp.getOldShortcutFile().getPath(), launcher);
												note.show();
											}
										});
										note2.addOption(ButtonOption.NO, ButtonAlignment.RIGHT, new ButtonCallback() {
											@Override
											public void onClick() {
												steamApp.createShortcutOnDesktop(launcher);
												note.show();
											}
										});
										note2.show();
									}else{
										steamApp.createShortcutOnDesktop(launcher);
										note.show();
									}
								}
								
								@Override
								public void onRun() {
									launcher.getSteam().launch(steamApp);
								}
							};
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									list.getItems().add(app.getBox());
								}
							});
						}
						break;
					}
					case BATTLENET:{
						List<String> names = new ArrayList<>();
						for(BattleNETGames games : BattleNETGames.values()) {
							names.add(games.getName());
						}
						Collections.sort(names);
						
						for(String game : names){
							BattleNETGames battleNETGames = BattleNETGames.getByName(game);
							BattleNETApplication battleNETApplication = new BattleNETApplication(game, "icon/BattleNET/"+battleNETGames.getIconFile()) {
								@Override
								public void onEdit() {
									manager.editBattleNETGame(battleNETGames);
								}
								
								@Override
								public void onLink() {
								
								}
								
								@Override
								public void onRun() {
									launcher.getBattleNET().launch(battleNETGames);
								}
							};
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									list.getItems().add(battleNETApplication.getBox());
								}
							});
						}
						break;
					}
				}
				return null;
			}
		};
		loadingList = new Thread(task);
		loadingList.start();
	}
	
	@Override
	public void onRefresh() {
	
	}
	
	@FXML
	void onAdd(){
		this.manager.createNew();
	}
	
	@FXML
	void onSortChange(){
		ConfigFile cfg = menu.config;
		cfg.load();
		cfg.set("Games.Sort", SortStyle.getByName(sort.getSelectionModel().getSelectedItem()).name());
		cfg.save();
		loadList(currentAppType);
	}
}
