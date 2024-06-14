package com.natamus.omegamute;

import com.natamus.collective.fabric.callbacks.CollectiveSoundEvents;
import com.natamus.omegamute.data.Constants;
import com.natamus.omegamute.data.Variables;
import com.natamus.omegamute.events.SoundEvents;
import com.natamus.omegamute.fabric.cmds.FabricCommandOmega;
import com.natamus.omegamute.util.Util;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.world.entity.Entity;

public class ModFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {


		ClientEntityEvents.ENTITY_LOAD.register((Entity entity, ClientLevel world) -> {
			if (Variables.soundFileLoaded) {
				return;
			}

			try {
				Util.loadSoundFile();
			} catch (Exception ex) {
				Constants.logger.warn("Something went wrong while generating the sound file.");
			}

			Variables.soundFileLoaded = true;
		});

		CollectiveSoundEvents.SOUND_PLAY.register((SoundEngine soundEngine, SoundInstance soundInstance) -> {
			return SoundEvents.onSoundEvent(soundEngine, soundInstance);
		});

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			FabricCommandOmega.register(dispatcher);
		});
	}
}
