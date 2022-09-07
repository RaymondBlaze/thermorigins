package dev.limonblaze.thermorigins.power.data;

import com.google.auto.service.AutoService;
import dev.limonblaze.thermorigins.power.ModifyPostLandActionPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;

@AutoService(IModifyPostLandActionPowerData.class)
public class ModifyPostLandActionPowerData implements IModifyPostLandActionPowerData {
    
    @Override
    public BiFunction<PowerType<ModifyPostLandActionPower>, LivingEntity, ModifyPostLandActionPower> getPowerConstructor(SerializableData.Instance data) {
        return (type, entity) -> new ModifyPostLandActionPower(type, entity,
            data.get("entity_action"),
            data.get("block_action"),
            data.get("block_condition")
        );
    }
    
}
