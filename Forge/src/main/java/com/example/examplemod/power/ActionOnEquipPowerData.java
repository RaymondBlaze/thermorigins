package com.example.examplemod.power;

import com.example.examplemod.power.data.IActionOnEquipPowerData;
import com.example.examplemod.util.ActionConditionUtil;
import com.google.auto.service.AutoService;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;
import java.util.function.Function;

@AutoService(IActionOnEquipPowerData.class)
public class ActionOnEquipPowerData implements IActionOnEquipPowerData {
    @Override
    public Function<SerializableData.Instance, BiFunction<PowerType<ActionOnEquipPower>, LivingEntity, ActionOnEquipPower>> getPowerConstructorForge() {
        return (data) -> (type, entity) -> new ActionOnEquipPower(type, entity, data.get("slot"), ActionConditionUtil.itemConditionPredicate(data.get("item_condition")), ActionConditionUtil.entityActionConsumer(data.get("entity_action")));
    }
}