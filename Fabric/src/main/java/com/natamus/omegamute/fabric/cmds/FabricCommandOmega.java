package com.natamus.omegamute.fabric.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.omegamute.cmds.CommandOmega;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.world.entity.player.Player;

public class FabricCommandOmega {
	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommands.literal("omegamute")
			.then(ClientCommands.literal("reload")
			.executes((command) -> {
                return CommandOmega.reload();
			}))

			.then(ClientCommands.literal("query")
			.executes((command) -> {
				return CommandOmega.query();
			}))

			.then(ClientCommands.literal("listen").requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player)
			.executes((command) -> {
				return CommandOmega.listen(false);
			}))
			.then(ClientCommands.literal("listen").requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player)
			.then(ClientCommands.literal("all")
			.executes((command) -> {
				return CommandOmega.listen(true);
			})))

			.then(ClientCommands.literal("mute")
			.then(ClientCommands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				return CommandOmega.mute(StringArgumentType.getString(command, "string-contains"));
			})))

			.then(ClientCommands.literal("cull")
			.then(ClientCommands.argument("cull-time", IntegerArgumentType.integer(0, 3600))
			.then(ClientCommands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				return CommandOmega.cull(StringArgumentType.getString(command, "string-contains"), IntegerArgumentType.getInteger(command, "cull-time"));
			}))))

			.then(ClientCommands.literal("unmute")
			.then(ClientCommands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
                return CommandOmega.unmute(StringArgumentType.getString(command, "string-contains"));
			})))

			.then(ClientCommands.literal("settings")
			.then(ClientCommands.literal("serilum")
			.executes((command) -> {
				return CommandOmega.setupSettings("serilum");
			})))
		);
	}
}
