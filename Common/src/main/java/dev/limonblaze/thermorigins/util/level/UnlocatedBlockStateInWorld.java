package dev.limonblaze.thermorigins.util.level;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;

/**
 * A BlockInWorld impl with a determined {@link BlockState} but with no {@link BlockPos} info.
 */
@MethodsReturnNonnullByDefault
public class UnlocatedBlockStateInWorld extends SavedBlockStateInWorld {
    
    public UnlocatedBlockStateInWorld(LevelReader level, BlockState state) {
        super(level, BlockPos.ZERO, state);
    }
    
    @Contract("-> null")
    @Override
    public BlockEntity getEntity() {
        return null;
    }
    
    @Override
    public BlockPos getPos() {
        return BlockPos.ZERO;
    }
    
}
