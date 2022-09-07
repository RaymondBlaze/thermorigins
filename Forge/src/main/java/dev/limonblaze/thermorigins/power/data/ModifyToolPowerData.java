package dev.limonblaze.thermorigins.power.data;

import com.google.auto.service.AutoService;
import dev.limonblaze.thermorigins.power.ModifyToolPower;
import dev.limonblaze.thermorigins.util.ActionConditionUtil;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;
import java.util.function.Function;

@AutoService(IModifyToolPowerData.class)
public class ModifyToolPowerData implements IModifyToolPowerData {
    
    @Override
    public Function<SerializableData.Instance, BiFunction<PowerType<ModifyToolPower>, LivingEntity, ModifyToolPower>> getPowerConstructorForge() {
        return data -> (type, entity) -> new ModifyToolPower(type, entity,
            data.getFloat("break_speed"),
            ActionConditionUtil.blockConditionPredicate(data.get("break_speed_block_condition")),
            ActionConditionUtil.blockConditionPredicate(data.get("correct_tool_block_condition"))
        );
    }
    
}
