package com.natamus.omegamute.neoforge.events;

import com.natamus.omegamute.cmds.CommandOmega;
import com.natamus.omegamute.data.Constants;
import com.natamus.omegamute.events.MuteEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class NeoForgeMuteEvent {
	@SubscribeEvent
	public static void registerCommands(RegisterClientCommandsEvent e) {
		CommandOmega.register(e.getDispatcher());
	}

	@SubscribeEvent
	public static void onSoundEvent(PlaySoundEvent e) {
		if (!MuteEvent.onSoundEvent(e.getEngine(), e.getOriginalSound())) {
			e.setSound(null);
		}
	}

	@SubscribeEvent
	public static void onKey(InputEvent.Key e) {
		if (e.getAction() != 1) {
			return;
		}

		if (Constants.hotkey != null && e.getKey() == Constants.hotkey.getKey().getValue()) {
			MuteEvent.onHotkeyPress();
		}
	}
}