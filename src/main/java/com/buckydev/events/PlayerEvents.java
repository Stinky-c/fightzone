package com.buckydev.events;

import static com.buckydev.Fightzone.LOGGER;
import static com.buckydev.Fightzone.MOD_RANDOM;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class PlayerEvents {


    public static boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
        int id = MOD_RANDOM.nextIntBetweenInclusive(1, Integer.MAX_VALUE);
        LOGGER.warn("{}| Entity: {}, Source: {}, Amount: {}", id, entity, source, amount);
        if (!PreEventChecks.isEnabled()) {
            return true;
        }

        if (PreEventChecks.isDamageTypeBlacklisted(source)) {
            LOGGER.warn("{}| Damage type blocked", id);
            return false;
        }

        if (PreEventChecks.isEntityBlacklisted(entity)) {
            LOGGER.warn("{}| Entity block", id);
            return false;
        }

        if (PreEventChecks.isDamageSourceEntityBlacklisted(source)) {
            LOGGER.warn("{}| Entity Source block", id);
            return false;
        }

        return true; // fall through
    }

    public static boolean allowDeath(LivingEntity entity, DamageSource source, float amount) {
        return allowDamage(entity, source, amount);
    }


}
