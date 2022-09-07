package dev.limonblaze.thermorigins.power.data;

import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.power.ModifyPostLandActionPower;
import io.github.apace100.calio.data.SerializableData;

public interface IModifyPostLandActionPowerData extends IPowerData<ModifyPostLandActionPower> {
    
    @Override
    default SerializableData getSerializableData() {
        return new SerializableData()
            .add("entity_action", Services.PLATFORM.getEntityActionDataType(), null)
            .add("block_action", Services.PLATFORM.getBlockActionDataType(), null)
            .add("block_condition", Services.PLATFORM.getBlockConditionDataType(), null);
    }
    
}
