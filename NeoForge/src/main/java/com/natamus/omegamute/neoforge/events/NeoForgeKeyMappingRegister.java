package com.natamus.omegamute.neoforge.events;

import com.natamus.omegamute.data.Constants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class NeoForgeKeyMappingRegister {
	@SubscribeEvent
	public static void registerKeyBinding(RegisterKeyMappingsEvent e) {
		Constants.hotkey = new KeyMapping("omegamute.key.reload", 46, "key.categories.misc");
		e.register(Constants.hotkey);
	}
}