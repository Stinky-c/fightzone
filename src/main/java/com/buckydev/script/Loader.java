package com.buckydev.script;


import static com.buckydev.Fightzone.LOGGER;

import com.buckydev.Fightzone;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;

public class Loader {

    private int loadCounter = 0;

    protected Loader() {
    }

    protected ScriptMap init(JexlEngine engine) {

        return load(engine);
    }

    public ScriptMap load(JexlEngine engine) {

        loadCounter += 1;
        Path searchPath = FabricLoader.getInstance().getConfigDir().resolve(Fightzone.MOD_ID);
        ScriptMap map = new ScriptMap();

        try {
            Files.createDirectories(searchPath);
        } catch (IOException e) {
            LOGGER.error("Failed to create", e);
        }

        if (Files.notExists(searchPath)) {
            return map;
        }
        try {
            List<Exception> exceptions = new ArrayList<>();
            FileReaderUtil.readFiles(searchPath, false).forEach(((path, s) -> {
                try {
                    LOGGER.debug("Discovered expression at {}");
                    JexlExpression expr = scriptLoad(engine, s);
                    ExpressionMeta meta = new ExpressionMeta(path, this.loadCounter);

                    map.put(meta, expr);

                } catch (Exception e) {
                    exceptions.add(e);
                }
            }));
        } catch (Exception e) {
            LOGGER.error("Failed to read expression files", e);
        }

        return map;
    }

    // TODO: Maybe validate
    private JexlExpression scriptLoad(JexlEngine engine, String expression) {
        return engine.createExpression(expression);
    }


    record ExpressionMeta(Path path, int version) {

    }

    static class ScriptMap extends ConcurrentHashMap<ExpressionMeta, JexlExpression> {

    }

    public class FileReaderUtil {

        /**
         * Reads files from a directory into strings.
         *
         * @param directory directory to scan
         * @param recursive if true, include subdirectories; if false, only top-level files
         * @return map of file path -> file content
         * @throws IOException if listing/reading fails
         */
        public static Map<Path, String> readFiles(Path directory, boolean recursive)
                throws IOException {
            Map<Path, String> result = new LinkedHashMap<>();

            if (!Files.isDirectory(directory)) {
                throw new IllegalArgumentException("Not a directory: " + directory);
            }

            if (!recursive) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                    for (Path path : stream) {
                        if (Files.isRegularFile(path)) {
                            result.put(path, Files.readString(path, StandardCharsets.UTF_8));
                        }
                    }
                }
            } else {
                try (Stream<Path> stream = Files.walk(directory)) {
                    stream.filter(Files::isRegularFile).forEach(path -> {
                        try {
                            result.put(path, Files.readString(path, StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
                } catch (UncheckedIOException e) {
                    throw e.getCause();
                }
            }

            return result;
        }
    }

}
