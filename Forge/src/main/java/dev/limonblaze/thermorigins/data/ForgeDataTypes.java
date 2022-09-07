package dev.limonblaze.thermorigins.data;

import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.TierSortingRegistry;

public class ForgeDataTypes {
    
    public static final SerializableDataType<Tier> TIER = SerializableDataType.wrap(
        Tier.class,
        SerializableDataTypes.IDENTIFIER,
        tier -> {
            ResourceLocation id = TierSortingRegistry.getName(tier);
            return id == null ? new ResourceLocation("wood") : id;
        },
        id -> {
            Tier tier = TierSortingRegistry.byName(id);
            return Tiers.WOOD;
        }
    );
    
    public static final SerializableDataType<ConfiguredSoundEvent> CONFIGURED_SOUND_EVENT =
        new SerializableDataType<>(ConfiguredSoundEvent.class, ConfiguredSoundEvent.CODEC);
    
    public static final SerializableDataType<WeightedConfiguredSoundEvent> WEIGHTED_CONFIGURED_SOUND_EVENT =
        new SerializableDataType<>(WeightedConfiguredSoundEvent.class, WeightedConfiguredSoundEvent.CODEC);
    
}
