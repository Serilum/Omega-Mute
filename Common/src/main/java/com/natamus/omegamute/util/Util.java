package com.natamus.omegamute.util;

import com.natamus.collective.functions.DataFunctions;
import com.natamus.omegamute.events.MuteEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Util {
	// MUTED VALUE
	// -1 == no mute
	// 0 == always mute
	// > 0 == cull sound for x seconds
	
	private static final String dirpath = DataFunctions.getConfigDirectory() + File.separator + "omegamute";
	private static final File dir = new File(dirpath);
	private static final File file = new File(dirpath + File.separator + "soundmap.txt");
	
	public static boolean loadSoundFile() throws IOException {
		MuteEvent.ismutedsoundmap = new HashMap<String, Integer>();
		
		if (!dir.isDirectory() || !file.isFile()) {
			generateSoundFile();
			return false;
		}
		
		String sounds = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "soundmap.txt", new String[0])));
		for (String sound : sounds.split("\n")) {
			if (sound.length() < 4) {
				continue;
			}
			
			String soundname = sound.replace(",", "");
			int mutedvalue = -1;
			
			if (soundname.startsWith("!")) {
				mutedvalue = 0;
				soundname = soundname.substring(1);
			}
			else if (soundname.contains("-")) {
				try {
					String[] soundsplit = soundname.split("-");
					if (soundsplit.length == 2) {
						mutedvalue = Integer.parseInt(soundsplit[0]);
						soundname = soundsplit[1];
					}
				}
				catch (NumberFormatException ignored) {}
			}
			
			MuteEvent.ismutedsoundmap.put(soundname.trim(), mutedvalue);
		}
		return true;
	}
	
	private static void updateSoundFile() {
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(dirpath + File.separator + "soundmap.txt"), true);
			
			List<String> keyset = new ArrayList<String>(MuteEvent.ismutedsoundmap.keySet());
			Collections.sort(keyset);
			for (String soundname : keyset) {
				int mutedvalue = MuteEvent.ismutedsoundmap.get(soundname);
				if (mutedvalue >= 0) {
					if (mutedvalue == 0) {
						soundname = "!" + soundname;
					}
					else {
						soundname = mutedvalue + "-" + soundname;
					}
				}
				writer.println(soundname + ",");
			}

			writer.close();	
		} catch (Exception ignored) { }
	}
	
	private static void generateSoundFile() throws IOException {
		List<String> soundnames = new ArrayList<String>();
		
		Set<ResourceLocation> soundlocations = Registry.SOUND_EVENT.keySet();
		for (ResourceLocation soundlocation : soundlocations) {
			SoundEvent sound = Registry.SOUND_EVENT.get(soundlocation);
			soundnames.add(sound.getLocation().toString().replace("minecraft:", ""));
		}
		
		Collections.sort(soundnames);
		
		dir.mkdirs();
		
		PrintWriter writer = new PrintWriter(dirpath + File.separator + "soundmap.txt", StandardCharsets.UTF_8);
		for (String soundname : soundnames) {
			MuteEvent.ismutedsoundmap.put(soundname, -1);
			writer.println(soundname + ",");
		}

		writer.close();
	}
	
	public static HashMap<String, Integer> getMutedSounds() {
		HashMap<String, Integer> mutedsounds = new HashMap<String, Integer>();
		
		List<String> keyset = new ArrayList<String>(MuteEvent.ismutedsoundmap.keySet());
		Collections.sort(keyset);
		for (String soundname : keyset) {
			int mutedvalue = MuteEvent.ismutedsoundmap.get(soundname);
			if (mutedvalue >= 0) {
				mutedsounds.put(soundname, mutedvalue);
			}
		}
		
		return mutedsounds;
	}
	
	public static List<String> muteWildcard(String wildcard, int culltime) {
		if (!dir.isDirectory() || !file.isFile()) {
			try {
				generateSoundFile();
			} catch (Exception ignored) {}
		}		

		List<String> mutedsounds = new ArrayList<String>();
		
		try {
			String sounds = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "soundmap.txt", new String[0])));

			for (String sound : sounds.split("\n")) {
				if (sound.length() < 4) {
					continue;
				}
				
				String soundname = sound.replace(",", "");
				int mutedvalue = -1;
				
				if (soundname.startsWith("!")) {
					mutedvalue = 0;
					soundname = soundname.substring(1);
				}
				else if (soundname.contains("-")) {
					try {
						String[] soundsplit = soundname.split("-");
						if (soundsplit.length == 2) {
							mutedvalue = Integer.parseInt(soundsplit[0]);
							soundname = soundsplit[1];
						}
					}
					catch (NumberFormatException ignored) {}
				}
				
				if (soundname.toLowerCase().contains(wildcard.toLowerCase())) {
					mutedvalue = culltime;
					mutedsounds.add(soundname.trim());
				}
				
				MuteEvent.ismutedsoundmap.put(soundname.trim(), mutedvalue);
			}
		} catch (Exception ignored) { }
		
		if (mutedsounds.size() > 0) {
			updateSoundFile();
		}
		return mutedsounds;
	}
	
	public static List<String> unmuteWildcard(String wildcard) {
		List<String> unmutedsounds = new ArrayList<String>();
		
		try {
			String sounds = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "soundmap.txt", new String[0])));

			for (String sound : sounds.split("\n")) {
				if (sound.length() < 4) {
					continue;
				}
				
				String soundname = sound.replace(",", "");
				int mutedvalue = -1;
				
				if (soundname.startsWith("!")) {
					mutedvalue = 0;
					soundname = soundname.substring(1);
				}
				else if (soundname.contains("-")) {
					try {
						String[] soundsplit = soundname.split("-");
						if (soundsplit.length == 2) {
							mutedvalue = Integer.parseInt(soundsplit[0]);
							soundname = soundsplit[1];
						}
					}
					catch (NumberFormatException ignored) {}
				}
				
				if (soundname.toLowerCase().contains(wildcard.toLowerCase())) {
					mutedvalue = -1;
					unmutedsounds.add(soundname.trim());
				}
				
				MuteEvent.ismutedsoundmap.put(soundname.trim(), mutedvalue);
			}
		} catch (Exception ignored) { }
		
		if (unmutedsounds.size() > 0) {
			updateSoundFile();
		}
		return unmutedsounds;
	}
}