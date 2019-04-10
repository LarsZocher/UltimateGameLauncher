package GUI.screens;

import GUI.Menu;
import GUI.localization.Language;
import GUI.screens.AddGame.ProgramManager;
import GUI.screens.AddGame.Steam.NewSteamUser;
import GUI.screens.AddGame.Steam.NewSteamUserController;
import GUI.screens.Alert.Alert;
import GUI.screens.misc.SteamMobileConfirmation;
import GUI.screens.misc.initMenuController;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Steam.SteamUser;
import codebehind.steam.mobileauthentication.SteamGuardAccount;
import codebehind.steam.mobileauthentication.TimeAligner;
import codebehind.steam.mobileauthentication.model.Confirmation;
import com.jfoenix.controls.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class steamController extends initMenuController {
	
	@FXML
	private HBox users;
	@FXML
	private JFXButton refresh;
	@FXML
	private Label title;
	@FXML
	private Label code;
	@FXML
	private Label code_user;
	@FXML
	private JFXTextField steamPath;
	@FXML
	private JFXToggleButton devMode;
	@FXML
	private VBox confirmations;
	@FXML
	private Pane time;
	@FXML
	private ScrollPane usersScroll;
	
	private List<String> lastUser = new ArrayList<>();
	private GameLauncher launcher = new GameLauncher();
	private ArrayList<steamUserController> controller = new ArrayList<>();
	private ArrayList<confirmationController> confController = new ArrayList<>();
	private int userCount = 0;
	private SteamUser currentUser;
	private long lastRefresh = 0;
	
	int pos = 0;
	final int minPos = 0;
	final int maxPos = 100;
	
	@Override
	public void init(Menu menu) {
		super.init(menu);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				title.setText(Language.format(Menu.lang.getLanguage().TitleSteamUser));
				refresh.setText(Language.format(Menu.lang.getLanguage().ButtonRefresh));
			}
		});
		time.getStylesheets().add(Menu.styleSheet);
		time.setId("bar-4");
		
		usersScroll.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				
				if (event.getDeltaY() > 0)
					usersScroll.setHvalue((pos == minPos ? minPos : (pos-=10))/100.0);
				else
					usersScroll.setHvalue((pos == maxPos ? maxPos : (pos+=10))/100.0);
				
			}
		});
		
		new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				forceRefreshList();
				reloadConfirmations(true, true);
				return null;
			}
		}).start();
		
		startTimers();
	}
	
	private void startTimers(){
		Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(10), ev -> {
			for(String value : launcher.getSteam().getSteamGuard().getSga().keySet()) {
				SteamUser user = launcher.getSteam().getUser(value);
				if(user.hasSteamGuard()) {
					try {
						launcher.getSteam().getSteamGuard().doLogin(user);
						System.out.println("[Steam] Session renewed for user "+value+"!");
					} catch(Throwable throwable) {
						throwable.printStackTrace();
					}
				}
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		
		Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(30), ev -> {
			new Thread(new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					if(isCurrentlyShown())
						reloadConfirmations(false, false);
					return null;
				}
			}).start();
		}));
		timeline2.setCycleCount(Animation.INDEFINITE);
		timeline2.play();
	}
	
	@FXML
	public void reloadConfirmations(){
		new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				reloadConfirmations(true, true);
				return null;
			}
		}).start();
	}
	
	public void reloadConfirmations(boolean showLoading, boolean override){
		if(!override){
			if(System.currentTimeMillis()-15000<lastRefresh){
				return;
			}
		}
		lastRefresh = System.currentTimeMillis();
		
		if(showLoading)
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					steamController.this.confirmations.getChildren().clear();
					Label loading = new Label("loading...");
					loading.setFont(new Font(16));
					loading.setStyle("-fx-text-fill: #d2d2d2");
					steamController.this.confirmations.getChildren().add(loading);
				}
			});
		List<Confirmation> confirmations = new ArrayList<>();
		for(String username : launcher.getSteam().getUsernames()) {
			if(launcher.getSteam().getUser(username).hasSteamGuard()) {
				System.out.println("[Steam] loading confirmations from "+username);
				Confirmation[] confs = launcher.getSteam().getSteamGuard().getConfirmations(launcher.getSteam().getUser(username));
				for(Confirmation conf : confs)
					confirmations.add(conf);
				System.out.println("[Steam] "+confs.length+" confirmations added from "+username);
			}
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				steamController.this.confirmations.getChildren().clear();
				for(Confirmation confirmation : confirmations) {
					FXMLLoader loader = new FXMLLoader(steamController.class.getClassLoader().getResource("fxml/Confirmation.fxml"));
					try {
						AnchorPane root = loader.load();
						confirmationController controller = loader.getController();
						controller.init(confirmation, launcher, steamController.this, root);
						confController.add(controller);
						steamController.this.confirmations.getChildren().add(root);
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
				if(confirmations.size()==0){
					if(steamController.this.confirmations.getChildren().size()!=0) {
						Label loading = (Label) steamController.this.confirmations.getChildren().get(0);
						loading.setText(Language.format(Menu.lang.getLanguage().SteamConfirmationEmpty));
						loading.setFont(new Font(16));
						loading.setStyle("-fx-text-fill: #d2d2d2");
					}else{
						steamController.this.confirmations.getChildren().clear();
						Label loading = new Label(Language.format(Menu.lang.getLanguage().SteamConfirmationEmpty));
						loading.setFont(new Font(16));
						loading.setStyle("-fx-text-fill: #d2d2d2");
						steamController.this.confirmations.getChildren().add(loading);
					}
				}
			}
		});
	}
	
	public ArrayList<confirmationController> getConfController() {
		return confController;
	}
	
	public VBox getConfirmations() {
		return confirmations;
	}
	
	@Override
	public void onShow() {
		if(launcher.getSteam().getUsernames().size() != userCount) {
			forceRefreshList();
		}
		if(!code_user.getText().isEmpty()){
			if(currentUser.hasSteamGuard()) {
				try {
					time.setPrefWidth((30-(TimeAligner.GetSteamTime()%30))*9.3);
					String code = launcher.getSteam().getSteamGuard().getCode(currentUser);
					setCode(code, currentUser);
				} catch(Throwable throwable) {
					throwable.printStackTrace();
				}
			}
		}
	}
	
	boolean flip = true;
	@Override
	public void onRefresh() {
		refreshList();
		flip=!flip;
		if(!code_user.getText().isEmpty()&&isCurrentlyShown()&&flip){
			try {
				time.setPrefWidth((30-(TimeAligner.GetSteamTime()%30))*9.3);
				if(TimeAligner.GetSteamTime()%30==0){
					if(currentUser.hasSteamGuard()) {
						String code = launcher.getSteam().getSteamGuard().getCode(currentUser);
						setCode(code, currentUser);
					}
				}
			} catch(Throwable throwable) {
				throwable.printStackTrace();
			}
		}
	}
	
	public void refreshList() {
		if(!lastUser.equals(launcher.getSteam().getUsernames())) {
			forceRefreshList();
		}
	}
	
	public void forceRefreshList() {
		Task task = new Task() {
			@Override
			protected Object call() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						users.getChildren().clear();
						List<String> usersList = launcher.getSteam().getUsernames();
						controller.clear();
						for(String username : usersList) {
							if(launcher.getSteam().getUser(username).isMainAccount()) {
								addUser(username);
								break;
							}
						}
						for(String username : usersList) {
							addUser:
							{
								if(launcher.getSteam().getUser(username).isMainAccount())
									break addUser;
								addUser(username);
							}
						}
						lastUser = usersList;
					}
				});
				return null;
			}
		};
		new Thread(task).start();
	}
	
	public void addUser(String username) {
		try {
			FXMLLoader loader = new FXMLLoader(steamController.class.getClassLoader().getResource("fxml/steamUser.fxml"));
			Pane root = loader.load();
			
			steamUserController controller = loader.getController();
			controller.setUser(username);
			controller.setLauncher(launcher);
			controller.setSteamController(steamController.this);
			controller.init(menu);
			steamController.this.controller.add(controller);
			
			root.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					controller.showIcons(true);
				}
			});
			root.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					controller.showIcons(false);
				}
			});
			
			users.getChildren().add(root);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void onAdd() {
		try {
			FXMLLoader userLoader = new FXMLLoader(ProgramManager.class.getClassLoader().getResource("fxml/NewSteamUser.fxml"));
			Parent userRoot = userLoader.load();
			Alert userAlert = new Alert(Menu.root);
			
			NewSteamUserController userController = userLoader.getController();
			userController.init(userAlert);
			userController.setLauncher(launcher);
			userController.setCallback(() -> {
				forceRefreshList();
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
	
	@FXML
	void onOpen() {
		FileDialog d = new FileDialog(new JFrame());
		d.setFile("Steam.exe");
		d.show();
		String path = d.getFiles()[0].getPath();
		if(!path.isEmpty()) {
			path = path.replace(new File(path).getName(), "");
			steamPath.setText(path);
			launcher.getSteam().setPath(steamPath.getText());
			forceRefreshList();
		}
	}
	
	@FXML
	void onSteamPathChange() {
		String path = steamPath.getText();
		if(!path.endsWith("\\"))
			path += "\\";
		launcher.getSteam().setPath(path);
		forceRefreshList();
	}
	
	@FXML
	void onCopy() {
		try {
			StringSelection selection = new StringSelection(this.code.getText());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		} catch(Exception e) {
			e.printStackTrace();
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		}
	}
	
	@FXML
	void onRefreshCode() {
		try {
			if(currentUser.hasSteamGuard()) {
				String code = launcher.getSteam().getSteamGuard().getCode(currentUser);
				setCode(code, currentUser);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		}
	}
	
	@FXML
	void onDevMode() {
		launcher.getSteam().setDeveloperMode(devMode.isSelected());
	}
	
	public void setCode(String code, SteamUser user){
		this.code.setText(code.toUpperCase());
		this.code_user.setText(user.getCurrentUsername());
		this.currentUser = user;
		System.out.println("[Steam] Refreshing code for "+user.getUsername());
	}
	
	public ArrayList<steamUserController> getController() {
		return controller;
	}
}
