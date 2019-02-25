package GUI.screens.AddGame.Steam;

import GUI.Menu;
import GUI.css.CSSUtils;
import GUI.localization.Language;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Steam.DBSearchResult;
import api.GameLauncher.Steam.SteamApp;
import api.GameLauncher.Steam.SteamDB;
import com.jfoenix.controls.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class EditSteamGameController {
	
	@FXML
	private JFXComboBox<String> user;
	@FXML
	private JFXTextField name;
	@FXML
	private JFXTextField dev;
	@FXML
	private JFXTextField appid;
	@FXML
	private JFXTextField args;
	@FXML
	private Label title;
	@FXML
	private Label userLabel;
	@FXML
	private JFXListView<HBox> names;
	@FXML
	private JFXButton next;
	@FXML
	private JFXButton next1;
	@FXML
	private JFXButton next2;
	@FXML
	private ImageView pic_header;
	@FXML
	private ImageView pic_icon;
	@FXML
	private ImageView pic_icon1;
	@FXML
	private StackPane stack_header;
	@FXML
	private StackPane stack_icon;
	@FXML
	private HBox change;
	
	private EditSteamGame editSteamGame;
	private SteamApp app = new SteamApp();
	private GameLauncher launcher;
	private Timeline timeline;
	private Stage stage;
	
	public void init(Stage stage) {
		this.stage = stage;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				CSSUtils.setCSS(user, "jfx-combo-box");
				List<String> users = launcher.getSteam().getUsernames();
				for(String username : users) {
					user.getItems().add(launcher.getSteam().getUser(username).getCurrentUsername());
				}
				user.getSelectionModel().select(0);
				
				title.setText(Language.format(Menu.lang.getLanguage().WindowTitleSteamApplication));
				name.setPromptText(Language.format(Menu.lang.getLanguage().Name));
				dev.setPromptText(Language.format(Menu.lang.getLanguage().Developer));
				appid.setPromptText(Language.format(Menu.lang.getLanguage().AppID));
				userLabel.setText(Language.format(Menu.lang.getLanguage().AdvancedSettings));
				args.setPromptText(Language.format(Menu.lang.getLanguage().StartOptions));
				next.setText(Language.format(Menu.lang.getLanguage().ButtonFinish));
				next1.setText(Language.format(Menu.lang.getLanguage().ButtonSearchGame));
				next2.setText(Language.format(Menu.lang.getLanguage().ButtonReturn));
			}
		});
		loadSteamApp(app);
		timeline = new Timeline(new KeyFrame(Duration.millis(50), ev -> {
			if(name.getText().isEmpty() || appid.getText().isEmpty()) {
				next.setDisable(true);
			} else {
				next.setDisable(false);
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		
		appid.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.matches("\\d*")) {
					appid.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				user.getSelectionModel().select(launcher.getSteam().getMainAccount().getCurrentUsername());
			}
		});
		setImageEffects();
	}
	
	private void setImageEffects(){
		pic_header.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pic_header.setOpacity(0.4);
				pic_header.toBack();
				pic_icon.toFront();
			}
		});
		stack_header.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pic_header.setOpacity(1);
				pic_header.toFront();
			}
		});
		pic_icon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pic_icon.setOpacity(0.4);
				pic_icon1.toFront();
				pic_header.toFront();
			}
		});
		stack_icon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pic_icon.setOpacity(1);
				pic_icon.toFront();
			}
		});
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stage.getScene().setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						pic_icon.toFront();
						pic_header.toFront();
						pic_header.setOpacity(1);
						pic_icon.setOpacity(1);
					}
				});
			}
		});
	}
	
	
	
	public void loadSteamApp(SteamApp app) {
		name.setText(app.getName());
		appid.setText(app.getAppID());
		args.setText(app.getArgs());
		dev.setText(app.getDeveloper());
		try {
			for(String name : app.getNamesToSay()) {
				SteamNameItem item = new SteamNameItem(name) {
					@Override
					public void onRemove() {
						names.getItems().remove(this.getBox());
					}
				};
				names.getItems().add(item.getBox());
			}
		} catch(NullPointerException e) {
		
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < user.getItems().size(); i++) {
					if(user.getItems().get(i).equalsIgnoreCase(launcher.getSteam().getUser(app.getUser()).getCurrentUsername())) {
						user.getSelectionModel().select(i);
						break;
					}
				}
			}
		});
		this.app = app;
		
		Image header;
		Image icon = null;
		if(!app.getAppID().isEmpty()) {
			header = new Image(app.getPicture(), 225, 103, false, true, true);
			try {
				icon = new Image(new File(app.getIconPNGPath(launcher, false)).toURI().toURL().toExternalForm(), 103, 103, false, true, true);
			} catch(MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			header = new Image("icon/loading_dark.png", 225, 103, false, true, true);
			icon = new Image("icon/loading_dark.png", 103, 103, false, true, true);
		}
		pic_header.setImage(header);
		pic_icon.setImage(icon);
	}
	
	public void setLauncher(GameLauncher launcher) {
		this.launcher = launcher;
	}
	
	public void setEditSteamGame(EditSteamGame editSteamGame) {
		this.editSteamGame = editSteamGame;
	}
	
	@FXML
	void onFinish() {
		if((!name.getText().isEmpty()) && (!appid.getText().isEmpty())) {
			app.setName(name.getText());
			app.setAppID(appid.getText());
			app.setUser(getUser());
			app.setArgs(args.getText());
			app.setDeveloper(dev.getText());
			List<String> names = new ArrayList<>();
			names.add(name.getText());
			app.setNamesToSay(names);
			if(app.getConfigName().isEmpty()) {
				app.setConfigName(app.getName());
				CheckName();
			}
			stage.close();
			timeline.stop();
			editSteamGame.onContinue(app);
		}
	}
	
	private void CheckName() {
		for(String s : launcher.getSteam().getApps()) {
			if(s.equalsIgnoreCase(app.getConfigName())) {
				app.setConfigName(app.getConfigName() + "_2");
				CheckName();
			}
		}
	}
	
	@FXML
	void onBack() {
		timeline.stop();
		stage.close();
	}
	
	@FXML
	void onSearch() {
		SearchSteamGame steamGame = new SearchSteamGame() {
			@Override
			public void onContinue(DBSearchResult result) {
				SteamApp app = launcher.getSteam().getSteamCMD().getSteamApps(Integer.valueOf(result.getAppID())).get(Integer.valueOf(result.getAppID()));
				name.setText(app.getName());
				appid.setText(app.getAppID());
				dev.setText(app.getDeveloper());
				
				
				Image header;
				Image icon = null;
				if(!app.getPicture().isEmpty()) {
					header = new Image(app.getPicture(), 225, 103, false, true, true);
					try {
						icon = new Image(new File(app.getIconPNGPath(launcher, false)).toURI().toURL().toExternalForm(), 103, 103, false, true, true);
					} catch(MalformedURLException e) {
						e.printStackTrace();
					}
				} else {
					header = new Image("icon/loading_dark.png", 225, 103, false, true, true);
					icon = new Image("icon/loading_dark.png", 103, 103, false, true, true);
				}
				pic_header.setImage(header);
				pic_icon.setImage(icon);
				
				app.setUser(getUser());
				app.setConfigName(EditSteamGameController.this.app.getConfigName());
				EditSteamGameController.this.app = app;
			}
		};
		try {
			steamGame.start(new Stage());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void onExit() {
		timeline.stop();
		stage.close();
	}
	
	@FXML
	void onMinimize() {
		stage.setIconified(true);
	}
	
	private String getUser() {
		List<String> users = launcher.getSteam().getUsernames();
		for(String username : users) {
			if(launcher.getSteam().getUser(username).getCurrentUsername().equalsIgnoreCase(user.getSelectionModel().getSelectedItem())) {
				return username;
			}
		}
		return "";
	}
}
