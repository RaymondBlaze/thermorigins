package dev.limonblaze.thermorigins.power.data;

import com.google.auto.service.AutoService;
import dev.limonblaze.thermorigins.power.ModifyToolPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;

@AutoService(IModifyToolPowerData.class)
public class ModifyToolPowerData implements IModifyToolPowerData {
    
    @Override
    public BiFunction<PowerType<ModifyToolPower>, LivingEntity, ModifyToolPower> getPowerConstructor(SerializableData.Instance data) {
        return (type, entity) -> new ModifyToolPower(type, entity,
            data.getFloat("break_speed"),
            data.get("break_speed_block_condition"),
            data.get("correct_tool_block_condition")
        );
    }
    
}
