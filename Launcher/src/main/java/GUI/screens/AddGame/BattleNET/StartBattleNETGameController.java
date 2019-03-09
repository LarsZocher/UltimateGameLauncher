package GUI.screens.AddGame.BattleNET;

import GUI.localization.Language;
import GUI.localization.LanguageManager;
import GUI.screens.misc.BattleNETUserItem;
import GUI.screens.misc.initController;
import GUI.screens.misc.initMenuController;
import api.GameLauncher.Application;
import api.GameLauncher.BattleNET.BattleNET;
import api.GameLauncher.BattleNET.BattleNETGames;
import api.GameLauncher.BattleNET.BattleNETUser;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Utils.JsonConfig;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.sun.glass.ui.Timer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class StartBattleNETGameController extends initController {
	
	@FXML
	private Pane timer;
	@FXML
	private Label selectedUser;
	@FXML
	private Label startingWith;
	@FXML
	private ImageView icon;
	@FXML
	private VBox list;
	@FXML
	private ScrollPane pane;
	
	private int TIME_UNTIL_START;
	private final int FPS = 30;
	private final int START_WIDTH = 364;
	
	private GameLauncher launcher;
	private Timeline timeline;
	private double currentWitdhMultiplierer = 1;
	private int timerIndex = 0;
	private String currentUser;
	private BattleNETGames app;
	private List<BattleNETUserItem> items = new ArrayList<>();
	private int listIndex = 0;
	private long startTime;
	private JsonConfig jsonConfig;
	private LanguageManager lang;
	
	@Override
	public void init(Stage stage) {
		super.init(stage);
		this.launcher = new GameLauncher();
		this.list.setSpacing(10);
		this.startTime = System.currentTimeMillis() + 1000;
		
		this.jsonConfig = new JsonConfig("launcher.json");
		this.jsonConfig.load();
		this.jsonConfig.setDefault("language", "english");
		this.jsonConfig.setDefault("battleNetStartTime", 4000);
		this.jsonConfig.save();
		
		TIME_UNTIL_START = jsonConfig.getConfig().getInt("battleNetStartTime");
		lang = new LanguageManager(jsonConfig.getConfig().getString("language"));
		
		loadUsers();
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				startingWith.setText(Language.format(lang.getLanguage().StartBattleNETGameWith));
				
				stage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						if(event.getCode()== KeyCode.UP){
							if(listIndex>0){
								items.get(listIndex).unfocus();
								listIndex--;
								items.get(listIndex).focus();
								if(timeline.getStatus()== Animation.Status.RUNNING)
									stopTimer();
								ensureVisible(items.get(listIndex).getBox(), true);
								selectedUser.setText(items.get(listIndex).getUser().getName());
							}
						}
						if(event.getCode()== KeyCode.DOWN){
							if(listIndex<items.size()-1){
								items.get(listIndex).unfocus();
								listIndex++;
								items.get(listIndex).focus();
								if(timeline.getStatus()== Animation.Status.RUNNING)
									stopTimer();
								ensureVisible(items.get(listIndex).getBox(), false);
								selectedUser.setText(items.get(listIndex).getUser().getName());
							}
						}
						if(event.getCode()== KeyCode.ENTER){
							items.get(listIndex).triggerOnClick();
							stage.hide();
						}
					}
				});
				pane.setOnKeyPressed(event -> {
					if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP)
						event.consume();
				});
			}
		});
	}
	
	public void loadUsers(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				list.getChildren().clear();
				items.clear();
				String last = launcher.getBattleNET().getSettings().getSavedAccountNames().get(0);
				int index = 0;
				for(int i = 0; i<launcher.getBattleNET().getUsers().size(); i++) {
					BattleNETUser user = launcher.getBattleNET().getUsers().get(i);
					BattleNETUserItem item = new BattleNETUserItem(user, i) {
						@Override
						public void onClick(BattleNETUser user) {
							launcher.getBattleNET().changeUser(user);
							launcher.getBattleNET().launch(app);
							stage.hide();
						}
						
						@Override
						public void onMouseEntered() {
							if(!(System.currentTimeMillis()>startTime))
								return;
							items.get(listIndex).unfocus();
							listIndex = getIndex();
							items.get(listIndex).focus();
							if(timeline.getStatus()== Animation.Status.RUNNING)
								stopTimer();
							selectedUser.setText(items.get(listIndex).getUser().getName());
						}
					};
					list.getChildren().add(item.getBox());
					items.add(item);
					if(last.equalsIgnoreCase(user.getEmail())){
						index = i;
					}
				}
				listIndex = index;
				items.get(index).focus();
				selectedUser.setText(items.get(listIndex).getUser().getName());
			}
		});
	}
	
	public void setApp(BattleNETGames app){
		this.app = app;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				icon.setImage(new Image(launcher.getBattleNET().getIconAsURL128(app)));
			}
		});
	}
	
	public void startTimer(){
		timeline = new Timeline(new KeyFrame(Duration.millis(1000/FPS), ev -> {
			timerIndex++;
			currentWitdhMultiplierer = 1-(1.0/((TIME_UNTIL_START/1000)*FPS))*timerIndex;
			timer.setPrefWidth((int)(START_WIDTH*currentWitdhMultiplierer));
		}));
		timeline.setCycleCount((TIME_UNTIL_START/1000)*FPS);
		timeline.play();
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				launcher.getBattleNET().changeUser(items.get(listIndex).getUser());
				launcher.getBattleNET().launch(app);
				stage.hide();
			}
		});
	}
	
	private void stopTimer(){
		timeline.stop();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				timer.setPrefWidth(START_WIDTH);
			}
		});
	}
	
	@FXML
	void onExit() {
		stage.close();
	}
	
	private void ensureVisible(Node node, boolean up) {
		
		if(up) {
			double height = pane.getContent().getBoundsInLocal().getHeight();
			double y = node.getBoundsInParent().getMinY();
			
			pane.setVvalue(y / height);
		}else{
			double height = pane.getContent().getBoundsInLocal().getHeight();
			double y = node.getBoundsInParent().getMaxY();
			
			pane.setVvalue(y / height);
		}
		
		node.requestFocus();
	}
}
