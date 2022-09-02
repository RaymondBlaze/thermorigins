package dev.limonblaze.thermorigins.util;

import dev.limonblaze.thermorigins.access.ItemStackLevelAccess;
import com.mojang.datafixers.util.Pair;
import io.github.edwinmindcraft.apoli.api.power.configuration.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.material.FluidState;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionConditionUtil {
    @Contract("null -> null")
    public static Predicate<Pair<Entity, Entity>> biEntityConditionPredicate(ConfiguredBiEntityCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return (pair) -> condition.check(pair.getFirst(), pair.getSecond());
    }

    @Contract("null -> null")
    public static Predicate<Holder<Biome>> biomeConditionPredicate(ConfiguredBiomeCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return condition::check;
    }

    @Contract("null -> null")
    public static Consumer<BlockInWorld> blockCondition(ConfiguredBlockCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return (block) -> condition.check(block.getLevel(), block.getPos(), block::getState);
    }

    @Contract("null -> null")
    public static Predicate<Pair<DamageSource, Float>> damageConditionPredicate(ConfiguredDamageCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return (pair) -> condition.check(pair.getFirst(), pair.getSecond());
    }

    @Contract("null -> null")
    public static Predicate<Entity> entityConditionPredicate(ConfiguredEntityCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return condition::check;
    }

    @Contract("null -> null")
    public static Predicate<FluidState> fluidConditionPredicate(ConfiguredFluidCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return condition::check;
    }

    @Contract("null -> null")
    public static Predicate<ItemStack> itemConditionPredicate(ConfiguredItemCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return (itemStack) -> {
            if (((ItemStackLevelAccess)(Object)itemStack).thermorigins$getLevel() == null) {
                return false;
            }
            return condition.check(((ItemStackLevelAccess)(Object)itemStack).thermorigins$getLevel(), itemStack);
        };
    }

    @Contract("null -> null")
    public static Consumer<Pair<Entity, Entity>> biEntityActionConsumer(ConfiguredBiEntityAction<?, ?> action) {
        if (action == null) {
            return null;
        }
        return (pair) -> action.execute(pair.getFirst(), pair.getSecond());
    }

    @Contract("null -> null")
    public static Consumer<Triple<Level, BlockPos, Direction>> blockActionConsumer(ConfiguredBlockAction<?, ?> action) {
        if (action == null) {
            return null;
        }
        return (triple) -> action.execute(triple.getLeft(), triple.getMiddle(), triple.getRight());
    }

    @Contract("null -> null")
    public static Consumer<Entity> entityActionConsumer(ConfiguredEntityAction<?, ?> action) {
        if (action == null) {
            return null;
        }
        return action::execute;
    }

    @Contract("null -> null")
    public static Consumer<ItemStack> itemActionConsumer(ConfiguredItemAction<?, ?> action) {
        if (action == null) {
            return null;
        }
        return (stack) -> action.execute(((ItemStackLevelAccess)(Object)stack).thermorigins$getLevel(), new MutableObject<>(stack));
    }
    
}
