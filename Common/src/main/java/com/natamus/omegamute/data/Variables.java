package com.natamus.omegamute.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Variables {
	public static HashMap<String, Integer> isMutedSoundMap = new HashMap<String, Integer>();
	public static HashMap<String, Date> lastPlayedSound = new HashMap<String, Date>();

	public static boolean soundFileLoaded = false;
	public static boolean playerIsListening = false;
	public static boolean listeningToAll = false;

	public static List<String> soundsListenedTo = new ArrayList<String>();
}
