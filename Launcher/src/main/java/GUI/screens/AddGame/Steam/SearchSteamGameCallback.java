package GUI.screens.AddGame.Steam;

import api.GameLauncher.Steam.DBSearchResult;

/**
 * Removing of this disclaimer is forbidden.
 *
 * @author BubbleEgg
 * @verions: 1.0.0
 **/

public interface SearchSteamGameCallback {
	void onContinue(DBSearchResult result);
}
