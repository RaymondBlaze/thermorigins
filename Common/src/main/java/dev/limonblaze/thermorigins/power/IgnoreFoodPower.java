package dev.limonblaze.thermorigins.power;

import dev.limonblaze.thermorigins.access.FoodDataPlayerAccess;
import dev.limonblaze.thermorigins.mixin.FoodDataMixin;
import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.registry.ThermoPowers;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

import javax.annotation.Nullable;

/**
 * Power that removes all {@link FoodData} mechanics. <br>
 * @see FoodDataMixin
 */
public class IgnoreFoodPower extends Power {
    
    public IgnoreFoodPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
    
    public static boolean isActive(FoodDataPlayerAccess access) {
        Player player = access.getPlayer();
        return player != null && Services.PLATFORM.hasPower(player, IgnoreFoodPower.class, ThermoPowers.IGNORE_FOOD);
    }
    
}
