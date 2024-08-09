package com.natamus.omegamute.forge.events;

import com.natamus.omegamute.cmds.CommandOmega;
import com.natamus.omegamute.data.Constants;
import com.natamus.omegamute.data.Variables;
import com.natamus.omegamute.events.SoundEvents;
import com.natamus.omegamute.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class ForgeSoundEvents {
	@SubscribeEvent
	public void onLevelLoad(LevelEvent.Load e) {
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
    public void registerCommands(RegisterClientCommandsEvent e) {
    	CommandOmega.register(e.getDispatcher());
    }

	@SubscribeEvent
	public void onSoundEvent(PlaySoundEvent e) {
		if (!SoundEvents.onSoundEvent(e.getEngine(), e.getOriginalSound())) {
			e.setSound(null);
		}
	}
}