package dev.limonblaze.thermorigins.power.data;

import dev.limonblaze.thermorigins.platform.Services;
import dev.limonblaze.thermorigins.power.ModifyToolPower;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;

public interface IModifyToolPowerData extends IPowerData<ModifyToolPower> {
    
    @Override
    default SerializableData getSerializableData() {
        return IPowerData.super.getSerializableData()
            .add("break_speed", SerializableDataTypes.FLOAT, 1.0F)
            .add("break_speed_block_condition", Services.PLATFORM.getBlockActionDataType(), null)
            .add("correct_tool_block_condition", Services.PLATFORM.getBlockActionDataType(), null);
    }
    
}
