//package GUI.screens;
//
//import GUI.Menu;
//import GUI.Utils.ConfigFile;
//import GUI.screens.misc.initMenuController;
//import com.jfoenix.controls.*;
//import javafx.application.Platform;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.fxml.FXML;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Removing of this disclaimer is forbidden.
// *
// * @author BubbleEgg
// * @verions: 1.0.0
// **/
//
//public class settingsController extends initMenuController {
//
//	@FXML
//	private JFXSlider RadioVolume2;
//
//	@FXML
//	private JFXToggleButton ShowAllCommands;
//
//	@FXML
//	private JFXTextField APIKEY;
//
//	@FXML
//	private JFXSlider CommandEquality;
//
//	@FXML
//	private JFXSlider CommandLengthDifferenz;
//
//	@FXML
//	private JFXTextField ListenerTimer;
//
//	@FXML
//	private JFXSlider RadioVolume;
//
//	@FXML
//	private JFXToggleButton CollectStats;
//
//	@FXML
//	private JFXToggleButton AFKMode;
//
//	@FXML
//	private JFXSlider SpeakVolume;
//
//	@FXML
//	private JFXButton cancel;
//	@FXML
//	private JFXButton apply;
//
//	@FXML
//	private JFXComboBox<String> consoleLevel;
//
//	private ConfigFile cfg;
//	private ConfigFile radio;
//
//	@Override
//	public void init(Menu menu) {
//		super.init(menu);
//		radio = new ConfigFile("plugins/Radio/config.yml");
//		cfg = new ConfigFile("config.yml");
//		cfg.load();
//		List<String> items = new ArrayList<>();
//		items.add("FINEST");
//		items.add("FINER");
//		items.add("FINE");
//		items.add("INFO");
//		items.add("WARNING");
//		items.add("SEVERE");
////		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), ev -> {
////			loadSettings();
////		}));
////		timeline.play();
//		consoleLevel.getItems().setAll(items);
////		CSSUtils.setCSS(consoleLevel, "jfx-combo-box");
////		CSSUtils.setCSS(CommandEquality, "jfx-slider");
////		CSSUtils.setCSS(CommandLengthDifferenz, "jfx-slider");
////		CSSUtils.setCSS(SpeakVolume, "jfx-slider");
////		CSSUtils.setCSS(RadioVolume, "jfx-slider");
////		CSSUtils.setCSS(RadioVolume2, "jfx-slider");
////		CSSUtils.setCSS(APIKEY, "jfx-text-field");
////		CSSUtils.setCSS(ListenerTimer, "jfx-text-field");
////		CSSUtils.setCSS(ShowAllCommands, "jfx-toggle-button");
////		CSSUtils.setCSS(AFKMode, "jfx-toggle-button");
////		CSSUtils.setCSS(CollectStats, "jfx-toggle-button");
////		CSSUtils.setCSS(cancel, "jfx-button");
////		CSSUtils.setCSS(apply, "jfx-button");
//
//		ListenerTimer.textProperty().addListener(new ChangeListener<String>() {
//			@Override
//			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				if (!newValue.matches("\\d*")) {
//					ListenerTimer.setText(newValue.replaceAll("[^\\d]", ""));
//				}
//			}
//		});
//		//loadSettings();
//	}
//
//	@Override
//	public void onRefresh() {
//		compairSettings();
//	}
//
//	@Override
//	public void onShow() {
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				loadSettings();
//			}
//		});
//	}
//
//	public void loadSettings() {
//		if(!cfg.getFile().exists()){
//			return;
//		}
//		cfg.load();
//		radio.load();
//		consoleLevel.getSelectionModel().select(cfg.getString("DotBot.ConsoleLevel").toUpperCase());
//		CommandEquality.setValue(cfg.getDouble("DotBot.Command.EqualityPercent"));
//		CommandLengthDifferenz.setValue(cfg.getDouble("DotBot.Command.AllowedLengthDifference"));
//		ShowAllCommands.setSelected(cfg.getBoolean("DotBot.Command.DisplayAllResults"));
//		APIKEY.setText(cfg.getString("Google.APIKEY"));
//		ListenerTimer.setText(cfg.getString("Google.Listening.RestartTime"));
//		AFKMode.setSelected(cfg.getBoolean("Google.Listening.DisableOnAFK.Enabled"));
//		SpeakVolume.setValue(cfg.getDouble("Google.Speaking.DefaultVolume") * 100);
//		CollectStats.setSelected(cfg.getBoolean("Analytics.Collect"));
//		RadioVolume.setValue(radio.getInt("Radio.DefaultVolume"));
//		RadioVolume2.setValue(radio.getInt("Radio.QuieterVolume"));
//	}
//
//	public void compairSettings() {
//		try {
//			boolean isEqual = true;
//			check:
//			{
//				cfg.load();
//				radio.load();
//
//				if(!consoleLevel.getSelectionModel().getSelectedItem().equalsIgnoreCase(cfg.getString("DotBot.ConsoleLevel").toUpperCase())) {
//					isEqual = false;
//					break check;
//				}
//				if(CommandEquality.getValue() != cfg.getDouble("DotBot.Command.EqualityPercent")) {
//					isEqual = false;
//					break check;
//				}
//				if(CommandLengthDifferenz.getValue() != cfg.getDouble("DotBot.Command.AllowedLengthDifference")) {
//					isEqual = false;
//					break check;
//				}
//				if(ShowAllCommands.isSelected() != cfg.getBoolean("DotBot.Command.DisplayAllResults")) {
//					isEqual = false;
//					break check;
//				}
//				if(!APIKEY.getText().equalsIgnoreCase(cfg.getString("Google.APIKEY"))) {
//					isEqual = false;
//					break check;
//				}
//				if(!ListenerTimer.getText().equalsIgnoreCase(cfg.getString("Google.Listening.RestartTime"))) {
//					isEqual = false;
//					break check;
//				}
//				if(AFKMode.isSelected() != cfg.getBoolean("Google.Listening.DisableOnAFK.Enabled")) {
//					isEqual = false;
//					break check;
//				}
//				if(SpeakVolume.getValue() != ((int) (cfg.getDouble("Google.Speaking.DefaultVolume") * 100))) {
//					isEqual = false;
//					break check;
//				}
//				if(CollectStats.isSelected() != cfg.getBoolean("Analytics.Collect")) {
//					isEqual = false;
//					break check;
//				}
//				if(RadioVolume.getValue() != radio.getInt("Radio.DefaultVolume")) {
//					isEqual = false;
//					break check;
//				}
//				if(RadioVolume2.getValue() != radio.getInt("Radio.QuieterVolume")) {
//					isEqual = false;
//					break check;
//				}
//			}
//			cancel.setDisable(isEqual);
//		}catch(NullPointerException e){
//
//		}
//	}
//
//	@FXML
//	void onCancel() {
//		loadSettings();
//		cancel.setDisable(true);
//	}
//
//	@FXML
//	void onApply() {
//
//
//		try {
//			cfg.load();
//			cfg.set("DotBot.ConsoleLevel", consoleLevel.getSelectionModel().getSelectedItem());
//			cfg.set("DotBot.Command.EqualityPercent", ((int) Math.round(CommandEquality.getValue())));
//			cfg.set("DotBot.Command.AllowedLengthDifference", ((int) Math.round(CommandLengthDifferenz.getValue())));
//			cfg.set("DotBot.Command.DisplayAllResults", ShowAllCommands.isSelected());
//			cfg.set("Google.APIKEY", APIKEY.getText());
//			cfg.set("Google.Listening.RestartTime", Integer.valueOf(ListenerTimer.getText()));
//			cfg.set("Google.Listening.DisableOnAFK.Enabled", AFKMode.isSelected());
//			cfg.set("Google.Speaking.DefaultVolume", ((int) Math.round(SpeakVolume.getValue())) / 100.0);
//			cfg.set("Analytics.Collect", CollectStats.isSelected());
//			cfg.save();
//
//			radio.load();
//			radio.set("Radio.DefaultVolume", ((int) Math.round(RadioVolume.getValue())));
//			radio.set("Radio.QuieterVolume", ((int) Math.round(RadioVolume2.getValue())));
//			radio.save();
//		} catch(Exception e) {
//
//		}
//	}
//}
