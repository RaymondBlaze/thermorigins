package dev.limonblaze.thermorigins.util;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

/**
 * A BlockInWorld impl with a determined {@linkplain BlockPos} and a vaguely defined {@linkplain BlockPos}
 */
@MethodsReturnNonnullByDefault
public class VaguePosBlockInWorld extends BlockInWorld {
    private final BlockState state;
    
    public VaguePosBlockInWorld(LevelReader level, BlockPos pos, BlockState state) {
        super(level, pos, true);
        this.state = state;
    }
    
    @Override
    public BlockState getState() {
        return state;
    }
    
}
