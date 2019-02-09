package GUI.screens.AddGame.BattleNET;

import GUI.screens.misc.initController;
import api.GameLauncher.BattleNET.BattleNETGames;
import api.GameLauncher.GameLauncher;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

public class EditBattleNETGameController extends initController {
	
	@FXML
	private Label name;
	@FXML
	private JFXTextArea names;
	@FXML
	private JFXButton next;
	
	private EditBattleNETGame editBattleNETGame;
	private BattleNETGames app;
	private GameLauncher launcher;
	private Timeline timeline;
	
	@Override
	public void init(Stage stage) {
		super.init(stage);
		timeline = new Timeline(new KeyFrame(Duration.millis(50), ev -> {
			if(names.getText().isEmpty()) {
				next.setDisable(true);
			} else {
				next.setDisable(false);
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	
	public void loadApp(BattleNETGames app) {
		this.app = app;
		name.setText(app.getName());
		try {
			for(String name : launcher.getBattleNET().getNamesToSay(app)) {
				if(names.getText().isEmpty()) {
					names.setText(name);
				}else{
					names.setText(names.getText()+ "\n" + name);
				}
			}
		}catch(NullPointerException e){
		
		}
	}
	
	public void setLauncher(GameLauncher launcher) {
		this.launcher = launcher;
	}
	
	public void setEditBattleNETGame(EditBattleNETGame editBattleNETGame) {
		this.editBattleNETGame = editBattleNETGame;
	}
	
	@FXML
	void onFinish() {
		List<String> names = new ArrayList<>();
		for(String name : EditBattleNETGameController.this.names.getText().split("\n")) {
			if(name.isEmpty())
				continue;
			names.add(name);
		}
		launcher.getBattleNET().setNamesToSay(app, names);
		stage.close();
		timeline.stop();
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
}
