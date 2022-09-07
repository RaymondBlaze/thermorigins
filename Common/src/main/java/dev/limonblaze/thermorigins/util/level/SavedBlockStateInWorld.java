package dev.limonblaze.thermorigins.util.level;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

/**
 * A BlockInWorld impl with a determined {@link BlockState}.
 */
@MethodsReturnNonnullByDefault
public class SavedBlockStateInWorld extends BlockInWorld {
    private final BlockState state;
    
    public SavedBlockStateInWorld(LevelReader level, BlockPos pos, BlockState state) {
        super(level, pos, true);
        this.state = state;
    }
    
    @Override
    public BlockState getState() {
        return state;
    }
    
}
