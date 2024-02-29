package com.natamus.omegamute.data;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;

public class Constants {
	public static final Minecraft mc = Minecraft.getInstance();
	public static Logger logger = LogUtils.getLogger();
}
