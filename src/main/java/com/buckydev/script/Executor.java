package com.buckydev.script;

import static com.buckydev.Fightzone.CONFIG;
import static com.buckydev.Fightzone.LOGGER;

import com.buckydev.Fightzone;
import com.buckydev.config.FightzoneConfig.Script;
import java.util.stream.Stream;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlFeatures;
import org.apache.commons.jexl3.JexlScript;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.introspection.JexlPermissions;
import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;

public class Executor {

    static JexlEngine engine = buildEngine();

    private static JexlEngine buildEngine() {
        JexlPermissions permissions = JexlPermissions.SECURE;
        JexlFeatures features = new JexlFeatures().loops(false);

        return new JexlBuilder().permissions(permissions).features(features).safe(false).create();
    }


    public Executor() {

    }


    public static JexlScript compileScript(String script) {
        try {
            return engine.createScript(script);

        } catch (Exception e) {
            LOGGER.error("Failed to parse script {}", e);
            throw e;
        }
    }

    public Stream<Script> engineIter(MapContext context) {

        // iter scripts. Execute with context. Filter for only booleans and true. (True=Apply action)

        return CONFIG.getScripts().stream()
                .map(script -> Pair.of(script, this.execute(script.getScript(), context)))
                .filter(o -> o.getRight() instanceof Boolean bool && bool).map(o -> o.getLeft());


    }

    private Object execute(JexlScript script, MapContext context) {
        // TODO: Script timings
        return script.execute(context);
    }


    public static boolean livingEntityEvent(LivingEntity entity, DamageSource source,
            float amount) {

        MapContext context = new MapContext();

        String target = EntityType.getKey(entity.getType()).toString();
        @Nullable String dmgSource = source.typeHolder().unwrapKey()
                .map(key -> key.identifier().toString()).orElse(null);

        context.set("target", target);
        context.set("damage_source", dmgSource);
        context.set("amount", amount);

        Stream<Script> stream = Fightzone.executor.engineIter(context);

        //TODO: more logging

        // Map matched scripts to their actions. Catch any action that is false. False cancel the event
        return stream.map(script -> script.getAction().allowAction(true))
                .filter(bool -> bool == false).findFirst().orElse(true);
    }


}
