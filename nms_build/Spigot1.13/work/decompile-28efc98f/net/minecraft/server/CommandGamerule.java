package net.minecraft.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class CommandGamerule {

    public static void a(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> com_mojang_brigadier_commanddispatcher) {
        LiteralArgumentBuilder literalargumentbuilder = (LiteralArgumentBuilder) CommandDispatcher.a("gamerule").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(2);
        });
        Iterator iterator = GameRules.getGameRules().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            literalargumentbuilder.then(((LiteralArgumentBuilder) CommandDispatcher.a((String) entry.getKey()).executes((commandcontext) -> {
                return a((CommandListenerWrapper) commandcontext.getSource(), (String) entry.getKey());
            })).then(((GameRules.b) entry.getValue()).b().a("value").executes((commandcontext) -> {
                return a((CommandListenerWrapper) commandcontext.getSource(), (String) entry.getKey(), commandcontext);
            })));
        }

        com_mojang_brigadier_commanddispatcher.register(literalargumentbuilder);
    }

    private static int a(CommandListenerWrapper commandlistenerwrapper, String s, CommandContext<CommandListenerWrapper> commandcontext) {
        GameRules.GameRuleValue gamerules_gamerulevalue = commandlistenerwrapper.getServer().aQ().get(s);

        gamerules_gamerulevalue.e().a(commandcontext, "value", gamerules_gamerulevalue);
        commandlistenerwrapper.sendMessage(new ChatMessage("commands.gamerule.set", new Object[] { s, gamerules_gamerulevalue.a()}), true);
        return gamerules_gamerulevalue.c();
    }

    private static int a(CommandListenerWrapper commandlistenerwrapper, String s) {
        GameRules.GameRuleValue gamerules_gamerulevalue = commandlistenerwrapper.getServer().aQ().get(s);

        commandlistenerwrapper.sendMessage(new ChatMessage("commands.gamerule.query", new Object[] { s, gamerules_gamerulevalue.a()}), false);
        return gamerules_gamerulevalue.c();
    }
}
