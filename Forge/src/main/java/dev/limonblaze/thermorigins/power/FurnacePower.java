package dev.limonblaze.thermorigins.power;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.limonblaze.thermorigins.data.FurnaceFactory;
import dev.limonblaze.thermorigins.data.ThermoDataTypes;
import dev.limonblaze.thermorigins.util.ActionConditionUtil;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.IInventoryPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.calio.api.network.CalioCodecHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FurnacePower extends PowerFactory<FurnacePower.Configuration> implements
    IActivePower<FurnacePower.Configuration>,
    IInventoryPower<FurnacePower.Configuration>
{
    public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ThermoDataTypes.FURNACE.fieldOf("furnace_type")
            .forGetter(Configuration::furnace),
        CalioCodecHelper.optionalField(Codec.STRING, "title")
            .forGetter(x -> Optional.ofNullable(x.title)),
        CalioCodecHelper.optionalField(CalioCodecHelper.BOOL, "drop_on_death", false)
            .forGetter(Configuration::dropOnDeath),
        ConfiguredItemCondition.optional("drop_on_death_filter")
            .forGetter(Configuration::dropFilter),
        ConfiguredEntityAction.optional("active_action")
            .forGetter(Configuration::activeAction),
        ConfiguredEntityAction.optional("idle_action")
            .forGetter(Configuration::idleAction),
        CalioCodecHelper.optionalField(IActivePower.Key.BACKWARD_COMPATIBLE_CODEC, "key", IActivePower.Key.PRIMARY)
            .forGetter(Configuration::key)
    ).apply(instance, (furnace, title, drop, filter, activeAction, idleAction, key) ->
        new Configuration(furnace, title.orElse(null), drop, filter, activeAction, idleAction, key)
    ));
    
    public FurnacePower() {
        super(CODEC);
        this.ticking();
    }
    
    @Override
    public void activate(ConfiguredPower<Configuration, ?> power, Entity entity) {
        if(!entity.level.isClientSide() && entity instanceof Player player) {
            if(power.isActive(entity)) {
                player.openMenu(this.getInventory(power, entity));
            }
        }
    }
    
    @Override
    public Key getKey(ConfiguredPower<Configuration, ?> power, @Nullable Entity entity) {
        return power.getConfiguration().key;
    }
    
    @Override
    public boolean shouldDropOnDeath(ConfiguredPower<Configuration, ?> power, Entity entity, ItemStack itemStack) {
        return ActionConditionUtil.itemConditionPredicate(power.getConfiguration().dropFilter.value()).test(itemStack);
    }
    
    @Override
    public boolean shouldDropOnDeath(ConfiguredPower<Configuration, ?> power, Entity entity) {
        return power.getConfiguration().dropOnDeath;
    }
    
    @Override
    public FurnaceFactory.Instance getInventory(ConfiguredPower<Configuration, ?> power, Entity entity) {
        Configuration config = power.getConfiguration();
        return power.getPowerData(entity, () -> config.furnace.createInstance(config.title));
    }
    
    public FurnaceFactory.Instance getInventory(ConfiguredPower<Configuration, ?> power, IPowerContainer container) {
        Configuration config = power.getConfiguration();
        return power.getPowerData(container, () -> config.furnace.createInstance(config.title));
    }
    
    @Override
    public MenuConstructor getMenuCreator(ConfiguredPower<Configuration, ?> power, Entity entity) {
        return getInventory(power, entity);
    }
    
    @Override
    public void deserialize(ConfiguredPower<Configuration, ?> power, IPowerContainer container, CompoundTag tag) {
        getInventory(power, container).fromTag(tag);
    }
    
    @Override
    public void serialize(ConfiguredPower<Configuration, ?> power, IPowerContainer container, CompoundTag tag) {
        getInventory(power, container).toTag(tag);
    }
    
    @Override
    public void tick(ConfiguredPower<Configuration, ?> power, Entity entity) {
        if(!entity.level.isClientSide && power.isActive(entity)) {
            Configuration config = power.getConfiguration();
            FurnaceFactory.Instance furnace = getInventory(power, entity);
            if(furnace.tick((LivingEntity) entity)) {
                ConfiguredEntityAction.execute(config.activeAction, entity);
            } else {
                ConfiguredEntityAction.execute(config.idleAction, entity);
            }
            if(furnace.getChanged()) ApoliAPI.synchronizePowerContainer(entity);
        }
    }
    
    public record Configuration(FurnaceFactory furnace,
                                @Nullable String title,
                                boolean dropOnDeath,
                                Holder<ConfiguredItemCondition<?, ?>> dropFilter,
                                Holder<ConfiguredEntityAction<?, ?>> activeAction,
                                Holder<ConfiguredEntityAction<?, ?>> idleAction,
                                IActivePower.Key key
    ) implements IDynamicFeatureConfiguration {}
    
}
