package dev.limonblaze.thermorigins.condition.block;

import dev.limonblaze.thermorigins.data.ForgeDataTypes;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.BlockCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TierHarvestableBlockCondition extends BlockCondition<FieldConfiguration<Tier>> {
    
    public TierHarvestableBlockCondition() {
        super(FieldConfiguration.codec(ForgeDataTypes.TIER, "tier"));
    }
    
    @Override
    protected boolean check(FieldConfiguration<Tier> config, LevelReader reader, BlockPos position, NonNullSupplier<BlockState> stateGetter) {
        return TierSortingRegistry.isCorrectTierForDrops(config.value(), stateGetter.get());
    }
    
}
