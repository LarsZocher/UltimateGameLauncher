package GUI.screens;

import GUI.Menu;
import GUI.css.CSSUtils;
import GUI.localization.Language;
import GUI.screens.AddGame.ProgramManager;
import GUI.screens.Notification.ButtonAlignment;
import GUI.screens.Notification.ButtonCallback;
import GUI.screens.Notification.ButtonOption;
import GUI.screens.Notification.NotificationIcon;
import GUI.screens.misc.*;
import api.GameLauncher.AppTypes;
import api.GameLauncher.Application;
import api.GameLauncher.BattleNET.BattleNETGames;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Origin.OriginGame;
import api.GameLauncher.Steam.SteamApp;
import api.GameLauncher.Steam.SteamUser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import java.io.File;
import java.util.*;


/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class gamesController extends initMenuController {
	
	@FXML
	private AnchorPane listPane;
	@FXML
	private Label title;
	@FXML
	private JFXButton add;
	@FXML
	private VBox all;
	@FXML
	private VBox steam;
	@FXML
	private VBox battleNET;
	@FXML
	private VBox origin;
	@FXML
	private VBox uplay;
	@FXML
	private JFXComboBox<String> sort;
	@FXML
	private JFXTextField search;
	private GridView<GameDisplay> list;
	
	private List<TabButton> buttons = new ArrayList<>();
	private GameLauncher launcher;
	private AppTypes currentAppType;
	private String currentFilter = "";
	private ProgramManager manager;
	private Thread loadingList;
	private Thread loadingListDisplays;
	private HashMap<AppTypes, Boolean> acceptNext = new HashMap<>();
	private Stage stage;
	private List<Application> current = new ArrayList<>();
	private ObservableList<GameDisplay> panes;
	private int cellWidth = 225;
	private int cellHeight = 102;
	private int index = 0;
	private boolean loading = false;
	private boolean firstLoading = true;
	
	@Override
	public void init(Menu menu) {
		super.init(menu);
		this.stage = menu.stage;
		
		this.launcher = new GameLauncher();
		this.manager = new ProgramManager(launcher) {
			@Override
			public void onSuccessfullyCreated(SteamApp app) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						loadList(currentAppType, "");
					}
				});
			}
		};
		menu.jsonConfig.load();
		menu.jsonConfig.setDefault("sort", SortStyle.ALPHABETICALLY.name());
		menu.jsonConfig.save();
		
		sort.getStyleClass().add("jfx-combo-box");
		
		panes = FXCollections.observableList(new ArrayList<>());
		list = new GridView<>(panes);
		list.setCellFactory(new Callback<GridView<GameDisplay>, GridCell<GameDisplay>>() {
			@Override
			public GridCell<GameDisplay> call(GridView<GameDisplay> arg0) {
				return new DisplayGridCell();
			}
		});
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AnchorPane.setTopAnchor(list, 0.0);
				AnchorPane.setBottomAnchor(list, 0.0);
				AnchorPane.setLeftAnchor(list, 0.0);
				AnchorPane.setRightAnchor(list, 0.0);
				
				listPane.getChildren().add(list);
				
				list.setCellWidth(cellWidth);
				list.setCellHeight(cellHeight);
				
				list.setHorizontalCellSpacing(5);
				list.setVerticalCellSpacing(5);
				
				add.setText(Language.format(Menu.lang.getLanguage().ButtonAddGame));
				title.setText(Language.format(Menu.lang.getLanguage().TitleGames));
				search.setPromptText(Language.format(Menu.lang.getLanguage().WindowTitleSearch));
				
				menu.jsonConfig.load();
				CSSUtils.setCSS(sort, "jfx-combo-box");
				for(SortStyle style : SortStyle.values()) {
					sort.getItems().add(style.getName());
				}
				sort.getSelectionModel().select(SortStyle.valueOf(menu.jsonConfig.getConfig().getString("sort")).getName());
				
				stage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						if(!search.isFocused()) {
							search.setText("");
							search.requestFocus();
							search.setText(event.getText());
							search.positionCaret(1);
						} else {
							if(event.getCode() == KeyCode.ENTER) {
								stage.getScene().getRoot().requestFocus();
								loadList(currentAppType, search.getText());
							} else {
								loadList(currentAppType, search.getText());
							}
						}
					}
				});
			}
		});
		
		buttons.add(new TabButton(all, Language.format(Menu.lang.getLanguage().GameUIAll)) {
			@Override
			public void onClick() {
				loadList(AppTypes.ALL, "");
				resetButtons();
				focus();
				search.setText("");
			}
		});
		buttons.add(new TabButton(steam, AppTypes.STEAM.getGUIName()) {
			@Override
			public void onClick() {
				loadList(AppTypes.STEAM, "");
				resetButtons();
				focus();
				search.setText("");
			}
		});
		buttons.add(new TabButton(battleNET, AppTypes.BATTLENET.getGUIName()) {
			@Override
			public void onClick() {
				loadList(AppTypes.BATTLENET, "");
				resetButtons();
				focus();
				search.setText("");
			}
		});
		buttons.add(new TabButton(origin, AppTypes.ORIGIN.getGUIName()) {
			@Override
			public void onClick() {
				loadList(AppTypes.ORIGIN, "");
				resetButtons();
				focus();
				search.setText("");
			}
		});
		buttons.add(new TabButton(uplay, AppTypes.UPLAY.getGUIName()) {
			@Override
			public void onClick() {
				loadList(AppTypes.UPLAY, "");
				resetButtons();
				focus();
				search.setText("");
			}
		});
		buttons.get(0).onClick();
		if(launcher.getBattleNET().isDisabled())
			buttons.get(2).disable();
		buttons.get(4).disable();
	}
	
	public void resetButtons() {
		for(TabButton button : buttons) {
			button.unfocus();
		}
	}
	
	public void loadList(AppTypes type, String filter) {
		loading = true;
		
		System.out.println("[Launcher] Loading games - Type: "+type.name()+"  Filter: \""+filter+"\"");
		for(AppTypes value : AppTypes.values()) {
			acceptNext.put(value, false);
		}
		acceptNext.put(type, true);
		
		if(loadingList != null && loadingList.isAlive()) {
			try {
				loadingList.stop();
			} catch(Exception e) {
			
			}
		}
		if(loadingListDisplays != null && loadingListDisplays.isAlive()) {
			try {
				loadingListDisplays.stop();
			} catch(Exception e) {
			
			}
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				panes.clear();
			}
		});
		
		Task task = new Task<Void>() {
			@Override
			public Void call() {
				try {
					Thread.sleep(50);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
				currentAppType = type;
				currentFilter = filter;
				switch(type) {
					case ALL:
					case STEAM:
					case BATTLENET:
					case ORIGIN: {
						List<Application> displays;
						if(type==AppTypes.ALL)
							displays = launcher.getApplications();
						else
							displays = launcher.getApplications(type);
						
						if(type == AppTypes.ALL || type == AppTypes.BATTLENET)
							displays = filterBattleNETGames(displays);
						displays = filterApps(displays, filter);
						
						switch(SortStyle.getByName(sort.getSelectionModel().getSelectedItem())) {
							case ALPHABETICALLY: {
								Collections.sort(displays, new Comparator<Application>() {
									@Override
									public int compare(Application o1, Application o2) {
										return getDisplayName(o1).toLowerCase().compareTo(getDisplayName(o2).toLowerCase());
									}
								});
								break;
							}
							case CREATION_DATE: {
								Collections.sort(displays, new Comparator<Application>() {
									@Override
									public int compare(Application o1, Application o2) {
										return Long.compare(o1.getCreated(), o2.getCreated());
									}
								});
								break;
							}
						}
						current = displays;
						break;
					}
				}
				
				List<GameDisplay> toAdd = new ArrayList<>();
				index = 0;
				
				loadingListDisplays = new Thread(new Task<Void>() {
					@Override
					protected Void call() {
						try {
							for(int i = 0; i < current.size(); i++) {
								toAdd.add(loadDisplay(current.get(i)));
							}
							Thread.sleep(100);
							loading = false;
							firstLoading = false;
						}catch(Exception e){
							e.printStackTrace();
						}
						return null;
					}
				});
				loadingListDisplays.start();
				
				while(loading){
					try {
						if(index<50)
							Thread.sleep(200);
						else
							Thread.sleep(2000);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							for(int i = index; i < toAdd.size(); i++) {
								index++;
								panes.add(toAdd.get(i));
							}
						}
					});
				}
				return null;
			}
		};
		loadingList = new Thread(task);
		loadingList.start();
	}
	
	public List<Application> filterBattleNETGames(List<Application> apps){
		if(launcher.getBattleNET().isDisabled()) {
			List<Application> filtered = new ArrayList<>();
			
			for(Application app : apps) {
				if(app.getType()!=AppTypes.BATTLENET){
					filtered.add(app);
				}
			}
			return filtered;
		}
		List<BattleNETGames> owned = launcher.getBattleNET().getSettings().getOwnedGames();
		
		games : for(BattleNETGames game : BattleNETGames.values()) {
			if(game.hasConfigName()){
				for(BattleNETGames ownedGame : owned) {
					if(game.getCode().equalsIgnoreCase(ownedGame.getCode())){
						continue games;
					}
				}
				int index = 0;
				for(int i = 0; i<apps.size(); i++){
					if(apps.get(i).getName().equalsIgnoreCase(game.getConfigName())){
						index = i;
						break;
					}
				}
				apps.remove(index);
			}
		}
		
		return apps;
	}
	
	public List<Application> filterApps(List<Application> apps, String filter){
		if(!filter.isEmpty()) {
			filter = filter.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
			List<String> chars = new ArrayList<>();
			for(char c : filter.toCharArray()) {
				chars.add(c+"");
			}
			List<Application> filtered = new ArrayList<>();
			game:
			for(Application display : apps) {
				String displayName = getDisplayName(display).toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
				int containsAll = 0;
				for(char c : displayName.toCharArray()) {
					if(containsAll>chars.size()-1){
						break;
					}
					if((c+"").equalsIgnoreCase(chars.get(containsAll))){
						containsAll++;
					}
				}
				if(containsAll==chars.size())
					filtered.add(display);
			}
			apps = filtered;
		}
		return apps;
	}
	
	public String getDisplayName(Application app) {
		switch(app.getType()) {
			case STEAM:
				return app.getContent(SteamApp.class).getName();
			case BATTLENET:
				return BattleNETGames.getByConfigName(app.getName()).getName();
			case ORIGIN:
				return launcher.getOrigin().getGame(app.getName()).getName();
		}
		return "";
	}
	
	public GameDisplay loadDisplay(Application app) {
		switch(app.getType()) {
			case STEAM: {
				SteamApp steamApp = launcher.getSteam().getApp(app.getName());
				SteamUser user = new SteamUser(launcher, steamApp.getUser());
				GameDisplay display = new GameDisplay(app.getDisplayName(), app, launcher.getImageManager().getHeaderURL(app), true, true, true) {
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
									loadList(currentAppType, "");
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
						manager.editSteamGame(app);
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
						if(steamApp.hasAlreadyAShortcut()) {
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
						} else {
							steamApp.createShortcutOnDesktop(launcher);
							note.show();
						}
					}
					
					@Override
					public void onRun() {
						launcher.getSteam().launch(steamApp);
					}
					
				};
				if(!user.exists()){
					if(launcher.getSteam().getUsernames().size()>0){
						SteamUser newUser = launcher.getSteam().getUser(launcher.getSteam().getUsernames().get(0));
						display.setUser(newUser);
						System.out.println("[Steam] "+app.getName()+": user "+user.getUsername() + " not found! Setting user to: "+newUser.getUsername());
						SteamApp newApp = app.getContent(SteamApp.class);
						newApp.setUser(newUser.getUsername());
						launcher.getSteam().addApp(newApp);
					}
				}else
					display.setUser(user);
				return display;
			}
			case BATTLENET: {
				BattleNETGames battleNETGames = BattleNETGames.getByConfigName(app.getName());
				GameDisplay display = new GameDisplay(battleNETGames.getName(), app, launcher.getImageManager().getHeaderURL(app), true, true, false) {
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
						if(launcher.getBattleNET().hasAlreadyAShortcut(battleNETGames)) {
							GUI.screens.Notification.Notification note2 = new GUI.screens.Notification.Notification();
							note2.setText("Es wurde bereits eine Verknüpfung von diesem Spiel gefunden! Möchten Sie diese ersetzten?");
							note2.setTitle("Warnung");
							note2.setIcon(NotificationIcon.QUESTION);
							note2.addOption(ButtonOption.YES, ButtonAlignment.RIGHT, new ButtonCallback() {
								@Override
								public void onClick() {
									launcher.getBattleNET().replaceShortcut(launcher, battleNETGames);
									note.show();
								}
							});
							note2.addOption(ButtonOption.NO, ButtonAlignment.RIGHT, new ButtonCallback() {
								@Override
								public void onClick() {
									launcher.getBattleNET().createShortcutOnDesktop(launcher, battleNETGames);
									note.show();
								}
							});
							note2.show();
						} else {
							launcher.getBattleNET().createShortcutOnDesktop(launcher, battleNETGames);
							note.show();
						}
					}
					
					@Override
					public void onEdit() {
						manager.editBattleNETGame(battleNETGames);
					}
					
					@Override
					public void onDelete() {
					
					}
					
					@Override
					public void onRun() {
						launcher.getBattleNET().launchWithUserSelection(battleNETGames, false);
					}
				};
				return display;
			}
			case ORIGIN: {
				OriginGame originGame = launcher.getOrigin().getGame(app.getName());
				GameDisplay display = new GameDisplay(originGame.getName(), app, launcher.getImageManager().getHeaderURL(app), true, true, false) {
					@Override
					public void onLink() {
					
					}
					
					@Override
					public void onEdit() {
					
					}
					
					@Override
					public void onDelete() {
					
					}
					
					@Override
					public void onRun() {
						launcher.getOrigin().launch(originGame);
					}
				};
				return display;
			}
		}
		return null;
	}
	
	@Override
	public void onShow() {
	
	}
	
	@Override
	public void onRefresh() {
	
	}
	
	@FXML
	void onAdd() {
		this.manager.createNew();
	}
	
	@FXML
	void onSortChange() {
		menu.jsonConfig.load();
		menu.jsonConfig.getConfig().put("sort", SortStyle.getByName(sort.getSelectionModel().getSelectedItem()).name());
		menu.jsonConfig.save();
		if(!firstLoading)
			loadList(currentAppType, currentFilter);
	}
}
