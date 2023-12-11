package com.natamus.omegamute.forge.events;

import com.natamus.omegamute.cmds.CommandOmega;
import com.natamus.omegamute.data.Constants;
import com.natamus.omegamute.events.MuteEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class ForgeMuteEvent {
    @SubscribeEvent
    public void registerCommands(RegisterClientCommandsEvent e) {
    	CommandOmega.register(e.getDispatcher());
    }

	@SubscribeEvent
	public void onSoundEvent(PlaySoundEvent e) {
		if (!MuteEvent.onSoundEvent(e.getEngine(), e.getOriginalSound())) {
			e.setSound(null);
		}
	}

	@SubscribeEvent
	public void onKey(InputEvent.Key e) {
		if (e.getAction() != 1) {
			return;
		}

		if (Constants.hotkey != null && e.getKey() == Constants.hotkey.getKey().getValue()) {
			MuteEvent.onHotkeyPress();
		}
	}
}