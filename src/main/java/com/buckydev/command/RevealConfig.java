package com.buckydev.command;

import static com.buckydev.Fightzone.CONFIG;

import com.buckydev.Fightzone;
import com.buckydev.config.FightzoneConfig.Script;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.util.EventResult;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class RevealConfig {


    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("reveal").executes(RevealConfig::run);
    }


    public static int run(CommandContext<CommandSourceStack> context)
            throws CommandSyntaxException {

        CommandSourceStack source = context.getSource();

        for (Script script : CONFIG.getScripts()) {
            Component name = Component.literal(script.getName()).withColor(TextColor.AQUA);

            Component action = Component.literal(script.getAction().toString())
                    .withColor(getColorForAction(script.getAction()));

            HoverEvent hoverEvent = new HoverEvent.ShowText(
                    Component.literal(script.getScript().getSourceText()));
            Component sourceComp = Component.literal(" <Source>")
                    .setStyle(Style.EMPTY.withHoverEvent(hoverEvent));

            Component message = Component.empty().append(name).append(" - ").append(action)
                    .append(sourceComp);

            source.sendSystemMessage(message);
        }

        source.sendSuccess(() -> Component.literal("Length: ")
                .append(String.valueOf(Fightzone.CONFIG.getScripts().size())), false);
        return 1;
    }


    private static TextColor getColorForAction(EventResult action) {
        return switch (action) {
            case ALLOW -> TextColor.GREEN;
            case PASS -> TextColor.YELLOW;
            case DENY -> TextColor.RED;
        };
    }


}
