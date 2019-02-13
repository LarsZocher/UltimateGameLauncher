package GUI;

import GUI.localization.Language;
import GUI.screens.misc.MenuButton;
import GUI.screens.misc.PluginEntry;
import GUI.screens.misc.initMenuController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class menuController extends initMenuController {
	
	@FXML
	private Pane bar;
	@FXML
	private HBox settings;
	@FXML
	private HBox steam;
	@FXML
	private HBox games;
	@FXML
	private HBox infos;
	@FXML
	private BorderPane borderpane;
	
	
	private ArrayList<MenuButton> buttons;
	
	@Override
	public void init(Menu menu) {
		super.init(menu);
		buttons = new ArrayList<>();
//		buttons.add(new MenuButton(start, "start") {
//			@Override
//			public void onClick() {
//				loadFXML("start");
//			}
//		});
//		buttons.add(new MenuButton(console, "console") {
//			@Override
//			public void onClick() {
//				loadFXML("console");
//			}
//		});
//		buttons.add(new MenuButton(settings, "settings") {
//			@Override
//			public void onClick() {
//				loadFXML("settings");
//			}
//		});
//		buttons.add(new MenuButton(plugins, "plugins") {
//			@Override
//			public void onClick() {
//				loadFXML("plugins");
//			}
//		});
		buttons.add(new MenuButton(games, "games") {
			@Override
			public void onClick() {
				loadFXML("games");
			}
		});
		buttons.add(new MenuButton(steam, "steamN") {
			@Override
			public void onClick() {
				loadFXML("steamN");
			}
		});
		buttons.add(new MenuButton(settings, "steamN") {
			@Override
			public void onClick() {
				loadFXML("steamN");
			}
		});
		buttons.add(new MenuButton(infos, "infos") {
			@Override
			public void onClick() {
				loadFXML("infos");
			}
		});
		//preloadFXML("steamN");
		buttons.get(0).onClick();
		
		bar.getStylesheets().add(Menu.styleSheet);
		bar.setId("bar");
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				((Label)games.getChildren().get(2)).setText(Language.format(Menu.lang.getLanguage().MenuGames));
			}
		});
	}
	
	@Override
	public void onRefresh() {
	
	}
	
	@FXML
	void onExit() {
		System.exit(0);
	}
	
	@FXML
	void onMinimize() {
		menu.stage.setIconified(true);
	}
	
	private void resetButtons() {
		for(MenuButton button : buttons) {
			button.unfocus();
		}
	}
	
	private MenuButton getButtonByName(String name) {
		for(MenuButton button : buttons) {
			if(button.getFXMLname().equalsIgnoreCase(name)) {
				return button;
			}
		}
		return buttons.get(0);
	}
	
	private HashMap<String, Parent> roots = new HashMap<>();
	public HashMap<String, initMenuController> controllers = new HashMap<>();
	
	public void loadFXML(String name) {
		try {
			Parent root;
			if(roots.containsKey(name.toLowerCase())) {
				root = roots.get(name.toLowerCase());
			} else {
				FXMLLoader loader = new FXMLLoader(menuController.class.getClassLoader().getResource("fxml/" + name + ".fxml"));
				root = loader.load();
				try {
					initMenuController controller = loader.getController();
					controllers.put(name.toLowerCase(), controller);
					controller.init(menu);
					System.out.println("[FXML] Loaded FXML " + name + " - " + controller.getClass().getName());
				} catch(NullPointerException e) {
					e.printStackTrace();
				}
				roots.put(name.toLowerCase(), root);
			}
			System.out.println("[FXML] Switching scene to " + name);
			borderpane.setCenter(root);
			resetButtons();
			getButtonByName(name).focus();
			for(initMenuController value : controllers.values()) {
				value.setCurrentlyShown(false);
			}
			controllers.get(name.toLowerCase()).setCurrentlyShown(true);
			controllers.get(name.toLowerCase()).onShow();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void preloadFXML(String name) {
		Task task = new Task() {
			@Override
			protected Object call() throws Exception {
				try {
					Parent root;
					if(roots.containsKey(name.toLowerCase())) {
						root = roots.get(name.toLowerCase());
					} else {
						FXMLLoader loader = new FXMLLoader(menuController.class.getClassLoader().getResource("fxml/" + name + ".fxml"));
						root = loader.load();
						try {
							initMenuController controller = loader.getController();
							controller.init(menu);
							controllers.put(name, controller);
							
							System.out.println("[FXML] Preloaded FXML " + name + " - " + controller.getClass().getName());
						} catch(NullPointerException e) {
							e.printStackTrace();
						}
						roots.put(name.toLowerCase(), root);
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		new Thread(task).start();
	}
}
