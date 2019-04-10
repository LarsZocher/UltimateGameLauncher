package GUI.screens.Alert;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.mp4parser.aspectj.lang.reflect.AjType;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class AlertManager {
	
	private static int activeAlerts = 0;
	
	public static void showSimpleAlert(StackPane root, Node back, String message){
//		JFXDialogLayout layout = new JFXDialogLayout();
//		layout.setBody(new Label(message));
//
//		JFXButton button = new JFXButton("OK");
//		button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				dialog.close();
//			}
//		});
//		layout.setActions(button);
//		showAlert(root, back, message);
	}
	
	public static void showAlert(StackPane root, Node back, Region content, String message){
		GaussianBlur blur = new GaussianBlur(0);
		ColorAdjust adjust = new ColorAdjust(0, 0, 0, 0);
		adjust.setInput(blur);
		
		DoubleProperty value = new SimpleDoubleProperty(0);
		value.addListener((observable, oldV, newV) ->
		{
			blur.setRadius(10*newV.doubleValue());
			adjust.setBrightness(-0.3*newV.doubleValue());
		});
		
		Timeline timeline = new Timeline();
		final KeyValue kv = new KeyValue(value, 1);
		final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
		timeline.getKeyFrames().add(kf);
		
		Timeline timeline2 = new Timeline();
		final KeyValue kv2 = new KeyValue(value, 0);
		final KeyFrame kf2 = new KeyFrame(Duration.millis(200), kv2);
		timeline2.getKeyFrames().add(kf2);
		
		back.setEffect(adjust);
		
		JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
		
		dialog.show();
		dialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
			@Override
			public void handle(JFXDialogEvent event) {
				activeAlerts--;
				if(activeAlerts!=0)return;
				back.setEffect(blur);
				timeline2.playFromStart();
				timeline2.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if(activeAlerts!=0)return;
						back.setEffect(null);
					}
				});
			}
		});
		activeAlerts++;
		if(activeAlerts!=1)return;
		timeline.playFromStart();
	}
}
