package com.serilum.omegamute.util;

import com.natamus.collective.functions.DataFunctions;
import com.natamus.collective.functions.MessageFunctions;
import com.serilum.omegamute.data.Constants;
import com.serilum.omegamute.data.Variables;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Util {
	private static final String dirpath = DataFunctions.getConfigDirectory() + File.separator + "omegamute";
	private static final File dir = new File(dirpath);
	private static final File file = new File(dirpath + File.separator + "soundmap.txt");

	public static boolean loadSoundFile() throws IOException {
		boolean soundFileExists = file.isFile();

		HashMap<String, Integer> soundMap = new HashMap<>();
		if (soundFileExists) {
			soundMap = readSoundFile();
		}

		boolean addedNewSounds = addMissingRegisteredSounds(soundMap);
		Variables.isMutedSoundMap = soundMap;

		if (addedNewSounds) {
			updateSoundFile();
		}
		return soundFileExists;
	}

	private static HashMap<String, Integer> readSoundFile() throws IOException {
		HashMap<String, Integer> soundMap = new HashMap<>();
		String sounds = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "soundmap.txt")));

		for (String line : sounds.split("\n")) {
			String soundEntry = line.replace(",", "").trim();
			if (soundEntry.length() < 4) {
				continue;
			}

			soundMap.put(getSoundName(soundEntry), getMutedValue(soundEntry));
		}

		return soundMap;
	}

	private static String getSoundName(String soundEntry) {
		if (soundEntry.startsWith("!")) {
			return soundEntry.substring(1);
		}

		if (hasCullTime(soundEntry)) {
			return soundEntry.split("-", 2)[1];
		}
		return soundEntry;
	}

	private static int getMutedValue(String soundEntry) {
		if (soundEntry.startsWith("!")) {
			return 0;
		}

		if (hasCullTime(soundEntry)) {
			return Integer.parseInt(soundEntry.split("-", 2)[0]);
		}
		return -1;
	}

	private static boolean hasCullTime(String soundEntry) {
		String[] soundSplit = soundEntry.split("-", 2);
		return soundSplit.length == 2 && soundSplit[0].matches("\\d+");
	}

	private static boolean addMissingRegisteredSounds(HashMap<String, Integer> soundMap) {
		if (Constants.mc.level == null) {
			return false;
		}

		Registry<SoundEvent> soundEventRegistry = Constants.mc.level.registryAccess().registryOrThrow(Registries.SOUND_EVENT);

		boolean addedSound = false;
		for (SoundEvent soundEvent : soundEventRegistry) {
			String soundName = soundEvent.getLocation().toString();
			if (!soundMap.containsKey(soundName)) {
				soundMap.put(soundName, -1);
				addedSound = true;
			}
		}

		return addedSound;
	}

	private static void updateSoundFile() {
		try {
			dir.mkdirs();

			PrintWriter writer = new PrintWriter(new FileOutputStream(dirpath + File.separator + "soundmap.txt"), true);

			List<String> keySet = new ArrayList<String>(Variables.isMutedSoundMap.keySet());

			Collections.sort(keySet);

			for (String soundName : keySet) {
				int mutedValue = Variables.isMutedSoundMap.get(soundName);
				if (mutedValue >= 0) {
					if (mutedValue == 0) {
						soundName = "!" + soundName;
					}
					else {
						soundName = mutedValue + "-" + soundName;
					}
				}
				writer.println(soundName + ",");
			}

			writer.close();
		} catch (Exception ignored) { }
	}

	public static HashMap<String, Integer> getMutedSounds() {
		HashMap<String, Integer> mutedSounds = new HashMap<String, Integer>();

		List<String> keySet = new ArrayList<String>(Variables.isMutedSoundMap.keySet());

		Collections.sort(keySet);

		for (String soundName : keySet) {
			int mutedValue = Variables.isMutedSoundMap.get(soundName);
			if (mutedValue >= 0) {
				mutedSounds.put(soundName, mutedValue);
			}
		}

		return mutedSounds;
	}

	public static List<String> muteWildcard(String wildcard, int cullTime) {
		return setMutedValueOfMatchingSounds(wildcard, cullTime);
	}

	public static List<String> unmuteWildcard(String wildcard) {
		return setMutedValueOfMatchingSounds(wildcard, -1);
	}

	private static List<String> setMutedValueOfMatchingSounds(String wildcard, int mutedValue) {
		List<String> matchingSounds = new ArrayList<>();

		String query = wildcard.trim().toLowerCase().replace(" ", "_");
		if (query.isEmpty()) {
			return matchingSounds;
		}

		try {
			loadSoundFile();
		}
		catch (Exception ignored) {
			return matchingSounds;
		}

		for (String soundName : Variables.isMutedSoundMap.keySet()) {
			if (soundName.toLowerCase().contains(query)) {
				matchingSounds.add(soundName);
			}
		}

		for (String soundName : matchingSounds) {
			Variables.isMutedSoundMap.put(soundName, mutedValue);
		}

		if (!matchingSounds.isEmpty()) {
			updateSoundFile();
		}

		Collections.sort(matchingSounds);
		return matchingSounds;
	}

	public static void sendEmptyLine() {
		MessageFunctions.sendClientMessage(Constants.mc.player, Component.literal(""));
	}
}
