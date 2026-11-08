package com.serilum.omegamute;

import com.natamus.collective.config.DuskConfigCustomFile;
import com.natamus.collective.services.Services;
import com.serilum.omegamute.util.Reference;
import com.serilum.omegamute.util.Util;

public class ModCommon {

	public static void init() {
		load();
	}

	private static void load() {
		if (!Services.MODLOADER.isClientSide()) {
			return;
		}

		DuskConfigCustomFile.addText(Reference.MOD_ID, "soundmap.txt", "Sound Map", "Every sound, one per line. A '!' in front mutes it, a number and '-' in front plays it at most once per that many seconds.", Util::readSoundMapText, Util::writeSoundMapText);
	}
}