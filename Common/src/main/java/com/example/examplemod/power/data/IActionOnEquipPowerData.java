package com.example.examplemod.power.data;

import com.example.examplemod.platform.Services;
import com.example.examplemod.power.ActionOnEquipPower;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;

public interface IActionOnEquipPowerData extends IPowerData<ActionOnEquipPower> {
    @Override
    default SerializableData getSerializableData() {
        return new SerializableData()
                .add("slot", SerializableDataTypes.EQUIPMENT_SLOT)
                .add("item_condition", Services.PLATFORM.getItemConditionDataType(), null)
                .add("entity_action", Services.PLATFORM.getEntityActionDataType());
    }
}