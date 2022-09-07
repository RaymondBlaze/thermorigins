package dev.limonblaze.thermorigins.power.data;

import dev.limonblaze.thermorigins.power.RedirectLightningPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiFunction;

public class RedirectLightningPowerData implements IPowerData<RedirectLightningPower> {
    
    @Override
    public SerializableData getSerializableData() {
        return new SerializableData()
            .add("ignore_lightning_rod", SerializableDataTypes.BOOLEAN, false)
            .add("range", SerializableDataTypes.DOUBLE)
            .add("chance", SerializableDataTypes.DOUBLE, 1.0);
    }
    
    @Override
    public BiFunction<PowerType<RedirectLightningPower>, LivingEntity, RedirectLightningPower> getPowerConstructor(SerializableData.Instance data) {
        return (type, entity) -> new RedirectLightningPower(type, entity,
            data.getBoolean("ignore_lightning_rod"),
            data.getDouble("range"),
            data.getDouble("chance")
        );
    }
    
}
