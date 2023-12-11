package com.natamus.omegamute;

import com.mojang.blaze3d.platform.InputConstants;
import com.natamus.collective.fabric.callbacks.CollectiveSoundEvents;
import com.natamus.omegamute.fabric.cmds.FabricCommandOmega;
import com.natamus.omegamute.data.Constants;
import com.natamus.omegamute.events.MuteEvent;
import com.natamus.omegamute.util.Util;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

public class ModFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Constants.hotkey = KeyBindingHelper.registerKeyBinding(new KeyMapping("omegamute.key.reload", InputConstants.Type.KEYSYM, 46, "key.categories.misc"));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (Constants.hotkey.isDown()) {
				MuteEvent.onHotkeyPress();
				Constants.hotkey.setDown(false);
			}
		}); 
		
 		try {
			Util.loadSoundFile();
		} catch (Exception ex) {
			return;
		}
 		
		CollectiveSoundEvents.SOUND_PLAY.register((SoundEngine soundEngine, SoundInstance soundInstance) -> {
			return MuteEvent.onSoundEvent(soundEngine, soundInstance);
		});

		FabricCommandOmega.register(ClientCommandManager.DISPATCHER);
	}
}
