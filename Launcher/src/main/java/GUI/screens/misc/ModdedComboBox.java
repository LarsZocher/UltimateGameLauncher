package gui.screens.misc;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class ModdedComboBox{
	
	public JFXComboBox<String> box;
	
	public ModdedComboBox(JFXComboBox<String> box, String promtText, List<String> items){
		this.box = box;
		this.box.setPromptText(promtText);
		this.box.getItems().addAll(items);
		this.box.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					box.setValue("");
					box.setPromptText(box.getItems().get(box.getSelectionModel().getSelectedIndex()) + "");
				} catch(Exception e) {
				}
			}
		});
	}
	
	public String getSelectedItem(){
		return box.getPromptText();
	}
}
