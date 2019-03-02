//package GUI.old;
//
//import GUI.Utils.ConfigFile;
//import com.jfoenix.controls.*;
//import javafx.animation.Animation;
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
//import javafx.scene.image.ImageView;
//import javafx.util.Duration;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.logging.Level;
//
///**
// * Removing of this disclaimer is forbidden.
// *
// * @author BubbleEgg
// * @verions: 1.0.0
// **/
//
//public class GUIController {
//
//	public GUI gui;
//	public ConfigFile cfg;
//	public ConfigFile cfgmain;
//	private File console;
//	private File onlyConsole;
//	private File withoutConsole;
//
//	@FXML
//	private ImageView closeButton;
//	@FXML
//	private JFXSlider volumeSlider;
//	@FXML
//	private JFXSlider commandEquality;
//	@FXML
//	private JFXSlider lengthDifferenz;
//	@FXML
//	private JFXToggleButton cmdConsoleButton;
//	@FXML
//	private JFXToggleButton DBConsoleButton;
//	@FXML
//	private JFXToggleButton statCollectButton;
//	@FXML
//	private JFXToggleButton afkButton;
//	@FXML
//	private JFXComboBox consoleLevel;
//	@FXML
//	private JFXTextField APIkey;
//	@FXML
//	private Label statStarted;
//	@FXML
//	private Label statUsed;
//	@FXML
//	private Label statCommands;
//	@FXML
//	private JFXButton startButton;
//
//	public void setGui(GUI gui) {
//		this.gui = gui;
//
//	}
//
//	public void init() {
//		createBatchFiles();
//
//		volumeSlider.getStylesheets().setAll("GUI/css/Slider.css");
//		commandEquality.getStylesheets().setAll("GUI/css/Slider.css");
//		lengthDifferenz.getStylesheets().setAll("GUI/css/Slider.css");
//		consoleLevel.getStylesheets().setAll("GUI/css/ComboBox.css");
//		consoleLevel.getItems().setAll(Level.FINEST,
//				Level.FINER,
//				Level.FINE,
//				Level.INFO,
//				Level.WARNING,
//				Level.SEVERE);
//
//		cfg = new ConfigFile("launcher.yml");
//		cfgmain = new ConfigFile("config.yml");
//		cfg.load();
//		cfgmain.load();
//
//		cmdConsoleButton.setSelected(cfg.getBoolean("Start.cmdConsole"));
//		DBConsoleButton.setSelected(cfg.getBoolean("Start.NormalConsole"));
//		statCollectButton.setSelected(cfgmain.getBoolean("Analytics.Collect"));
//
//		consoleLevel.setPromptText(cfgmain.getString("DotBot.ConsoleLevel"));
//		afkButton.setSelected(cfgmain.getBoolean("Google.Listening.DisableOnAFK.Enabled"));
//		commandEquality.setValue(cfgmain.getInt("DotBot.Command.EqualityPercent"));
//		lengthDifferenz.setValue(cfgmain.getInt("DotBot.Command.AllowedLengthDifference"));
//		APIkey.setText(cfgmain.getString("Google.APIKEY"));
//		volumeSlider.setValue(cfgmain.getDouble("Google.Speaking.DefaultVolume") * 100.0);
//
//		statStarted.setText(cfg.getInt("Stat.Started")+"");
//		statUsed.setText(round((cfg.getInt("Stat.Used")/60.0), 1)+"");
//		statCommands.setText(cfg.getInt("Stat.Commands")+"");
//
//		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), ev -> {
//			reloadStats();
//		}));
//		timeline.setCycleCount(Animation.INDEFINITE);
//		timeline.play();
//	}
//
//	public void createBatchFiles() {
//		console = new File("DotBotConsole.bat");
//		if(!console.exists()) {
//			try {
//				console.createNewFile();
//				PrintWriter writer = new PrintWriter(console);
//				writer.println("@echo off");
//				writer.println("java -jar DotBot.jar");
//				writer.println("exit");
//				writer.flush();
//			} catch(IOException e) {
//				e.printStackTrace();
//			}
//		}
//		onlyConsole = new File("DotBotConsoleOnly.bat");
//		if(!onlyConsole.exists()) {
//			try {
//				onlyConsole.createNewFile();
//				PrintWriter writer = new PrintWriter(onlyConsole);
//				writer.println("@echo off");
//				writer.println("java -jar DotBot.jar --console");
//				writer.println("exit");
//				writer.flush();
//			} catch(IOException e) {
//				e.printStackTrace();
//			}
//		}
//		withoutConsole = new File("DotBotWithoutConsole.bat");
//		if(!withoutConsole.exists()) {
//			try {
//				withoutConsole.createNewFile();
//				PrintWriter writer = new PrintWriter(withoutConsole);
//				writer.println("@echo off");
//				writer.println("start javaw -jar DotBot.jar");
//				writer.println("exit");
//				writer.flush();
//			} catch(IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@FXML
//	public void onCloseButtonClick() {
//		gui.stage.close();
//		System.exit(0);
//	}
//
//	@FXML
//	public void onMinButtonClick() {
//		gui.stage.setIconified(true);
//	}
//
//	@FXML
//	public void onCmdConsoleButton() {
//		cfg.load();
//		cfg.set("Start.cmdConsole", cmdConsoleButton.isSelected());
//		cfg.save();
//		checkStartArgs();
//	}
//
//	@FXML
//	public void onDBConsoleButton() {
//		cfg.load();
//		cfg.set("Start.NormalConsole", DBConsoleButton.isSelected());
//		cfg.save();
//		checkStartArgs();
//	}
//
//	@FXML
//	public void onCommandLevel() {
//		try {
//			consoleLevel.setValue("");
//			consoleLevel.setPromptText(consoleLevel.getItems().get(consoleLevel.getSelectionModel().getSelectedIndex()) + "");
//		} catch(Exception e) {
//		}
//	}
//
//	@FXML
//	public void onApplyButton() {
//		cfgmain.load();
//		cfgmain.set("DotBot.ConsoleLevel", consoleLevel.getPromptText());
//		cfgmain.set("Google.Listening.DisableOnAFK.Enabled", afkButton.isSelected());
//		cfgmain.set("DotBot.Command.EqualityPercent", ((int) Math.round(commandEquality.getValue())));
//		cfgmain.set("DotBot.Command.AllowedLengthDifference", ((int) Math.round(lengthDifferenz.getValue())));
//		cfgmain.set("Google.APIKEY", APIkey.getText());
//		cfgmain.set("Google.Speaking.DefaultVolume", ((int) Math.round(volumeSlider.getValue())) / 100.0);
//		cfgmain.save();
//	}
//
//	@FXML
//	public void onStatCollectButton() {
//		cfgmain.load();
//		cfgmain.set("Analytics.Collect", statCollectButton.isSelected());
//		cfgmain.save();
//	}
//
//	public void checkStartArgs(){
//		startButton.setDisable(!cmdConsoleButton.isSelected() && !DBConsoleButton.isSelected());
//	}
//
//	@FXML
//	public void onStart() {
//		if(!cmdConsoleButton.isSelected() && !DBConsoleButton.isSelected()) {
//			return;
//		}
//		if(cmdConsoleButton.isSelected() && !DBConsoleButton.isSelected()) {
//			try {
//				Runtime.getRuntime().exec("cmd /c start \"\" " + onlyConsole.getPath());
//			} catch(IOException e) {
//				e.printStackTrace();
//			}
//		}
//		if(cmdConsoleButton.isSelected() && DBConsoleButton.isSelected()) {
//			try {
//				Runtime.getRuntime().exec("cmd /c start \"\" " + console.getPath());
//			} catch(IOException e) {
//				e.printStackTrace();
//			}
//		}
//		if(!cmdConsoleButton.isSelected() && DBConsoleButton.isSelected()) {
//			try {
//				Runtime.getRuntime().exec("cmd /c start \"\" " + withoutConsole.getPath());
//			} catch(IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public void reloadStats(){
//		cfg.load();
//
//		statStarted.setText(cfg.getInt("Stat.Started")+"");
//		statUsed.setText(round((cfg.getInt("Stat.Used")/60.0), 1)+"");
//		statCommands.setText(cfg.getInt("Stat.Commands")+"");
//	}
//
//	public double round(double zahl, int stellen) {
//		return ((int)zahl + (Math.round(Math.pow(10,stellen)*(zahl-(int)zahl)))/(Math.pow(10,stellen)));
//	}
//
//}
