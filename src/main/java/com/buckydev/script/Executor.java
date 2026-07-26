package com.buckydev.script;

import static com.buckydev.Fightzone.LOGGER;

import com.buckydev.script.Loader.ScriptMap;
import java.util.stream.Stream;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.JexlFeatures;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.introspection.JexlPermissions;

public class Executor {

    JexlEngine engine = buildEngine();
    Store store = new Store();
    Loader loader = new Loader();

    private static JexlEngine buildEngine() {
        JexlPermissions permissions = JexlPermissions.SECURE;
        JexlFeatures features = new JexlFeatures().loops(false);

        return new JexlBuilder().permissions(permissions).features(features).safe(false).create();
    }


    public Executor() {

    }


    public void init() {
        LOGGER.info("Initializing Executor");
        ScriptMap map = loader.init(engine);
        store.init(map);
    }

    public void clear() {

    }

    public boolean engineIter(MapContext context) {
        Stream<JexlExpression> stream = store.iter();

        // Look for any match saying to cancel damage
        return stream.anyMatch(expr -> (expr.evaluate(context) instanceof Boolean bool && !bool));

    }

    // region Player events
    public static boolean allowDamage(Executor executor, LivingEntity entity, DamageSource source,
            float amount) {
        MapContext context = new MapContext();

        String target = EntityType.getKey(entity.getType()).toString();
//        String dmgSource = source.typeHolder().unwrapKey().get().identifier(); // FIXME: get on optional

        context.set("target", target);

        return executor.engineIter(context);
    }

//    public static boolean allowDeath(LivingEntity entity, DamageSource source, float amount) {
//    }

    // endregion


}
