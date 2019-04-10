package GUI.screens.AddGame;

import GUI.Menu;
import GUI.css.CSSUtils;
import GUI.localization.Language;
import GUI.screens.Alert.Alert;
import GUI.screens.misc.Callback;
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

public class AddGameController {
	
	@FXML
	private JFXComboBox<String> types;
	@FXML
	private Label text;
	@FXML
	private Label title;
	@FXML
	private JFXButton next;
	
	private Callback<AppTypes> callback;
	private Alert alert;
	
	public void init() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				CSSUtils.addCSS(types, "jfx-combo-box");
				for(AppTypes type : AppTypes.values()){
					if(type.isCreateable())
						types.getItems().add(type.getName());
				}
				types.getSelectionModel().select(0);
				
				
				text.setText(Language.format(Menu.lang.getLanguage().ChooseGame));
				next.setText(Language.format(Menu.lang.getLanguage().ButtonContinue));
				title.setText(Language.format(Menu.lang.getLanguage().WindowTitleAddGame));
			}
		});
	}
	
	public void setCallback(Callback<AppTypes> callback) {
		this.callback = callback;
	}
	
	public void setAlert(Alert alert) {
		this.alert = alert;
	}
	
	@FXML
	void onContinue(){
		AppTypes type = AppTypes.getByName(types.getSelectionModel().getSelectedItem());
		callback.onCallback(type);
		alert.close();
	}
}
