package dev.limonblaze.thermorigins.power.data;

import com.google.auto.service.AutoService;
import dev.limonblaze.thermorigins.power.ModifyPostLandActionPower;
import dev.limonblaze.thermorigins.util.ActionConditionUtil;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;
import java.util.function.Function;

@AutoService(IModifyPostLandActionPowerData.class)
public class ModifyPostLandActionPowerData implements IModifyPostLandActionPowerData {
    
    @Override
    public Function<SerializableData.Instance, BiFunction<PowerType<ModifyPostLandActionPower>, LivingEntity, ModifyPostLandActionPower>> getPowerConstructorForge() {
        return data -> (type, entity) -> new ModifyPostLandActionPower(type, entity,
            ActionConditionUtil.entityActionConsumer(data.get("entity_action")),
            ActionConditionUtil.blockActionConsumer(data.get("block_action")),
            ActionConditionUtil.blockConditionPredicate(data.get("block_condition"))
        );
    }
    
}
