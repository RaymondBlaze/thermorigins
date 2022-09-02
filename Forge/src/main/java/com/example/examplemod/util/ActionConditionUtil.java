package com.example.examplemod.util;

import com.example.examplemod.access.ItemStackLevelAccess;
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

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionConditionUtil {
    @Nullable public static Predicate<Pair<Entity, Entity>> biEntityConditionPredicate(ConfiguredBiEntityCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return (pair) -> condition.check(pair.getFirst(), pair.getSecond());
    }

    @Nullable public static Predicate<Holder<Biome>> biomeConditionPredicate(ConfiguredBiomeCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return condition::check;
    }

    @Nullable public static Consumer<BlockInWorld> blockCondition(ConfiguredBlockCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return (block) -> condition.check(block.getLevel(), block.getPos(), block::getState);
    }

    @Nullable public static Predicate<Pair<DamageSource, Float>> damageConditionPredicate(ConfiguredDamageCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return (pair) -> condition.check(pair.getFirst(), pair.getSecond());
    }

    @Nullable public static Predicate<Entity> entityConditionPredicate(ConfiguredEntityCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return condition::check;
    }

    @Nullable public static Predicate<FluidState> fluidConditionPredicate(ConfiguredFluidCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return condition::check;
    }

    @Nullable public static Predicate<ItemStack> itemConditionPredicate(ConfiguredItemCondition<?, ?> condition) {
        if (condition == null) {
            return null;
        }
        return (itemStack) -> {
            if (((ItemStackLevelAccess)(Object)itemStack).yourmodid$getLevel() == null) {
                return false;
            }
            return condition.check(((ItemStackLevelAccess)(Object)itemStack).yourmodid$getLevel(), itemStack);
        };
    }

    @Nullable public static Consumer<Pair<Entity, Entity>> biEntityActionConsumer(ConfiguredBiEntityAction<?, ?> action) {
        if (action == null) {
            return null;
        }
        return (pair) -> action.execute(pair.getFirst(), pair.getSecond());
    }

    @Nullable public static Consumer<Triple<Level, BlockPos, Direction>> blockActionConsumer(ConfiguredBlockAction<?, ?> action) {
        if (action == null) {
            return null;
        }
        return (triple) -> action.execute(triple.getLeft(), triple.getMiddle(), triple.getRight());
    }

    @Nullable public static Consumer<Entity> entityActionConsumer(ConfiguredEntityAction<?, ?> action) {
        if (action == null) {
            return null;
        }
        return action::execute;
    }

    @Nullable public static Consumer<ItemStack> itemActionConsumer(ConfiguredItemAction<?, ?> action) {
        if (action == null) {
            return null;
        }
        return (stack) -> action.execute(((ItemStackLevelAccess)(Object)stack).yourmodid$getLevel(), new MutableObject<>(stack));
    }
}
