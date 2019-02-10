package codebehind.steam.mobileauthentication;

import codebehind.okhttp.NameValuePairList;
import codebehind.okhttp.cookies.SimpleCookieJar;
import codebehind.steam.mobileauthentication.model.SessionData;
import codebehind.toolbelt.JsonHelper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class AuthenticatorLinker
{
	/// <summary>
	/// Set to register a new phone number when linking. If a phone number is not set on the account, this must be set. If a phone number is set on the account, this must be null.
	/// </summary>
	public String PhoneNumber = null;
	
	/// <summary>
	/// Randomly-generated device ID. Should only be generated once per linker.
	/// </summary>
	public String DeviceID;
	
	/// <summary>
	/// After the initial link step, if successful, this will be the SteamGuard data for the account. PLEASE save this somewhere after generating it; it's vital data.
	/// </summary>
	public SteamGuardAccount LinkedAccount;
	
	/// <summary>
	/// True if the authenticator has been fully finalized.
	/// </summary>
	public boolean Finalized = false;
	
	private SessionData _session;
	private SimpleCookieJar _cookies;
	
	public AuthenticatorLinker(SessionData session)
	{
		this._session = session;
		this.DeviceID = GenerateDeviceID();
		
		this._cookies = new SimpleCookieJar();
		session.addCookies(_cookies);
	}
	
	public LinkResult AddAuthenticator()
	{
		boolean hasPhone = _hasPhoneAttached();
		if (hasPhone && PhoneNumber != null)
			return LinkResult.MustRemovePhoneNumber;
		if (!hasPhone && PhoneNumber == null)
			return LinkResult.MustProvidePhoneNumber;
		
		if (!hasPhone)
		{
			if (!_addPhoneNumber())
			{
				return LinkResult.GeneralFailure;
			}
		}
		
		NameValuePairList postData = new NameValuePairList();
		postData.add("access_token", _session.getOAuthToken());
		postData.add("steamid", _session.getSteamID()+"");
		postData.add("authenticator_type", "1");
		postData.add("device_identifier", this.DeviceID);
		postData.add("sms_phone_id", "1");
		
		String response = null;
		try {
			response = SteamWeb.MobileLoginRequest(APIEndpoints.STEAMAPI_BASE + "/ITwoFactorService/AddAuthenticator/v0001", "POST", postData, null, null);
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		}
		if (response == null) return LinkResult.GeneralFailure;
		
		response = response.replace("{\"response\":", "").replace("}}", "}");
		SteamGuardAccount account = null;
		try {
			account = new SteamGuardAccount(JsonHelper.Deserialize(SteamGuardAccount.Config.class, response));
		} catch(Exception e) {
			e.printStackTrace();
		}
		if (account == null)
		{
			return LinkResult.GeneralFailure;
		}
		if (account.getConfig().getStatus() == 29)
		{
			return LinkResult.AuthenticatorPresent;
		}
		if (account.getConfig().getStatus() != 1)
		{
			return LinkResult.GeneralFailure;
		}
		
		this.LinkedAccount = account;
		this.LinkedAccount.setSession(this._session);
		this.LinkedAccount.getConfig().setDeviceID(DeviceID);
		
		return LinkResult.AwaitingFinalization;
	}
	
	public FinalizeResult FinalizeAddAuthenticator(String smsCode) throws Throwable {
		//The act of checking the SMS code is necessary for Steam to finalize adding the phone number to the account.
		//Of course, we only want to check it if we're adding a phone number in the first place...
		
		if (!this._checkSMSCode(smsCode)&&(this.PhoneNumber!=null))
		{
			return FinalizeResult.BadSMSCode;
		}
		
		System.out.println("Whoo");
		
		NameValuePairList postData = new NameValuePairList();
		postData.add("steamid", _session.getSteamID()+"");
		postData.add("access_token", _session.getOAuthToken());
		postData.add("activation_code", smsCode);
		int tries = 0;
		while (tries <= 30)
		{
			postData.set("authenticator_code", LinkedAccount.GenerateSteamGuardCode());
			postData.set("authenticator_time", TimeAligner.GetSteamTime()+"");
			
			String response = SteamWeb.MobileLoginRequest(APIEndpoints.STEAMAPI_BASE + "/ITwoFactorService/FinalizeAddAuthenticator/v0001", "POST", postData, null, null);
			if (response == null) return FinalizeResult.GeneralFailure;
			
			FinalizeAuthenticatorResponse finalizeResponse = null;
			try {
				finalizeResponse = JsonHelper.Deserialize(FinalizeAuthenticatorResponse.class, response);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			if (finalizeResponse == null || finalizeResponse.Response == null)
			{
				return FinalizeResult.GeneralFailure;
			}
			
			if (finalizeResponse.Response.Status == 89)
			{
				return FinalizeResult.BadSMSCode;
			}
			
			if (finalizeResponse.Response.Status == 88)
			{
				if (tries >= 30)
				{
					return FinalizeResult.UnableToGenerateCorrectCodes;
				}
			}
			
			if (!finalizeResponse.Response.Success)
			{
				return FinalizeResult.GeneralFailure;
			}
			
			if (finalizeResponse.Response.WantMore)
			{
				tries++;
				continue;
			}
			
			this.LinkedAccount.getConfig().setFullyEnrolled(true);
			return FinalizeResult.Success;
		}
		
		return FinalizeResult.GeneralFailure;
	}
	
	private boolean _checkSMSCode(String smsCode)
	{
		NameValuePairList postData = new NameValuePairList();
		postData.add("op", "check_sms_code");
		postData.add("arg", smsCode);
		postData.add("checkfortos", "0");
		postData.add("skipvoip", "1");
		postData.add("sessionid", _session.getSessionID());
		
		String response = null;
		try {
			response = SteamWeb.DoRequest(APIEndpoints.COMMUNITY_BASE + "/steamguard/phoneajax", "POST", postData, null, null, _cookies, null);
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		}
		if (response == null) return false;
		
		AddPhoneResponse addPhoneNumberResponse = null;
		try {
			addPhoneNumberResponse = JsonHelper.Deserialize(AddPhoneResponse.class, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if (!addPhoneNumberResponse.Success)
		{
			try {
				Thread.sleep(3500); //It seems that Steam needs a few seconds to finalize the phone number on the account.
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			return _hasPhoneAttached();
		}
		
		return true;
	}
	
	private boolean _addPhoneNumber()
	{
		NameValuePairList postData = new NameValuePairList();
		postData.add("op", "add_phone_number");
		postData.add("arg", PhoneNumber);
		postData.add("sessionid", _session.getSessionID());
		
		String response = null;
		try {
			response = SteamWeb.DoRequest(APIEndpoints.COMMUNITY_BASE + "/steamguard/phoneajax", "POST", postData, null, null, _cookies, null);
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		}
		if (response == null) return false;
		
		AddPhoneResponse addPhoneNumberResponse = null;
		try {
			addPhoneNumberResponse = JsonHelper.Deserialize(AddPhoneResponse.class, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return addPhoneNumberResponse.Success;
	}
	
	public boolean _hasPhoneAttached()
	{
		NameValuePairList postData = new NameValuePairList();
		postData.add("op", "has_phone");
		postData.add("arg", "null");
		postData.add("sessionid", _session.getSessionID());
		
		String response = null;
		try {
			response = SteamWeb.DoRequest(APIEndpoints.COMMUNITY_BASE + "/steamguard/phoneajax", "POST", postData, null, null, _cookies, null);
		} catch(Throwable throwable) {
			throwable.printStackTrace();
		}
		if (response == null) return false;
		
		HasPhoneResponse hasPhoneResponse = null;
		try {
			hasPhoneResponse = JsonHelper.Deserialize(HasPhoneResponse.class, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return hasPhoneResponse.HasPhone;
	}
	
	public enum LinkResult
	{
		MustProvidePhoneNumber, //No phone number on the account
		MustRemovePhoneNumber, //A phone number is already on the account
		AwaitingFinalization, //Must provide an SMS code
		GeneralFailure, //General failure (really now!)
		AuthenticatorPresent
	}
	
	public enum FinalizeResult
	{
		BadSMSCode,
		UnableToGenerateCorrectCodes,
		Success,
		GeneralFailure
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class AddAuthenticatorResponse
	{
		@JsonProperty(value="response")
		public SteamGuardAccount Response;
		
		public SteamGuardAccount getResponse() {
			return Response;
		}
		
		public void setResponse(SteamGuardAccount response) {
			Response = response;
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class FinalizeAuthenticatorResponse
	{
		@JsonIgnoreProperties(value="response")
		public FinalizeAuthenticatorInternalResponse Response;
		
		public FinalizeAuthenticatorInternalResponse getResponse() {
			return Response;
		}
		
		public void setResponse(FinalizeAuthenticatorInternalResponse response) {
			Response = response;
		}
		
		@JsonIgnoreProperties(ignoreUnknown=true)
		public static class FinalizeAuthenticatorInternalResponse {
			@JsonProperty(value="status")
			public int Status;
			
			@JsonProperty(value="server_time")
			public long ServerTime;
			
			@JsonProperty(value="want_more")
			public boolean WantMore;
			
			@JsonProperty(value="success")
			public boolean Success;
			
			public int getStatus() {
				return Status;
			}
			
			public void setStatus(int status) {
				Status = status;
			}
			
			public long getServerTime() {
				return ServerTime;
			}
			
			public void setServerTime(long serverTime) {
				ServerTime = serverTime;
			}
			
			public boolean isWantMore() {
				return WantMore;
			}
			
			public void setWantMore(boolean wantMore) {
				WantMore = wantMore;
			}
			
			public boolean isSuccess() {
				return Success;
			}
			
			public void setSuccess(boolean success) {
				Success = success;
			}
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class HasPhoneResponse
	{
		@JsonProperty(value="has_phone")
		public boolean HasPhone;
		
		public boolean isHasPhone() {
			return HasPhone;
		}
		
		public void setHasPhone(boolean hasPhone) {
			HasPhone = hasPhone;
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class AddPhoneResponse
	{
		@JsonProperty(value="success")
		public boolean Success;
		
		public boolean isSuccess() {
			return Success;
		}
		
		public void setSuccess(boolean success) {
			Success = success;
		}
	}
	
	public static String GenerateDeviceID()
	{
		try{
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			SecureRandom random = new SecureRandom();
			byte[] randomBytes = new byte[8];
			random.nextBytes(randomBytes);
			
			byte[] hashedBytes = sha1.digest(randomBytes);
			String random32 = bytesToHex(hashedBytes).replace("-", "").substring(0, 32).toLowerCase();
			
			return "android:" + SplitOnRatios(random32, new int[] { 8, 4, 4, 4, 12 }, "-");
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	private static String SplitOnRatios(String str, int[] ratios, String intermediate)
	{
		String result = "";
		
		int pos = 0;
		for (int index = 0; index < ratios.length; index++)
		{
			result += str.substring(pos, ratios[index]+pos);
			pos += ratios[index];
			
			if (index < ratios.length - 1)
				result += intermediate;
		}
		
		return result;
	}
}
