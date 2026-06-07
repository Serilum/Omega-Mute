package com.natamus.omegamute.cmds;
import com.natamus.omegamute.util.Reference;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective.functions.MessageFunctions;
import com.natamus.omegamute.data.Constants;
import com.natamus.omegamute.data.Variables;
import com.natamus.omegamute.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandOmega {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("omegamute")
			.then(Commands.literal("reload")
			.executes((command) -> {
				return reload();
			}))

			.then(Commands.literal("query")
			.executes((command) -> {
				return query();
			}))

			.then(Commands.literal("listen").requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player)
			.executes((command) -> {
				return listen(false);
			}))
			.then(Commands.literal("listen").requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player)
			.then(Commands.literal("all")
			.executes((command) -> {
				return listen(true);
			})))

			.then(Commands.literal("mute")
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				return mute(StringArgumentType.getString(command, "string-contains"));
			})))

			.then(Commands.literal("cull")
			.then(Commands.argument("cull-time", IntegerArgumentType.integer(0, 3600))
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				return cull(StringArgumentType.getString(command, "string-contains"), IntegerArgumentType.getInteger(command, "cull-time"));
			}))))

			.then(Commands.literal("unmute")
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				return unmute(StringArgumentType.getString(command, "string-contains"));
			})))

			.then(Commands.literal("settings")
			.then(Commands.literal("serilum")
			.executes((command) -> {
				return setupSettings("serilum");
			})))
		);
	}

	public static int reload() {
		if (Constants.mc.player == null) {
			return 1;
		}

		MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.reloadingomegamute", ChatFormatting.DARK_GREEN);
		try {
			if (Util.loadSoundFile()) {
				MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundmapchangessuccessfully", ChatFormatting.DARK_GREEN);
			}
			else {
				MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundmapfoundgenerated", ChatFormatting.DARK_GREEN);
			}
		} catch (Exception ex) {
			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.somethingwentwrong", ChatFormatting.RED);
		}
		return 1;
	}

	public static int query() {
		HashMap<String, Integer> mutedSounds = Util.getMutedSounds();
		if (mutedSounds.size() > 0) {
			StringBuilder combined = new StringBuilder();
			for (String soundName : mutedSounds.keySet()) {
				if (!combined.toString().equals("")) {
					combined.append(", ");
				}

				Integer mutedValue = mutedSounds.get(soundName);
				if (mutedValue > 0) {
					combined.append(soundName).append("(").append(mutedValue).append(")");
				}
				else {
					combined.append(soundName);
				}
			}

			if (Constants.mc.player == null) {
				return 1;
			}

			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.followingsoundevents", ChatFormatting.DARK_GREEN);
			MessageFunctions.sendMessage(Constants.mc.player, combined.toString(), ChatFormatting.YELLOW);
		}
		else {
			if (Constants.mc.player == null) {
				return 1;
			}

			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.currentlysoundevents", ChatFormatting.DARK_GREEN);
		}

		return 1;
	}

	public static int listen(boolean listenToAll) {
		if (Constants.mc.player == null) {
			return 1;
		}

		Variables.listeningToAll = listenToAll;
		Variables.soundsListenedTo = new ArrayList<String>();

		if (Variables.playerIsListening) {
			Variables.playerIsListening = false;

			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.stoppedlisteningactive", true, ChatFormatting.DARK_GREEN);
		}
		else {
			Variables.playerIsListening = true;

			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.listeningactivesounds", true, ChatFormatting.DARK_GREEN);

			if (listenToAll) {
				MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.listeningsoundssee", ChatFormatting.GRAY);
			}
			else {
				MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.listeningsoundsonce", ChatFormatting.GRAY);
			}
		}

		return 1;
	}

	public static int mute(String wildcard) {
		List<String> muted = Util.muteWildcard(wildcard, 0);
		if (Constants.mc.player == null) {
			return 1;
		}

		if (muted.size() > 0) {
			String combined = String.join(", ", muted);
			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.byusingwildcardstring", ChatFormatting.DARK_GREEN, wildcard, muted.size());
			MessageFunctions.sendMessage(Constants.mc.player, combined, ChatFormatting.YELLOW);
			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundmapfileupdated", ChatFormatting.DARK_GREEN);
		}
		else {
			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundeventsfound", ChatFormatting.RED, wildcard);
		}

		return 1;
	}

	public static int cull(String wildcard, int culltime) {
		List<String> muted = Util.muteWildcard(wildcard, culltime);
		if (Constants.mc.player == null) {
			return 1;
		}

		if (muted.size() > 0) {
			String combined = String.join(", ", muted);
			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.byusingwildcardstringfollowingsound", ChatFormatting.DARK_GREEN, wildcard, muted.size(), culltime);
			MessageFunctions.sendMessage(Constants.mc.player, combined, ChatFormatting.YELLOW);
			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundmapfileupdated", ChatFormatting.DARK_GREEN);
		}
		else {
			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundeventsfound", ChatFormatting.RED, wildcard);
		}

		return 1;
	}

	public static int unmute(String wildcard) {
		List<String> unmuted = Util.unmuteWildcard(wildcard);
		if (Constants.mc.player == null) {
			return 1;
		}

		if (unmuted.size() > 0) {
			String combined = String.join(", ", unmuted);
			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.byusingwildcardstringfollowing", ChatFormatting.DARK_GREEN, wildcard, unmuted.size());
			MessageFunctions.sendMessage(Constants.mc.player, combined, ChatFormatting.YELLOW);
			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundmapfileupdated", ChatFormatting.DARK_GREEN);}
		else {
			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundeventsfound", ChatFormatting.RED, wildcard);
		}

		return 1;
	}

	public static int setupSettings(String identifier) {
		if (identifier.equals("serilum")) {
			cull(".step", 1); // Too many step sounds in an animal farm
			MessageFunctions.sendMessage(Constants.mc.player, " ", ChatFormatting.WHITE);
			cull(".ambient", 1); // Too many ambient sounds in an animal farm
			MessageFunctions.sendMessage(Constants.mc.player, " ", ChatFormatting.WHITE);
			mute("entity.cat.ambient");
			MessageFunctions.sendMessage(Constants.mc.player, " ", ChatFormatting.WHITE);
			mute("entity.cat.stray_ambient"); // Because my IRL cats don't like the meows.

			MessageFunctions.sendTranslatableMessage(Constants.mc.player, "collective.omegamute.message.serilumfavouritesettings", true, ChatFormatting.GOLD, Reference.NAME);
		}

		return 1;
	}
}