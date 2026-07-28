package com.buckydev;

import com.buckydev.config.FightzoneConfig;
import com.buckydev.config.FightzoneConfigLoader;
import com.buckydev.script.Executor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fightzone implements ModInitializer {

    public static final String MOD_ID = "fightzone";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static FightzoneConfig CONFIG = FightzoneConfigLoader.loadConfig();

    public static Executor executor = new Executor();

    @Override
    public void onInitialize() {

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(Executor::livingEntityEvent);
        ServerLivingEntityEvents.ALLOW_DEATH.register(Executor::livingEntityEvent);

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> {
            LOGGER.info("Reloading config!");
            CONFIG = FightzoneConfigLoader.loadConfig();
        });

        // Save config
        ServerLifecycleEvents.BEFORE_SAVE.register(
                (server, flush, force) -> FightzoneConfigLoader.saveConfig(CONFIG, false));

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


