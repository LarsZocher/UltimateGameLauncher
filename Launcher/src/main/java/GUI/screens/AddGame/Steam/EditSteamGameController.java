package gui.screens.addgame.steam;

import gui.Menu;
import gui.css.CSSUtils;
import gui.localization.Language;
import gui.screens.alert.Alert;
import api.launcher.AppTypes;
import api.launcher.Application;
import api.launcher.GameLauncher;
import api.launcher.image.IconSize;
import api.launcher.image.PathType;
import api.launcher.steam.DBSearchResult;
import api.launcher.steam.SteamApp;
import com.google.gson.Gson;
import com.jfoenix.controls.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.IOException;
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
	
	private EditSteamGameCallback callback;
	private Application application = new Application();
	private SteamApp app = new SteamApp();
	private GameLauncher launcher;
	private Timeline timeline;
	private Alert alert;
	
	public void init(Alert alert) {
		this.alert = alert;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				CSSUtils.addCSS(user, "jfx-combo-box");
				List<String> users = launcher.getSteam().getUsernames();
				for(String username : users) {
					user.getItems().add(launcher.getSteam().getUser(username).getCurrentUsername());
				}
				user.getSelectionModel().select(0);
				
				title.setText(Language.format(Menu.lang.getLanguage().WindowTitleSteamApplication));
				name.setPromptText(Language.format(Menu.lang.getLanguage().Name));
				dev.setPromptText(Language.format(Menu.lang.getLanguage().Developer));
				appid.setPromptText(Language.format(Menu.lang.getLanguage().AppID));
				args.setPromptText(Language.format(Menu.lang.getLanguage().StartOptions));
				next.setText(Language.format(Menu.lang.getLanguage().ButtonFinish));
				next1.setText(Language.format(Menu.lang.getLanguage().ButtonSearchGame));
				next2.setText(Language.format(Menu.lang.getLanguage().ButtonReturn));
			}
		});
		loadSteamApp(application);
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
	}
	
	public void setOnContinue(EditSteamGameCallback callback){
		this.callback = callback;
	}
	
	public void loadSteamApp(Application application) {
		SteamApp app = application.getName()!=null?application.getContent(SteamApp.class):new SteamApp();
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
		this.application = application;
		
		Image header;
		Image icon;
		
		if(!app.getAppID().isEmpty()) {
			System.out.println(launcher.getImageManager().getHeaderURL(application));
			System.out.println(launcher.getImageManager().getHeaderFile(application));
			System.out.println(launcher.getImageManager().getIconPNG(application, IconSize.S_DEFAULT, PathType.FILE));
			System.out.println(launcher.getImageManager().getIconPNG(application, IconSize.S_32, PathType.URL));
			System.out.println(launcher.getImageManager().getIconICO(application, IconSize.S_64, PathType.FILE));
			System.out.println(launcher.getImageManager().getIconICO(application, IconSize.S_128, PathType.URL));
		}
		
		if(!app.getAppID().isEmpty()) {
			header = new Image(launcher.getImageManager().getHeaderURL(application), 225, 103, false, true, true);
			icon = new Image(launcher.getImageManager().getIconPNG(application, IconSize.S_DEFAULT, PathType.URL), 103, 103, false, true, true);
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
			
			application.setDisplayName(app.getName());
			application.setName(app.getConfigName());
			application.setUniqueID("STEAM_"+app.getAppID());
			application.setType(AppTypes.STEAM);
			
			JSONObject json = application.getRawContent();
			json.put("content", new JSONObject(new Gson().toJson(app)));
			application.setContent(json);
			
			alert.close();
			timeline.stop();
			callback.onContinue(application);
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
		alert.close();
	}
	
	@FXML
	void onSearch() {
		try {
			FXMLLoader loader = new FXMLLoader(EditSteamGameController.class.getClassLoader().getResource("fxml/SearchSteamGame.fxml"));
			Parent root = loader.load();
			Alert alert = new Alert(Menu.root);
			
			SearchSteamGameController controller = loader.getController();
			controller.init(alert);
			controller.setOnContinue(new SearchSteamGameCallback() {
				@Override
				public void onContinue(DBSearchResult result) {
					SteamApp app = launcher.getSteam().getSteamApp(result.getAppID());
					name.setText(app.getName());
					appid.setText(app.getAppID());
					dev.setText(app.getDeveloper());
					
					application.setDisplayName(app.getName());
					application.setName(app.getConfigName());
					application.setUniqueID("STEAM_"+app.getAppID());
					application.setType(AppTypes.STEAM);
					JSONObject json = application.getRawContent();
					json.put("content", new JSONObject(new Gson().toJson(app)));
					application.setContent(json);
					
					Image header = new Image(launcher.getImageManager().getHeaderURL(application), 225, 103, false, true, true);
					Image icon = new Image(launcher.getImageManager().getIconPNG(application, IconSize.S_DEFAULT, PathType.URL), 103, 103, false, true, true);
					
					pic_header.setImage(header);
					pic_icon.setImage(icon);
					
					app.setUser(getUser());
					app.setConfigName(EditSteamGameController.this.app.getConfigName());
					EditSteamGameController.this.app = app;
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
