package com.buckydev;

import eu.midnightdust.lib.config.MidnightConfig;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityTypeIds;

public class FightzoneConfig extends MidnightConfig {

    private static final String GENERAL = "general";
    private static final String ENTITY = "entity";
    private static final String DAMAGE_TYPE = "damage_type";
    private static final String DAMAGE_ENTITY = "damage_entity";

    @Entry(category = GENERAL)
    public static boolean enabled = true;


    @Entry(category = ENTITY)
    public static boolean enableEntityBlacklist = true;

    @Entry(category = ENTITY)
    public static List<Identifier> entityBlacklist = Lists.newArrayList(
            EntityTypeIds.MANNEQUIN.identifier(), EntityTypeIds.PLAYER.identifier());

    @Entry(category = DAMAGE_TYPE)
    public static boolean enableDamageTypeBlacklist = true;

    @Entry(category = DAMAGE_TYPE)
    public static List<Identifier> damageTypeBlacklist = Lists.newArrayList(
            DamageTypes.ARROW.identifier(), DamageTypes.CAMPFIRE.identifier());

    @Entry(category = DAMAGE_TYPE)
    public static boolean damageSourceDirectDisabled = false;


    @Entry(category = DAMAGE_ENTITY)
    public static boolean enableDamageSourceEntityBlacklist = true;
    @Entry(category = DAMAGE_ENTITY)
    public static List<Identifier> damageSourceEntityBlacklist = Lists.newArrayList(
            EntityTypeIds.PLAYER.identifier()
    );


}
