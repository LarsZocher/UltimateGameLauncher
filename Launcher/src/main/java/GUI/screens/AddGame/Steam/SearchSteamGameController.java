package GUI.screens.AddGame.Steam;

import GUI.Menu;
import GUI.localization.Language;
import GUI.screens.Alert.Alert;
import api.GameLauncher.Steam.DBSearchResult;
import api.GameLauncher.Steam.SteamDB;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class SearchSteamGameController {
	
	@FXML
	private JFXTextField search;
	@FXML
	private VBox list;
	@FXML
	private JFXButton next;
	@FXML
	private JFXButton next1;
	@FXML
	private JFXButton next11;
	@FXML
	private Label title;
	@FXML
	private Label text;
	
	private List<Label> labels = new ArrayList<>();
	private List<HBox> boxes = new ArrayList<>();
	private List<DBSearchResult> results = new ArrayList<>();
	private HBox selected;
	private Alert alert;
	private SearchSteamGameCallback callback;
	
	public void init(Alert alert) {
		this.alert = alert;
		for(Node node : list.getChildren()) {
			HBox box = (HBox) node;
			box.setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					unselectAllBoxes();
					if(!((Label)box.getChildren().get(0)).getText().isEmpty()) {
						box.setStyle("-fx-background-color: -primary");
						selected = box;
						next.setDisable(false);
					}
				}
			});
			
			boxes.add(box);
			labels.add((Label) box.getChildren().get(0));
		}
		for(Label label : labels) {
			label.setText("");
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				next.setText(Language.format(Menu.lang.getLanguage().ButtonFinish));
				next1.setText(Language.format(Menu.lang.getLanguage().ButtonCancel));
				next11.setText(Language.format(Menu.lang.getLanguage().ButtonSearch));
				title.setText(Language.format(Menu.lang.getLanguage().WindowTitleSearch));
				text.setText(Language.format(Menu.lang.getLanguage().Search));
			}
		});
	}
	
	private void unselectAllBoxes(){
		selected = null;
		next.setDisable(true);
		for(HBox box : boxes) {
			box.setStyle("-fx-background-color: TRANSPARENT");
		}
	}
	
	
	@FXML
	void onFinish(){
		for(DBSearchResult result : results) {
			if(result.getName().equalsIgnoreCase(((Label)selected.getChildren().get(0)).getText())){
				callback.onContinue(result);
				alert.close();
				return;
			}
		}
	}
	
	@FXML
	void onSearch(){
		unselectAllBoxes();
		results = SteamDB.searchByName(search.getText(), 25);
		int loops = results.size()<10?results.size():10;
		for(Label label : labels) {
			label.setText("");
		}
		List<String> names = new ArrayList<>();
		for(DBSearchResult result : results){
			names.add(result.getName());
		}
		for(int i = 0; i<loops; i++){
			labels.get(i).setText(names.get(i));
		}
	}
	
	public void setOnContinue(SearchSteamGameCallback callback) {
		this.callback = callback;
	}
	
	@FXML
	public void onExit(){
		alert.close();
	}
}
