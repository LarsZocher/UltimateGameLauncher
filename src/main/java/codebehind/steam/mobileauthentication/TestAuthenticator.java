package codebehind.steam.mobileauthentication;

import codebehind.steam.mobileauthentication.model.LoginRequest;
import codebehind.steam.mobileauthentication.model.LoginResult;
import codebehind.steam.mobileauthentication.model.LoginResultState;
import codebehind.toolbelt.JsonHelper;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Scanner;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class TestAuthenticator {
	
	public static void main(String[] args) throws Throwable {
		System.out.println("Enter user/password: ");
		Scanner s = new Scanner(System.in);
		String username = s.nextLine();
		String password = s.nextLine();
		LoginRequest req = new LoginRequest();
		req.setUsername(username);
		req.setPassword(password);
		UserLoginService loginSvc = new UserLoginService();
		LoginResult res = loginSvc.DoLogin(req);
		System.out.println("ID-0: "+res.getSteamID());
		while(res.getState().equals(LoginResultState.NEED_CAPTCHA) || res.getState().equals(LoginResultState.NEED_2FA)) {
			if(res.getState().equals(LoginResultState.NEED_CAPTCHA)) {
				System.out.println("Enter CAPTCHA from:");
				System.out.println("https://steamcommunity.com/public/captcha.php?gid=" + res.getCaptchaGID());
				
				req.setRequiresCaptcha(true);
				req.setCaptchaGID(res.getCaptchaGID());
				req.setCaptchaText(s.nextLine());
				res = loginSvc.DoLogin(req);
			}
			if(res.getState().equals(LoginResultState.NEED_EMAIL)) {
				req.setRequiresEmail(true);
				req.setEmailCode(s.nextLine());
				res = loginSvc.DoLogin(req);
			}
			if(res.getState().equals(LoginResultState.NEED_2FA)) {
				System.out.println("CODE:");
				req.setRequires2FA(true);
				req.setTwoFactorCode(s.nextLine());
				res = loginSvc.DoLogin(req);
			}
		}
		System.out.println("ID-1: "+res.getSteamID());
		System.out.println("ID: "+res.getSession().getSteamID());
		if(!res.getState().equals(LoginResultState.LOGIN_OK)) {
			System.out.println("[Steam] Login failed for user "+username+": "+res.getState().name());
			throw new Exception("Error during login!");
		}
		
		AuthenticatorLinker linker = new AuthenticatorLinker(res.getSession());
		System.out.println("Phone number:");
		linker.PhoneNumber = s.nextLine(); //Set this to non-null to add a new phone number to the account.
		AuthenticatorLinker.LinkResult result = linker.AddAuthenticator();
		
		if (result != AuthenticatorLinker.LinkResult.AwaitingFinalization)
		{
			System.out.println("Failed to add authenticator: " + result);
			return;
		}
		
		try
		{
			String sgFile = new Gson().toJson(linker.LinkedAccount.getConfig());
					//String sgFile = JsonConvert.SerializeObject(linker.LinkedAccount, Formatting.Indented);
			String fileName = linker.LinkedAccount.getConfig().getAccountName() + ".maFile";
			File file = new File(fileName);
			file.createNewFile();
			FileUtils.writeStringToFile(file, sgFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("EXCEPTION saving maFile. For security, authenticator will not be finalized.");
			return;
		}
		
		System.out.println("Please enter SMS code: ");
		String smsCode = s.nextLine();
		AuthenticatorLinker.FinalizeResult linkResult = linker.FinalizeAddAuthenticator(smsCode);
		
		if (linkResult != AuthenticatorLinker.FinalizeResult.Success)
		{
			System.out.println("Unable to finalize authenticator: " + linkResult);
		}
	}
}
