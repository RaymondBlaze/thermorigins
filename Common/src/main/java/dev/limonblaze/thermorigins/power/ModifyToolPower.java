package dev.limonblaze.thermorigins.power;

import dev.limonblaze.thermorigins.mixin.InventoryMixin;
import dev.limonblaze.thermorigins.mixin.PlayerMixin;
import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.registry.ThermoPowers;
import dev.limonblaze.thermorigins.util.VaguePosBlockInWorld;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Power that modify player's mining behavior by imitating {@link DiggerItem}. <br>
 * {@linkplain ModifyToolPower#breakSpeed}: The break speed to set, only the highest value of all(including original speed) will be accepted. <br>
 * {@linkplain ModifyToolPower#breakSpeedCondition}: Block Condition for modify break speed, modification will apply only when block matches condition. <br>
 * {@linkplain ModifyToolPower#correctToolCondition}: Block Condition for modify correct tool check, check result will be set to {@code true} when block matches condition. <br>
 * <b>Important</b>: Avoid using {@link BlockPos} specific block conditions in this power, because {@link VaguePosBlockInWorld} is used due to no proper access to {@link BlockPos}.
 * @see InventoryMixin
 * @see PlayerMixin
 */
public class ModifyToolPower extends Power {
    private final float breakSpeed;
    @Nullable private final Predicate<BlockInWorld> breakSpeedCondition;
    @Nullable private final Predicate<BlockInWorld> correctToolCondition;
    
    public ModifyToolPower(PowerType<?> type, LivingEntity entity, float breakSpeed,
                           @Nullable Predicate<BlockInWorld> breakSpeedCondition,
                           @Nullable Predicate<BlockInWorld> correctToolCondition) {
        super(type, entity);
        this.breakSpeed = breakSpeed;
        this.breakSpeedCondition = breakSpeedCondition;
        this.correctToolCondition = correctToolCondition;
    }
    
    public static float getBreakSpeed(LivingEntity entity, BlockState state, float original) {
        BlockInWorld block = new VaguePosBlockInWorld(
            entity.level,
            entity.blockPosition(),
            state
        );
        Optional<Float> optional = Services.PLATFORM.getPowers(entity, ModifyToolPower.class, ThermoPowers.MODIFY_TOOL)
            .stream()
            .filter(power -> power.breakSpeedCondition == null || power.breakSpeedCondition.test(block))
            .map(power -> power.breakSpeed)
            .max(Comparator.naturalOrder());
        if(optional.isPresent()) {
            float result = optional.get();
            return Math.max(original, result);
        }
        return original;
    }
    
    public static boolean isCorrectTool(LivingEntity entity, BlockState state) {
        BlockInWorld block = new VaguePosBlockInWorld(
            entity.level,
            entity.blockPosition(),
            state
        );
        return Services.PLATFORM.getPowers(entity, ModifyToolPower.class, ThermoPowers.MODIFY_TOOL)
            .stream()
            .anyMatch(power -> power.correctToolCondition == null || power.correctToolCondition.test(block));
    }
    
}