package com.natamus.omegamute;

import com.natamus.collective.check.RegisterMod;
import com.natamus.omegamute.forge.events.ForgeKeyMappingRegister;
import com.natamus.omegamute.forge.events.ForgeMuteEvent;
import com.natamus.omegamute.util.Reference;
import com.natamus.omegamute.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Reference.MOD_ID)
public class ModForge {
	
	public ModForge() {
		if (!FMLEnvironment.dist.equals(Dist.CLIENT)) {
			return;
		}

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::loadComplete);
		modEventBus.register(new ForgeKeyMappingRegister());

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

    	MinecraftForge.EVENT_BUS.register(new ForgeMuteEvent());
	}

	private static void setGlobalConstants() {

	}
}