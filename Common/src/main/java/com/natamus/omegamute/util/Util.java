package com.natamus.omegamute.util;

import com.natamus.collective.functions.DataFunctions;
import com.natamus.collective.functions.NumberFunctions;
import com.natamus.omegamute.data.Constants;
import com.natamus.omegamute.data.Variables;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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
		Variables.isMutedSoundMap = new HashMap<String, Integer>();
		
		if (!dir.isDirectory() || !file.isFile()) {
			if (!generateSoundFile()) {
				return false;
			}
		}
		
		String sounds = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "soundmap.txt")));
		for (String sound : sounds.split("\n")) {
			if (sound.length() < 4) {
				continue;
			}
			
			String soundName = sound.replace(",", "");
			int mutedValue = -1;
			
			if (soundName.startsWith("!")) {
				mutedValue = 0;
				soundName = soundName.substring(1);
			}
			else if (soundName.contains("-")) {
				try {
					String[] soundSplit = soundName.split("-");
					if (soundSplit.length == 2) {
						if (NumberFunctions.isNumeric(soundSplit[0])) {
							mutedValue = Integer.parseInt(soundSplit[0]);
						}

						soundName = soundSplit[1];
					}
				}
				catch (NumberFormatException ignored) {}
			}
			
			Variables.isMutedSoundMap.put(soundName.trim(), mutedValue);
		}
		return true;
	}
	
	private static void updateSoundFile() {
		try {
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
	
	private static boolean generateSoundFile() throws IOException {
		if (Constants.mc.level == null) {
			return false;
		}

		Registry<SoundEvent> soundEventRegistry = Constants.mc.level.registryAccess().registryOrThrow(Registries.SOUND_EVENT);

		List<String> soundNames = new ArrayList<String>();

		for (ResourceLocation soundlocation : soundEventRegistry.keySet()) {
			SoundEvent sound = soundEventRegistry.get(soundlocation);
			soundNames.add(sound.getLocation().toString());
		}
		
		Collections.sort(soundNames);
		
		dir.mkdirs();
		
		PrintWriter writer = new PrintWriter(dirpath + File.separator + "soundmap.txt", StandardCharsets.UTF_8);
		for (String soundName : soundNames) {
			Variables.isMutedSoundMap.put(soundName, -1);
			writer.println(soundName + ",");
		}

		writer.close();

		return true;
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
	
	public static List<String> muteWildcard(String wildcard, int culltime) {
		List<String> mutedSounds = new ArrayList<String>();

		if (!dir.isDirectory() || !file.isFile()) {
			try {
				if (!generateSoundFile()) {
					return mutedSounds;
				}
			}
			catch (IOException ex) {
				return mutedSounds;
			}
		}
		
		try {
			String sounds = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "soundmap.txt")));

			for (String sound : sounds.split("\n")) {
				if (sound.length() < 4) {
					continue;
				}
				
				String soundName = sound.replace(",", "");
				int mutedValue = -1;
				
				if (soundName.startsWith("!")) {
					mutedValue = 0;
					soundName = soundName.substring(1);
				}
				else if (soundName.contains("-")) {
					try {
						String[] soundSplit = soundName.split("-");
						if (soundSplit.length == 2) {
							if (NumberFunctions.isNumeric(soundSplit[0])) {
								mutedValue = Integer.parseInt(soundSplit[0]);
							}

							soundName = soundSplit[1];
						}
					}
					catch (NumberFormatException ignored) {}
				}
				
				if (soundName.toLowerCase().contains(wildcard.toLowerCase())) {
					mutedValue = culltime;
					mutedSounds.add(soundName.trim());
				}
				
				Variables.isMutedSoundMap.put(soundName.trim(), mutedValue);
			}
		} catch (Exception ignored) { }
		
		if (mutedSounds.size() > 0) {
			updateSoundFile();
		}
		return mutedSounds;
	}
	
	public static List<String> unmuteWildcard(String wildcard) {
		List<String> unmutedSounds = new ArrayList<String>();
		
		try {
			String sounds = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "soundmap.txt")));

			for (String sound : sounds.split("\n")) {
				if (sound.length() < 4) {
					continue;
				}
				
				String soundName = sound.replace(",", "");
				int mutedValue = -1;
				
				if (soundName.startsWith("!")) {
					mutedValue = 0;
					soundName = soundName.substring(1);
				}
				else if (soundName.contains("-")) {
					try {
						String[] soundSplit = soundName.split("-");
						if (soundSplit.length == 2) {
							if (NumberFunctions.isNumeric(soundSplit[0])) {
								mutedValue = Integer.parseInt(soundSplit[0]);
							}

							soundName = soundSplit[1];
						}
					}
					catch (NumberFormatException ignored) {}
				}
				
				if (soundName.toLowerCase().contains(wildcard.toLowerCase())) {
					mutedValue = -1;
					unmutedSounds.add(soundName.trim());
				}
				
				Variables.isMutedSoundMap.put(soundName.trim(), mutedValue);
			}
		} catch (Exception ignored) { }
		
		if (unmutedSounds.size() > 0) {
			updateSoundFile();
		}
		return unmutedSounds;
	}
}