package gui.screens.addgame.steam.steamguard;

import gui.Menu;
import gui.localization.Language;
import api.launcher.GameLauncher;
import api.launcher.steam.SteamGuardInformation;
import api.launcher.steam.SteamUser;
import codebehind.steam.mobileauthentication.AuthenticatorLinker;
import codebehind.steam.mobileauthentication.UserLoginService;
import codebehind.steam.mobileauthentication.model.LoginRequest;
import codebehind.steam.mobileauthentication.model.LoginResult;
import codebehind.steam.mobileauthentication.model.LoginResultState;
import codebehind.steam.mobileauthentication.model.SessionData;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class AuthenticatorMenuAddNormalController extends AuthenticatorInit{
	
	@FXML
	private Label status_1;
	@FXML
	private Label status_2;
	@FXML
	private Label status_3;
	@FXML
	private Label status_4;
	@FXML
	private Label code;
	@FXML
	private Label info;
	@FXML
	private Label steamLogin;
	@FXML
	private Label addPhoneNumber;
	@FXML
	private Label smsCode;
	@FXML
	private Label recoveryCode;
	@FXML
	private Label title;
	@FXML
	private TextField phone_number;
	@FXML
	private TextField sms_code;
	@FXML
	private TextField captcha_code;
	@FXML
	private ImageView captcha;
	@FXML
	private HBox box_2;
	@FXML
	private HBox box_3;
	@FXML
	private HBox box_4;
	
	private GameLauncher launcher;
	private SteamUser user;
	private AuthenticatorLinker linker;
	private SessionData session;
	private String steam64;
	
	private String captchaGID = "";
	private boolean needEmail = false;
	
	private String STATUS_LOGGING = Language.format(Menu.lang.getLanguage().StatusLoggingIn);
	private String STATUS_WAITING = Language.format(Menu.lang.getLanguage().StatusWaiting);
	private String STATUS_FAILED = Language.format(Menu.lang.getLanguage().StatusFailed);
	private String STATUS_SUCCESSFUL = Language.format(Menu.lang.getLanguage().StatusSuccessful);
	
	@Override
	public void init(AuthenticatorMenu menu) {
		super.init(menu);
		this.launcher = new GameLauncher();
		this.user = launcher.getSteam().getUser(menu.getUser());
		setStatus(status_1, STATUS_WAITING, "WHITE");
		setStatus(status_2, "-", "WHITE");
		setStatus(status_3, "-", "WHITE");
		setStatus(status_4, "-", "WHITE");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				info.setText("");
				code.setText("");
				steamLogin.setText(Language.format(Menu.lang.getLanguage().AuthenticatorNormalSteamLogin));
				addPhoneNumber.setText(Language.format(Menu.lang.getLanguage().AuthenticatorNormalAddPhoneNumber));
				smsCode.setText(Language.format(Menu.lang.getLanguage().AuthenticatorNormalSMSCode));
				recoveryCode.setText(Language.format(Menu.lang.getLanguage().AuthenticatorNormalRecoveryCode));
				title.setText(Language.format(Menu.lang.getLanguage().AuthenticatorNormalTitle));
			}
		});
		captcha.setVisible(false);
		captcha_code.setVisible(false);
		box_2.setVisible(false);
		box_3.setVisible(false);
		box_4.setVisible(false);
	}
	
	@Override
	public void onShow() {
		onCapcha();
	}
	
	public void addAuthenticator() throws Throwable {
		
		//STEAM LOGIN
		captcha.setVisible(false);
		captcha_code.setVisible(false);
		setStatus(status_1, STATUS_LOGGING, "WHITE");
		LoginRequest req = new LoginRequest();
		req.setUsername(user.getUsername());
		req.setPassword(user.getPassword());
		if(!captchaGID.isEmpty()){
			req.setRequiresCaptcha(true);
			req.setCaptchaGID(captchaGID);
			req.setCaptchaText(captcha_code.getText());
			captchaGID = "";
		}
		if(needEmail){
			req.setRequiresEmail(true);
			req.setEmailCode(captcha_code.getText());
			needEmail = false;
		}
		UserLoginService loginSvc = new UserLoginService();
		LoginResult res = loginSvc.DoLogin(req);
		if(res.getState().equals(LoginResultState.NEED_CAPTCHA)){
			System.out.println("Enter CAPTCHA");
			System.out.println("https://steamcommunity.com/public/captcha.php?gid=" + res.getCaptchaGID());
			captcha_code.setVisible(true);
			captcha.setVisible(true);
			captcha.setImage(new Image("https://steamcommunity.com/public/captcha.php?gid="+ res.getCaptchaGID()));
			captchaGID = res.getCaptchaGID();
			setStatus(info, Language.format(Menu.lang.getLanguage().AuthenticatorInfoEnterCapcha), null);
			setStatus(status_1, STATUS_WAITING, "WHITE");
			return;
		}
		if(res.getState().equals(LoginResultState.NEED_EMAIL)){
			System.out.println("Enter EMAIL_CODE");
			captcha_code.setVisible(true);
			setStatus(info, Language.format(Menu.lang.getLanguage().AuthenticatorInfoEnterEmailCode), null);
			setStatus(status_1, STATUS_WAITING, "WHITE");
			needEmail = true;
			return;
		}
		if(!res.getState().equals(LoginResultState.LOGIN_OK)) {
			System.out.println("[Steam] Login failed for user "+user.getUsername()+": "+res.getState().name());
			setStatus(status_1, STATUS_FAILED, "#bc0000");
			setStatus(info, Language.format(Menu.lang.getLanguage().AuthenticatorInfoSteamFailed, res.getState().name()), null);
			
			return;
		}
		
		//ADD PHONE NUMBER
		setStatus(status_1, STATUS_SUCCESSFUL, "-primary");
		setStatus(status_2, STATUS_WAITING, "WHITE");
		setStatus(info, Language.format(Menu.lang.getLanguage().AuthenticatorInfoEnterPhoneNumber), null);
		box_2.setVisible(true);
		captcha.setVisible(false);
		captcha_code.setVisible(false);
		
		session = res.getSession();
		steam64 = res.getSession().getSteamID()+"";
		linker = new AuthenticatorLinker(session);
	}
	
	@FXML
	void onPhone(){
		//ADD PHONE NUMBER
		
		linker = new AuthenticatorLinker(session);
		if(!phone_number.getText().isEmpty())
			linker.PhoneNumber = phone_number.getText();
		
		AuthenticatorLinker.LinkResult result = linker.AddAuthenticator();
		if (result != AuthenticatorLinker.LinkResult.AwaitingFinalization)
		{
			setStatus(status_2, STATUS_FAILED, "#bc0000");
			setStatus(info, Language.format(Menu.lang.getLanguage().AuthenticatorInfoFailedToAddAuthenticator, result.name()), null);
			System.out.println("Failed to add authenticator: " + result.name());
			return;
		}
		
		try
		{
			SteamGuardInformation credentials = new SteamGuardInformation();
			credentials.setAccount_name(linker.LinkedAccount.getConfig().getAccountName());
			credentials.setDevice_id(linker.LinkedAccount.getConfig().getDeviceID());
			credentials.setIdentity_secret(linker.LinkedAccount.getConfig().getIdentitySecret());
			credentials.setRevocation_code(linker.LinkedAccount.getConfig().getRevocationCode());
			credentials.setSecret_1(linker.LinkedAccount.getConfig().getSecret1());
			credentials.setSerial_number(linker.LinkedAccount.getConfig().getSerialNumber());
			credentials.setServer_time(linker.LinkedAccount.getConfig().getServerTime()+"");
			credentials.setShared_secret(linker.LinkedAccount.getConfig().getSharedSecret());
			credentials.setStatus(linker.LinkedAccount.getConfig().getStatus());
			credentials.setSteamguard_scheme("2");
			credentials.setSteamid(steam64);
			credentials.setToken_gid(linker.LinkedAccount.getConfig().getTokenGID());
			credentials.setUri(linker.LinkedAccount.getConfig().getUri());
			user.addSteamGuard(credentials);
			
			String userPath = System.getProperty("user.home");
			File backupFolder = new File(userPath + "/UltimateGameLauncher");
			if(!backupFolder.exists())
				backupFolder.mkdir();
			String sgFile = new Gson().toJson(linker.LinkedAccount.getConfig());
			File file = new File(backupFolder + "/" + linker.LinkedAccount.getConfig().getAccountName()+ ".backup");
			for(int i = 0; i<20; i++) {
				String fileName = linker.LinkedAccount.getConfig().getAccountName()+i+ ".backup";
				file = new File(backupFolder + "/" + fileName);
				if(!file.exists())
					break;
			}
			file.createNewFile();
			FileUtils.writeStringToFile(file, sgFile);
			
			setStatus(code, credentials.getRevocation_code(), null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("EXCEPTION saving backup. For security, authenticator will not be finalized.");
			setStatus(info, Language.format(Menu.lang.getLanguage().AuthenticatorInfoAuthenticatorNotAddedForSafety), null);
			setStatus(status_2, STATUS_FAILED, "#bc0000");
			return;
		}
		System.out.println("Please enter SMS code: ");
		setStatus(status_2, STATUS_SUCCESSFUL, "-primary");
		setStatus(status_3, STATUS_WAITING, "WHITE");
		setStatus(info, Language.format(Menu.lang.getLanguage().AuthenticatorInfoSMSCodeSent), null);
		phone_number.setEditable(false);
		box_3.setVisible(true);
	}
	
	@FXML
	void onSMS(){
		//CHECK SMS
		if(sms_code.getText().isEmpty())
			return;
		String smsCode = sms_code.getText();
		AuthenticatorLinker.FinalizeResult linkResult;
		try {
			linkResult = linker.FinalizeAddAuthenticator(smsCode);
		} catch(Throwable throwable) {
			throwable.printStackTrace();
			user.disableCredentialsFile();
			return;
		}
		
		if (linkResult != AuthenticatorLinker.FinalizeResult.Success)
		{
			System.out.println("Unable to finalize authenticator: " + linkResult.name());
			setStatus(info, Language.format(Menu.lang.getLanguage().AuthenticatorInfoUnableToFinalize, linkResult.name()), null);
			setStatus(status_3, STATUS_FAILED, "#bc0000");
			
			user.disableCredentialsFile();
			return;
		}
		sms_code.setEditable(false);
		box_4.setVisible(true);
		setStatus(status_3, STATUS_SUCCESSFUL, "-primary");
		setStatus(status_4, STATUS_SUCCESSFUL, "-primary");
		setStatus(info, Language.format(Menu.lang.getLanguage().AuthenticatorInfoSteamGuardAdded), null);
		menu.onSuccessfullyAdded();
	}
	
	
	@FXML
	void onCapcha(){
		new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					addAuthenticator();
				} catch(Throwable throwable) {
					throwable.printStackTrace();
				}
				return null;
			}
		}).start();
	}
	
	public void setStatus(Label label, String status, String color){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				label.setText(status);
				if(color!=null)
					label.setStyle("-fx-text-fill: "+color);
			}
		});
	}
	
	@FXML
	void onCancel(){
		menu.setScene("SteamGuardSettingsStart", true);
	}
	
}
