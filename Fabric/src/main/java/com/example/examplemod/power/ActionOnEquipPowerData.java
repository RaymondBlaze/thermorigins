package com.example.examplemod.power;

import com.example.examplemod.power.data.IActionOnEquipPowerData;
import com.google.auto.service.AutoService;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;

@AutoService(IActionOnEquipPowerData.class)
public class ActionOnEquipPowerData implements IActionOnEquipPowerData {
    @Override
    public BiFunction<PowerType<ActionOnEquipPower>, LivingEntity, ActionOnEquipPower> getPowerConstructor(SerializableData.Instance data) {
        return (type, entity) -> new ActionOnEquipPower(type, entity, data.get("slot"), data.get("item_condition"), data.get("entity_action"));
    }
}