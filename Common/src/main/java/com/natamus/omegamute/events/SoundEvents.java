package com.natamus.omegamute.events;

import com.natamus.collective.functions.MessageFunctions;
import com.natamus.omegamute.data.Constants;
import com.natamus.omegamute.data.Variables;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Date;

public class SoundEvents {
	public static boolean onSoundEvent(SoundEngine soundEngine, SoundInstance soundInstance) {	
		String rawName = soundInstance.getLocation().toString();
		String prefix = "";
		
		boolean canPlay = true;
		if (Variables.isMutedSoundMap.containsKey(rawName)) {
			int mutedValue = Variables.isMutedSoundMap.get(rawName);
			if (mutedValue >= 0) {
				if (mutedValue == 0) {
					prefix = "(muted) ";
					canPlay = false;
				}
				else {
					Date now = new Date();
					
					boolean replace = true;
					if (Variables.lastPlayedSound.containsKey(rawName)) {
						Date then = Variables.lastPlayedSound.get(rawName);

						long ms = (now.getTime()-then.getTime());
						if (ms < mutedValue * 1000L) {
							replace = false;
							prefix = "(" + mutedValue + "-culled-muted) ";
							canPlay = false;
						}
					}
					
					if (replace) {
						Variables.lastPlayedSound.put(rawName, now);
						prefix = "(" + mutedValue + "-culled-allowed) ";
					}
				}
			}
		}

		if (Variables.playerIsListening && Constants.mc.player != null && !rawName.endsWith(".hat")) {
			String[] nSpl = rawName.split(":");

			MutableComponent formattedNameComponent;
			if (nSpl.length == 2) {
				formattedNameComponent = Component.literal(nSpl[0]).withStyle(ChatFormatting.DARK_AQUA).append(Component.literal(":").withStyle(ChatFormatting.GRAY).append(Component.literal(nSpl[1]).withStyle(ChatFormatting.DARK_GREEN)));
			}
			else {
				formattedNameComponent = Component.literal(nSpl[0]).withStyle(ChatFormatting.DARK_AQUA);
			}

			formattedNameComponent = Component.literal(prefix).withStyle(ChatFormatting.GRAY).append(formattedNameComponent);

			if (!Variables.listeningToAll) {
				if (Variables.soundsListenedTo.contains(rawName)) {
					return canPlay;
				}

				Variables.soundsListenedTo.add(rawName);
			}

			MessageFunctions.sendMessage(Constants.mc.player, formattedNameComponent);
		}

		return canPlay;
	}
}