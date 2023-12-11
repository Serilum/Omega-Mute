package com.natamus.omegamute;

import com.natamus.collective.check.RegisterMod;
import com.natamus.omegamute.neoforge.events.NeoForgeKeyMappingRegister;
import com.natamus.omegamute.neoforge.events.NeoForgeMuteEvent;
import com.natamus.omegamute.util.Reference;
import com.natamus.omegamute.util.Util;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Reference.MOD_ID)
public class ModNeoForge {
	
	public ModNeoForge() {
		if (!FMLEnvironment.dist.equals(Dist.CLIENT)) {
			return;
		}

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::loadComplete);
		modEventBus.register(NeoForgeKeyMappingRegister.class);

		setGlobalConstants();
		ModCommon.init();

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		try {
			Util.loadSoundFile();
		} catch (Exception ex) {
			System.out.println("Something went wrong while generating the sound file. Omega Mute has been disabled.");
			return;
		}

		NeoForge.EVENT_BUS.register(NeoForgeMuteEvent.class);
	}

	private static void setGlobalConstants() {

	}
}