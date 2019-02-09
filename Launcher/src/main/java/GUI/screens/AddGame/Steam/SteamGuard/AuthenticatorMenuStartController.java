package GUI.screens.AddGame.Steam.SteamGuard;

import GUI.Menu;
import GUI.localization.Language;
import GUI.screens.Notification.*;
import api.GameLauncher.GameLauncher;
import api.GameLauncher.Steam.SteamUser;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class AuthenticatorMenuStartController extends AuthenticatorInit{
	
	@FXML
	private Label title1;
	@FXML
	private Label title2;
	@FXML
	private Label title3;
	@FXML
	private Label desc1;
	@FXML
	private Label desc2;
	@FXML
	private Label desc3;
	@FXML
	private JFXButton cancel;
	
	private GameLauncher launcher = new GameLauncher();
	
	@Override
	public void init(AuthenticatorMenu menu) {
		super.init(menu);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				title1.setText(Language.format(Menu.lang.getLanguage().AuthenticatorStartTitleAdd));
				title2.setText(Language.format(Menu.lang.getLanguage().AuthenticatorStartTitleAddManually));
				title3.setText(Language.format(Menu.lang.getLanguage().AuthenticatorStartTitleRemove));
				desc1.setText(Language.format(Menu.lang.getLanguage().AuthenticatorStartDescAdd));
				desc2.setText(Language.format(Menu.lang.getLanguage().AuthenticatorStartDescAddManually));
				desc3.setText(Language.format(Menu.lang.getLanguage().AuthenticatorStartDescRemove));
				cancel.setText(Language.format(Menu.lang.getLanguage().ButtonCancel));
			}
		});
	}
	
	@Override
	public void onShow() {
		super.onShow();
	}
	
	@FXML
	void onCancel(){
		menu.getStage().close();
	}
	
	@FXML
	void onNewAuthenticator(){
		menu.setScene("SteamGuardSettingsAddNormal", false);
	}
	
	@FXML
	void onExistingAuthenticator(){
	
	}
	
	@FXML
	void onRemoveAuthenticator(){
		SteamUser user = launcher.getSteam().getUser(menu.getUser());
		if(user!=null && user.exists() && user.hasSteamGuard()) {
			Notification note = new Notification();
			note.setTitle("Steam");
			note.setText("Are you sure you want to remove the authenticator?");
			note.setIcon(NotificationIcon.QUESTION);
			note.addOption(Language.format(Menu.lang.getLanguage().ButtonContinue), ButtonAlignment.RIGHT, new ButtonCallback() {
				@Override
				public void onClick() {
					boolean wasSuccessful;
					try {
						launcher.getSteam().getSteamGuard().doLogin(user);
						wasSuccessful = launcher.getSteam().getSteamGuard().getSga().get(user.getUsername()).DeactivateAuthenticator();
						user.disableCredentialsFile();
					} catch(Throwable throwable) {
						throwable.printStackTrace();
						wasSuccessful = false;
					}
					if(wasSuccessful){
						Notification note = new Notification();
						note.setTitle("Steam");
						note.setText("The authenticator was successfully removed!");
						note.setIcon(NotificationIcon.INFO);
						note.addOption(ButtonOption.OK, ButtonAlignment.RIGHT, new ButtonCallback() {
							@Override
							public void onClick() {
							}
						});
						note.show();
						menu.onSuccessfullyRemoved();
					}else{
						Notification note = new Notification();
						note.setTitle("Steam");
						note.setText("Failed to remove the authenticator!");
						note.setIcon(NotificationIcon.ERROR);
						note.addOption(ButtonOption.OK, ButtonAlignment.RIGHT, new ButtonCallback() {
							@Override
							public void onClick() {
							
							}
						});
						note.show();
					}
				}
			});
			note.addOption(Language.format(Menu.lang.getLanguage().ButtonCancel), ButtonAlignment.RIGHT, new ButtonCallback() {
				@Override
				public void onClick() {
				
				}
			});
			note.show();
		}
	}
}
