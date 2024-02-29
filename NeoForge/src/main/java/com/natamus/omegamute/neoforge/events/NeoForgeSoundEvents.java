package com.natamus.omegamute.neoforge.events;

import com.natamus.omegamute.cmds.CommandOmega;
import com.natamus.omegamute.data.Constants;
import com.natamus.omegamute.data.Variables;
import com.natamus.omegamute.events.SoundEvents;
import com.natamus.omegamute.util.Util;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(value = Dist.CLIENT)
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