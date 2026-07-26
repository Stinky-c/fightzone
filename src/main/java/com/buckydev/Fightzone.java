package com.buckydev;

import com.buckydev.events.PlayerEvents;
import com.buckydev.script.Executor;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.Identifier;

import net.minecraft.util.RandomSource;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlFeatures;
import org.apache.commons.jexl3.introspection.JexlPermissions;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fightzone implements ModInitializer {

    public static final String MOD_ID = "fightzone";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final RandomSource MOD_RANDOM = RandomSource.createThreadLocalInstance(7);

    public static Executor executor = new Executor();

    @Override
    public void onInitialize() {
        MidnightConfig.init(Fightzone.MOD_ID, FightzoneConfig.class);

        executor.init();

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(
                (entity, source, amount) -> Executor.allowDamage(executor, entity, source, amount));

//        ServerLivingEntityEvents.ALLOW_DAMAGE.register(PlayerEvents::allowDamage);
//        ServerLivingEntityEvents.ALLOW_DEATH.register(PlayerEvents::allowDeath);

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register(
                (_, _) -> {
                    LOGGER.info("Reloading script executor");
                    executor.clear();
                });

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((_, _, success) -> {
            if (success) {
                executor.init();
            }
        });

    }


    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

}


/*
Jexl engine
Build at mod init.

loads scripts into a store.
(Store gets reset on a reload)
Every event capture run the array of scripts.
    Script returns true for an entity to take damage.
    If a script does not return true continue to the next.

Until script store is invalidated cache event occurences
 */


