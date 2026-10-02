package com.serilum.omegamute.neoforge.events;

import com.serilum.omegamute.cmds.CommandOmega;
import com.serilum.omegamute.data.Constants;
import com.serilum.omegamute.data.Variables;
import com.serilum.omegamute.events.SoundEvents;
import com.serilum.omegamute.util.Util;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

public class NeoForgeSoundEvents {
	@SubscribeEvent
	public static void onLevelLoad(LevelEvent.Load e) {
		if (Variables.soundFileLoaded) {
			return;
		}

		try {
			Util.loadSoundFile();
		} catch (Exception ex) {
			Constants.logger.warn("Something went wrong while generating the sound file.");
		}

		Variables.soundFileLoaded = true;
	}

	@SubscribeEvent
	public static void registerCommands(RegisterClientCommandsEvent e) {
		CommandOmega.register(e.getDispatcher());
	}

	@SubscribeEvent
	public static void onSoundEvent(PlaySoundEvent e) {
		if (!SoundEvents.onSoundEvent(e.getEngine(), e.getOriginalSound())) {
			e.setSound(null);
		}
	}
}