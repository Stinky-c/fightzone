package com.buckydev.events;

import static com.buckydev.Fightzone.LOGGER;

import com.buckydev.FightzoneConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;

public class PreEventChecks {


    /// Check if entire mod is enabled
    ///
    /// @return
    public static boolean isEnabled() {
        return FightzoneConfig.enabled;
    }

    public static boolean isEntityBlacklisted(LivingEntity entity) {
        try {
            if (!FightzoneConfig.enableEntityBlacklist) {
                return false;
            }

            var holder = entity.typeHolder();
            return FightzoneConfig.entityBlacklist.stream().anyMatch(
                    v -> holder.is(v));
        } catch (Exception e) {
            LOGGER.error("Error in entity blacklist check", e);
            return false;
        }
    }

    public static boolean isDamageTypeBlacklisted(DamageSource source) {
        try {

            if (!FightzoneConfig.enableDamageTypeBlacklist) {
                return false;
            }

            var holder = source.typeHolder();
            return FightzoneConfig.damageTypeBlacklist.stream().anyMatch(v -> holder.is(v));
        } catch (Exception e) {
            LOGGER.error("Error in damage type blacklist check", e);
            return false;
        }
    }

    public static boolean isDamageSourceEntityBlacklisted(DamageSource source) {
        try {

            if (!FightzoneConfig.enableDamageSourceEntityBlacklist) {
                return false;
            }

            // If damage source does not have a causing entity, skip
            var causingEntity = source.getEntity();
            if (causingEntity == null ){
                LOGGER.debug("Skipped damage source without causing entity {}", source);
                return false;
            }
            var holder = causingEntity.typeHolder();
            return FightzoneConfig.damageSourceEntityBlacklist.stream().anyMatch(v -> holder.is(v));
        } catch (Exception e) {
            LOGGER.error("Error in source entity blacklist check", e);
            return false;

        }
    }

    public static boolean isDirectDamageDisabled() {
        return FightzoneConfig.damageSourceDirectDisabled;
    }
}
