package com.buckydev.command;

import static com.buckydev.Fightzone.CONFIG;

import com.buckydev.config.FightzoneConfig.Script;
import com.buckydev.config.FightzoneConfigLoader;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class DeleteScript {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("delete").then(Commands.argument("name", StringReader::readString)
                .suggests(DeleteScript::suggestion)).executes(RevealConfig::run);
    }

    public static CompletableFuture<Suggestions> suggestion(
            CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {

        // TODO: Cache maybe
        return CompletableFuture.supplyAsync(() -> {
            CONFIG.getScripts().stream().map(script -> script.getName())
                    .forEach(s -> builder.suggest(s));
            return builder.build();
        });


    }


    public static int run(CommandContext<CommandSourceStack> context) {

        String name = context.getArgument("name", String.class);
        List<Script> scriptList = CONFIG.getScripts();
        boolean dirty = false;

        for (int i = 0; i < scriptList.size(); i++) {
            Script script = scriptList.get(i);
            if (script.getName() != name) {
                continue;
            }

            scriptList.remove(i);
            dirty = true;

        }

        if (dirty) {
            CONFIG.setScripts(scriptList);
        }


        // Force save if editing with commands
        FightzoneConfigLoader.saveConfig(CONFIG, true);

        return 1;
    }


}
