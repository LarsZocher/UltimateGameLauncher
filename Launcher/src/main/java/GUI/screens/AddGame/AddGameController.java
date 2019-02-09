package GUI.screens.AddGame;

import GUI.Menu;
import GUI.css.CSSUtils;
import GUI.localization.Language;
import GUI.screens.misc.initController;
import api.GameLauncher.AppTypes;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class AddGameController extends initController {
	
	@FXML
	private JFXComboBox<String> types;
	@FXML
	private Label title;
	@FXML
	private Label text;
	@FXML
	private JFXButton next;
	
	private AddGame addGame;
	
	@Override
	public void init(Stage stage) {
		super.init(stage);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				CSSUtils.setCSS(types, "jfx-combo-box");
				for(AppTypes type : AppTypes.values()){
					if(type.isCreateable())
						types.getItems().add(type.getName());
				}
				types.getSelectionModel().select(0);
				
				title.setText(Language.format(Menu.lang.getLanguage().WindowTitleAddGame));
				text.setText(Language.format(Menu.lang.getLanguage().ChooseGame));
				next.setText(Language.format(Menu.lang.getLanguage().ButtonContinue));
			}
		});
	}
	
	public void setAddGame(AddGame addGame) {
		this.addGame = addGame;
	}
	
	@FXML
	void onContinue(){
		AppTypes type = AppTypes.getByName(types.getSelectionModel().getSelectedItem());
		addGame.onContinue(type);
		stage.close();
	}
	
	@FXML
	void onExit() {
		stage.close();
	}
	@FXML
	void onMinimize() {
		stage.setIconified(true);
	}
}
