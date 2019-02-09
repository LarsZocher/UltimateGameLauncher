package GUI.screens.misc;

import api.GameLauncher.Application;
import javafx.scene.layout.Pane;
import org.controlsfx.control.GridCell;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class DisplayGridCell extends GridCell<GameDisplay> {
	
	private Pane pane;
	
	@Override
	protected void updateItem(GameDisplay item, boolean empty) {
		super.updateItem(item, empty);
		
		if (empty) {
			setGraphic(null);
		} else {
			pane = item.getPane();
			setGraphic(pane);
		}
	}
}
