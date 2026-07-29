package com.buckydev.config;

import static com.buckydev.Fightzone.LOGGER;

import com.buckydev.Fightzone;
import com.buckydev.config.snakeyaml.Constructed;
import com.buckydev.config.snakeyaml.Represented;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

public final class FightzoneConfigLoader {

    private static final String CONFIG_NAME = Fightzone.MOD_ID + ".yaml";
    private static final Yaml YAML = createYaml();

    private FightzoneConfigLoader() {
    }

    private static Yaml createYaml() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setIndent(2);
        dumperOptions.setDefaultScalarStyle(ScalarStyle.JSON_SCALAR_STYLE);

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setTagInspector(
                tag -> tag.getClassName().equals(FightzoneConfig.class.getName()));

        // TODO: Tweak yaml loading

        Constructed constructed = new Constructed(FightzoneConfig.class, loaderOptions);
        Representer representer = new Represented(dumperOptions);

        return new Yaml(constructed, representer, dumperOptions, loaderOptions);
    }

    public static FightzoneConfig loadConfig() {
        Path path = getPath();
        LOGGER.debug("Loading config from {}", path);

        if (Files.notExists(path)) {
            LOGGER.debug("Config path does not exist. Loading default");
            FightzoneConfig config = new FightzoneConfig();
            saveConfig(config, false);
            return config;
        }

        try (InputStream input = Files.newInputStream(path)) {
            FightzoneConfig config = YAML.load(input);
            if (config == null) {
                LOGGER.warn("Config file at {} was empty. Returning default instance.", path);
                return new FightzoneConfig();
            }
            return config;
        } catch (IOException e) {
            LOGGER.error("Failed to load config from {}. Returning default instance.", path, e);
            return new FightzoneConfig();
        }
    }

    public static void saveConfig(FightzoneConfig config, boolean force) {
        if (!config.isSaveEnabled() || !force) {
            LOGGER.debug("Config tried to save. But saving is disabled");
            return;
        }

        Path path = getPath();
        LOGGER.debug("Saving config to {}", path);
        if (force) {
            LOGGER.warn("Config force saving!");
        }

        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (OutputStream output = Files.newOutputStream(path)) {
                YAML.dump(config, new java.io.OutputStreamWriter(output));
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save config to {}", path, e);
        }
    }

    public static Path getPath() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_NAME);
        return path;
    }


}