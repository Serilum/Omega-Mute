package com.natamus.omegamute.cmds;

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

		MessageFunctions.sendMessage(Constants.mc.player, "Reloading the omega mute soundmap file now.", ChatFormatting.DARK_GREEN);
		try {
			if (Util.loadSoundFile()) {
				MessageFunctions.sendMessage(Constants.mc.player, "New soundmap changes successfully loaded.", ChatFormatting.DARK_GREEN);
			}
			else {
				MessageFunctions.sendMessage(Constants.mc.player, "No soundmap found, a new one has been generated.", ChatFormatting.DARK_GREEN);
			}
		} catch (Exception ex) {
			MessageFunctions.sendMessage(Constants.mc.player, "Something went wrong while loading the soundmap file.", ChatFormatting.RED);
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

			MessageFunctions.sendMessage(Constants.mc.player, "The following sound events are currently muted:", ChatFormatting.DARK_GREEN);
			MessageFunctions.sendMessage(Constants.mc.player, combined.toString(), ChatFormatting.YELLOW);
		}
		else {
			if (Constants.mc.player == null) {
				return 1;
			}

			MessageFunctions.sendMessage(Constants.mc.player, "There are currently no sound events muted.", ChatFormatting.DARK_GREEN);
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

			MessageFunctions.sendMessage(Constants.mc.player, "You have stopped listening to the active sounds. To toggle it on use '/omegamute listen' again.", ChatFormatting.DARK_GREEN, true);
		}
		else {
			Variables.playerIsListening = true;

			MessageFunctions.sendMessage(Constants.mc.player, "You are now listening to the active sounds. To toggle it off use '/omegamute listen' again.", ChatFormatting.DARK_GREEN, true);

			if (listenToAll) {
				MessageFunctions.sendMessage(Constants.mc.player, "Listening to all sounds. To only see sounds once, use '/omegamute listen'", ChatFormatting.GRAY);
			}
			else {
				MessageFunctions.sendMessage(Constants.mc.player, "Listening to sounds once. To see all sound occurences, use '/omegamute listen all'", ChatFormatting.GRAY);
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
			MessageFunctions.sendMessage(Constants.mc.player, "By using the wildcard string '" + wildcard + "', the following " + muted.size() + " sound events have been muted:", ChatFormatting.DARK_GREEN);
			MessageFunctions.sendMessage(Constants.mc.player, combined, ChatFormatting.YELLOW);
			MessageFunctions.sendMessage(Constants.mc.player, "The soundmap file has been updated.", ChatFormatting.DARK_GREEN);
		}
		else {
			MessageFunctions.sendMessage(Constants.mc.player, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", ChatFormatting.RED);
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
			MessageFunctions.sendMessage(Constants.mc.player, "By using the wildcard string '" + wildcard + "', the following " + muted.size() + " sound events have been muted with a cull-time of " + culltime + ":", ChatFormatting.DARK_GREEN);
			MessageFunctions.sendMessage(Constants.mc.player, combined, ChatFormatting.YELLOW);
			MessageFunctions.sendMessage(Constants.mc.player, "The soundmap file has been updated.", ChatFormatting.DARK_GREEN);
		}
		else {
			MessageFunctions.sendMessage(Constants.mc.player, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", ChatFormatting.RED);
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
			MessageFunctions.sendMessage(Constants.mc.player, "By using the wildcard string '" + wildcard + "', the following " + unmuted.size() + " sound events have been unmuted:", ChatFormatting.DARK_GREEN);
			MessageFunctions.sendMessage(Constants.mc.player, combined, ChatFormatting.YELLOW);
			MessageFunctions.sendMessage(Constants.mc.player, "The soundmap file has been updated.", ChatFormatting.DARK_GREEN);}
		else {
			MessageFunctions.sendMessage(Constants.mc.player, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", ChatFormatting.RED);
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

			MessageFunctions.sendMessage(Constants.mc.player, "Serilum's favourite Omega Mute settings have been set.", ChatFormatting.GOLD, true);
		}

		return 1;
	}
}