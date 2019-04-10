package GUI.localization;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public class Language {
	public String language = "none";
	public String name = "none";
	public String Alphabetically = "NOT SET";
	public String CreationDate = "NOT SET";
	public String AddSteamUser = "NOT SET";
	public String SteamDeveloperMode = "NOT SET";
	public String SteamPath = "NOT SET";
	public String ButtonOpenFile = "NOT SET";
	public String ButtonAddGame = "NOT SET";
	public String ButtonAdd = "NOT SET";
	public String ButtonContinue = "NOT SET";
	public String ButtonCancel = "NOT SET";
	public String ButtonReturn = "NOT SET";
	public String ButtonSearchGame = "NOT SET";
	public String ButtonSearch = "NOT SET";
	public String ButtonReplace = "NOT SET";
	public String ButtonFinish = "NOT SET";
	public String ButtonSwitch = "NOT SET";
	public String ButtonDelete = "NOT SET";
	public String ButtonRefresh = "NOT SET";
	public String ButtonCode = "NOT SET";
	public String ButtonOK = "NOT SET";
	public String ButtonYes = "NOT SET";
	public String ButtonNo = "NOT SET";
	public String GameUIAll = "NOT SET";
	public String TitleGames = "NOT SET";
	public String TitleSteamUser = "NOT SET";
	public String WindowTitleAddGame = "NOT SET";
	public String WindowTitleAddSteamUser = "NOT SET";
	public String WindowTitleEditSteamUser = "NOT SET";
	public String WindowTitleSteamApplication = "NOT SET";
	public String WindowTitleSearch = "NOT SET";
	public String WindowTitleInformation = "NOT SET";
	public String WindowTitleWarning = "NOT SET";
	public String Username = "NOT SET";
	public String Password = "NOT SET";
	public String MenuGames = "NOT SET";
	public String ChooseGame = "NOT SET";
	public String AddUserToContinue = "NOT SET";
	public String Name = "NOT SET";
	public String AppID = "NOT SET";
	public String AdvancedSettings = "NOT SET";
	public String Developer = "NOT SET";
	public String StartWith = "NOT SET";
	public String ImportLibrary = "NOT SET";
	public String ImportLibraryAll = "NOT SET";
	public String ImportLibraryOnlyPaid = "NOT SET";
	public String ImportLibraryNoThanks = "NOT SET";
	public String StartOptions = "NOT SET";
	public String Search = "NOT SET";
	public String ErrorFillAllFields = "NOT SET";
	public String ErrorUserAlreadyExists = "NOT SET";
	public String SteamUserToolTipDeleteUser = "NOT SET";
	public String SteamUserToolTipEditUser = "NOT SET";
	public String SteamUserToolTipLinkUser = "NOT SET";
	public String SteamUserToolTipMainUser = "NOT SET";
	public String SteamUserToolTipAuthenticator = "NOT SET";
	public String SteamUserLinkSuccessfulCreated = "NOT SET";
	public String SteamUserDeleteUserConfirmation = "NOT SET";
	public String SteamConfirmationEmpty = "NOT SET";
	public String StatusLoggingIn = "NOT SET";
	public String StatusFailed = "NOT SET";
	public String StatusWaiting = "NOT SET";
	public String StatusSuccessful = "NOT SET";
	public String AuthenticatorInfoEnterCapcha = "NOT SET";
	public String AuthenticatorInfoEnterEmailCode = "NOT SET";
	public String AuthenticatorInfoSteamFailed = "NOT SET";
	public String AuthenticatorInfoEnterPhoneNumber = "NOT SET";
	public String AuthenticatorInfoFailedToAddAuthenticator = "NOT SET";
	public String AuthenticatorInfoAuthenticatorNotAddedForSafety = "NOT SET";
	public String AuthenticatorInfoSMSCodeSent = "NOT SET";
	public String AuthenticatorInfoUnableToFinalize = "NOT SET";
	public String AuthenticatorInfoSteamGuardAdded = "NOT SET";
	public String AuthenticatorStartTitleAdd = "NOT SET";
	public String AuthenticatorStartTitleAddManually = "NOT SET";
	public String AuthenticatorStartTitleRemove = "NOT SET";
	public String AuthenticatorStartDescAdd = "NOT SET";
	public String AuthenticatorStartDescAddManually = "NOT SET";
	public String AuthenticatorStartDescRemove = "NOT SET";
	public String AuthenticatorNormalSteamLogin = "NOT SET";
	public String AuthenticatorNormalAddPhoneNumber = "NOT SET";
	public String AuthenticatorNormalSMSCode = "NOT SET";
	public String AuthenticatorNormalRecoveryCode = "NOT SET";
	public String AuthenticatorNormalTitle = "NOT SET";
	public String StartBattleNETGameWith = "NOT SET";
	public String AlertApplicationDeleteTile = "NOT SET";
	public String AlertApplicationDeleteMessage = "NOT SET";
	public String AlertApplicationLinkTitle = "NOT SET";
	public String AlertApplicationLinkMessage = "NOT SET";
	public String AlertApplicationLinkReplaceTitle = "NOT SET";
	public String AlertApplicationLinkReplaceMessage = "NOT SET";
	public String AlertSteamUserLinkTitle = "NOT SET";
	public String AlertSteamUserLinkMessage = "NOT SET";
	public String AlertSteamUserDeleteTitle = "NOT SET";
	public String AlertSteamUserDeleteMessage = "NOT SET";
	public String AlertSteamUserAskCreateTitle = "NOT SET";
	public String AlertSteamUserAskCreateMessage = "NOT SET";
	
	public static String format(String string){
		return string
				.replaceAll("\\(ae\\)", "ä")
				.replaceAll("\\(AE\\)", "Ä")
				.replaceAll("\\(oe\\)", "ö")
				.replaceAll("\\(OE\\)", "Ö")
				.replaceAll("\\(ue\\)", "ü")
				.replaceAll("\\(UE\\)", "Ü")
				.replaceAll("\\(ss\\)", "ß");
	}
	
	public static String format(String string, String... objects){
		return String.format(format(string), objects);
	}
}
