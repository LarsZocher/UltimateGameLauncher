package GUI.screens;

import api.GameLauncher.GameLauncher;
import codebehind.steam.mobileauthentication.model.Confirmation;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.Launcher;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class confirmationController {
	
	@FXML
	private Label title;
	@FXML
	private Label price;
	@FXML
	private Label date;
	@FXML
	private ImageView imageView;
	@FXML
	private JFXButton accept;
	@FXML
	private JFXButton deny;
	
	private Confirmation confirmation;
	private GameLauncher launcher;
	private steamController controller;
	private Parent root;
	
	public void init(Confirmation confirmation, GameLauncher launcher, steamController controller, Parent root) {
		this.confirmation = confirmation;
		this.launcher = launcher;
		this.controller = controller;
		this.root = root;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				title.setText(confirmation.getDescription());
				price.setText(confirmation.getPrice());
				date.setText(confirmation.getDate());
				imageView.setImage(new javafx.scene.image.Image(confirmation.getPicture(), 65, 65, false, true, true));
				accept.setText(confirmation.getAccept());
				deny.setText(confirmation.getCancel());
			}
		});
	}
	
	@FXML
	void onAccept() {
		new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					launcher.getSteam().getSteamGuard().getSga().get(confirmation.getUser().getUsername()).AcceptConfirmation(confirmation);
				} catch(Throwable throwable) {
					throwable.printStackTrace();
				}
				return null;
			}
		}).start();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.getConfirmations().getChildren().remove(root);
				controller.getConfController().remove(this);
			}
		});
	}
	
	@FXML
	void onDeny() {
		new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					launcher.getSteam().getSteamGuard().getSga().get(confirmation.getUser().getUsername()).DenyConfirmation(confirmation);
				} catch(Throwable throwable) {
					throwable.printStackTrace();
				}
				return null;
			}
		}).start();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.getConfirmations().getChildren().remove(root);
				controller.getConfController().remove(this);
			}
		});
	}
	
	public Confirmation getConfirmation() {
		return confirmation;
	}
}
