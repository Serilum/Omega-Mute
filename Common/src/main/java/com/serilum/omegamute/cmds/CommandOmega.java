package com.serilum.omegamute.cmds;
import com.serilum.omegamute.util.Reference;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective.functions.CommandConfirmFunctions;
import com.natamus.collective.functions.MessageFunctions;
import com.serilum.omegamute.data.Constants;
import com.serilum.omegamute.data.Variables;
import com.serilum.omegamute.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
			.then(Commands.argument("string-contains", StringArgumentType.greedyString())
			.executes((command) -> {
				return mute(StringArgumentType.getString(command, "string-contains"));
			})))

			.then(Commands.literal("cull")
			.then(Commands.argument("cull-time", IntegerArgumentType.integer(0, 3600))
			.then(Commands.argument("string-contains", StringArgumentType.greedyString())
			.executes((command) -> {
				return cull(StringArgumentType.getString(command, "string-contains"), IntegerArgumentType.getInteger(command, "cull-time"));
			}))))

			.then(Commands.literal("unmute")
			.then(Commands.argument("string-contains", StringArgumentType.greedyString())
			.executes((command) -> {
				return unmute(StringArgumentType.getString(command, "string-contains"));
			})))

			.then(Commands.literal("settings")
			.then(Commands.literal("serilum")
			.executes((command) -> {
				return CommandConfirmFunctions.askToConfirm(command, getSettingsWarning(), () -> applySettings("serilum"));
			})
			.then(Commands.literal("confirm")
			.executes((command) -> {
				return CommandConfirmFunctions.confirm(command);
			}))))
		);
	}

	public static int reload() {
		if (Constants.mc.player == null) {
			return 1;
		}

		MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.reloadingomegamute", ChatFormatting.DARK_GREEN);
		try {
			if (Util.loadSoundFile()) {
				MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundmapchangessuccessfully", ChatFormatting.DARK_GREEN);
			}
			else {
				MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundmapfoundgenerated", ChatFormatting.DARK_GREEN);
			}
		} catch (Exception ex) {
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.somethingwentwrong", ChatFormatting.RED);
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

			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.followingsoundevents", ChatFormatting.DARK_GREEN);
			MessageFunctions.sendClientMessage(Constants.mc.player, Component.literal(combined.toString()).withStyle(ChatFormatting.YELLOW));
		}
		else {
			if (Constants.mc.player == null) {
				return 1;
			}

			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.currentlysoundevents", ChatFormatting.DARK_GREEN);
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

			Util.sendEmptyLine();
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.stoppedlisteningactive", ChatFormatting.DARK_GREEN);
		}
		else {
			Variables.playerIsListening = true;

			Util.sendEmptyLine();
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.listeningactivesounds", ChatFormatting.DARK_GREEN);

			if (listenToAll) {
				MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.listeningsoundssee", ChatFormatting.GRAY);
			}
			else {
				MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.listeningsoundsonce", ChatFormatting.GRAY);
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
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.byusingwildcardstring", ChatFormatting.DARK_GREEN, wildcard, muted.size());
			MessageFunctions.sendClientMessage(Constants.mc.player, Component.literal(combined).withStyle(ChatFormatting.YELLOW));
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundmapfileupdated", ChatFormatting.DARK_GREEN);
		}
		else {
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundeventsfound", ChatFormatting.RED, wildcard);
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
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.byusingwildcardstringfollowingsound", ChatFormatting.DARK_GREEN, wildcard, muted.size(), culltime);
			MessageFunctions.sendClientMessage(Constants.mc.player, Component.literal(combined).withStyle(ChatFormatting.YELLOW));
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundmapfileupdated", ChatFormatting.DARK_GREEN);
		}
		else {
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundeventsfound", ChatFormatting.RED, wildcard);
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
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.byusingwildcardstringfollowing", ChatFormatting.DARK_GREEN, wildcard, unmuted.size());
			MessageFunctions.sendClientMessage(Constants.mc.player, Component.literal(combined).withStyle(ChatFormatting.YELLOW));
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundmapfileupdated", ChatFormatting.DARK_GREEN);}
		else {
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.soundeventsfound", ChatFormatting.RED, wildcard);
		}

		return 1;
	}

	public static MutableComponent getSettingsWarning() {
		return Component.translatable("collective.omegamute.message.serilumsettingswarning").withStyle(ChatFormatting.YELLOW);
	}

	public static void applySettings(String identifier) {
		if (identifier.equals("serilum")) {
			cull(".step", 1); // Too many step sounds in an animal farm
			Util.sendEmptyLine();
			cull(".ambient", 1); // Too many ambient sounds in an animal farm
			Util.sendEmptyLine();
			mute("entity.cat.ambient");
			Util.sendEmptyLine();
			mute("entity.cat.stray_ambient"); // Because my IRL cats don't like the meows.

			Util.sendEmptyLine();
			MessageFunctions.sendClientTranslatableMessage(Constants.mc.player, "collective.omegamute.message.serilumfavouritesettings", ChatFormatting.GOLD, Reference.NAME);
		}
	}
}