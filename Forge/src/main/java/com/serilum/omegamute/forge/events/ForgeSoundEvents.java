package com.serilum.omegamute.forge.events;

import com.serilum.omegamute.cmds.CommandOmega;
import com.serilum.omegamute.data.Constants;
import com.serilum.omegamute.data.Variables;
import com.serilum.omegamute.events.SoundEvents;
import com.serilum.omegamute.util.Util;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.lang.invoke.MethodHandles;

public class ForgeSoundEvents {
	public static void registerEventsInBus() {
		BusGroup.DEFAULT.register(MethodHandles.lookup(), ForgeSoundEvents.class);
	}

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