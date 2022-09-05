package dev.limonblaze.thermorigins.power;

import dev.limonblaze.thermorigins.mixin.FoodDataMixin;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;

/**
 * Power that removes all {@link FoodData} mechanics. <br>
 * @see FoodDataMixin
 */
public class PreventFoodUpdatePower extends Power {
    
    public PreventFoodUpdatePower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }
    
}
