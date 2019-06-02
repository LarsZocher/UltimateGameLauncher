package gui.screens.alert;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public abstract class CustomAlert {
	
	protected JFXDialog dialog;
	
	public void setDialog(JFXDialog dialog){
		this.dialog = dialog;
	}
	
	public JFXDialog getDialog() {
		return dialog;
	}
	
	public abstract Region getContent();
	
	public abstract void onClose(JFXDialogEvent event);
}
