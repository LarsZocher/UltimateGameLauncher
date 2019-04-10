package GUI.screens.Alert;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Alert {
	
	public static int activeAlerts = 0;
	
	private JFXDialog dialog = new JFXDialog();
	private StackPane root;
	private Node background;
	private Region content;
	private double blurValue = 0;
	private double hueValue = 0;
	private double saturationValue = 0;
	private double brightnessValue = 0;
	private double contrastValue = 0;
	private boolean useEffects = false;
	
	private Timeline timeline;
	private Timeline timeline2;
	
	public Alert(StackPane root) {
		this.root = root;
		dialog.setDialogContainer(root);
	}
	
	public Alert(StackPane root, Region content) {
		this(root);
		setContent(content);
	}
	
	public Alert(StackPane root, Node background, Region content) {
		this(root, content);
		this.background = background;
	}
	
	public void show() {
		dialog.show();
		
		GaussianBlur blur = new GaussianBlur(blurValue);
		ColorAdjust adjust = new ColorAdjust(hueValue, saturationValue, brightnessValue, contrastValue);
		adjust.setInput(blur);
		
		DoubleProperty value = new SimpleDoubleProperty(0);
		if(useEffects && background != null) {
			value.addListener((observable, oldV, newV) ->
			{
				blur.setRadius(blurValue * newV.doubleValue());
				adjust.setHue(hueValue * newV.doubleValue());
				adjust.setSaturation(saturationValue * newV.doubleValue());
				adjust.setBrightness(brightnessValue * newV.doubleValue());
				adjust.setContrast(contrastValue * newV.doubleValue());
			});
			
			timeline = new Timeline();
			final KeyValue kv = new KeyValue(value, 1);
			final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
			timeline.getKeyFrames().add(kf);
			
			timeline2 = new Timeline();
			final KeyValue kv2 = new KeyValue(value, 0);
			final KeyFrame kf2 = new KeyFrame(Duration.millis(200), kv2);
			timeline2.getKeyFrames().add(kf2);
			background.setEffect(adjust);
		}
		setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
			@Override
			public void handle(JFXDialogEvent event) {
				activeAlerts--;
				if(activeAlerts != 0 || !useEffects || background == null) return;
				
				value.setValue(1);
				
				timeline2.playFromStart();
				timeline2.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if(activeAlerts == 0 && background != null) {
							background.setEffect(null);
						}
					}
				});
				
			}
		});
		
		activeAlerts++;
		if(activeAlerts != 1 || !useEffects || background == null) return;
		
		timeline.playFromStart();
	}
	
	public void close() {
		dialog.close();
	}
	
	public void setBackgroundBlur(double value) {
		this.blurValue = value;
		this.useEffects = true;
	}
	
	public void setBackgroundColorAdjust(double hue, double saturation, double brightness, double contrast) {
		this.hueValue = hue;
		this.saturationValue = saturation;
		this.brightnessValue = brightness;
		this.contrastValue = contrast;
		this.useEffects = true;
	}
	
	public void setContent(SimpleAlert sa) {
		sa.setDialog(dialog);
		setContent(sa.getContent());
	}
	
	public void setContent(Region content) {
		this.content = content;
		dialog.setContent(content);
	}
	
	public void setBackground(Node background) {
		this.background = background;
	}
	
	public void setOnDialogClosed(EventHandler<JFXDialogEvent> event) {
		dialog.addEventHandler(JFXDialogEvent.CLOSED, event);
	}
	
	public void setOnDialogOpened(EventHandler<JFXDialogEvent> event) {
		dialog.addEventHandler(JFXDialogEvent.OPENED, event);
	}
}
