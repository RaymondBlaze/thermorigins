package dev.limonblaze.thermorigins.power;

import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.registry.ThermoPowers;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ModifyPostLandActionPower extends Power {
    @Nullable private final Consumer<Entity> entityAction;
    @Nullable private final Consumer<Triple<Level, BlockPos, Direction>> blockAction;
    @Nullable private final Predicate<BlockInWorld> blockCondition;
    
    public ModifyPostLandActionPower(PowerType<?> type, LivingEntity entity,
                                     @Nullable Consumer<Entity> entityAction,
                                     @Nullable Consumer<Triple<Level, BlockPos, Direction>> blockAction,
                                     @Nullable Predicate<BlockInWorld> blockCondition) {
        super(type, entity);
        this.entityAction = entityAction;
        this.blockAction = blockAction;
        this.blockCondition = blockCondition;
    }
    
    public static void executeActions(LivingEntity entity) {
        List<ModifyPostLandActionPower> powers = Services.PLATFORM.getPowers(entity, ModifyPostLandActionPower.class, ThermoPowers.MODIFY_POST_LAND_ACTION);
        if(powers.size() > 0) {
            BlockPos pos = entity.getOnPos();
            BlockInWorld blockConditionData = new BlockInWorld(entity.level, pos, true);
            Triple<Level, BlockPos, Direction> blockActionData = Triple.of(entity.level, pos, Direction.UP);
            for(ModifyPostLandActionPower power : powers) {
                if(power.blockCondition == null || power.blockCondition.test(blockConditionData)) {
                    power.blockAction.accept(blockActionData);
                    power.entityAction.accept(entity);
                }
            }
        }
    }
    
}
