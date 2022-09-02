package dev.limonblaze.thermorigins.power;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.limonblaze.thermorigins.data.Furnace;
import dev.limonblaze.thermorigins.data.ThermoDataTypes;
import dev.limonblaze.thermorigins.util.ActionConditionUtil;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.IActivePower;
import io.github.edwinmindcraft.apoli.api.power.IInventoryPower;
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
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FurnacePower extends PowerFactory<FurnacePower.Configuration> implements
    IInventoryPower<FurnacePower.Configuration>,
    IActivePower<FurnacePower.Configuration>
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
        CalioCodecHelper.optionalField(IActivePower.Key.BACKWARD_COMPATIBLE_CODEC, "key", IActivePower.Key.PRIMARY)
            .forGetter(Configuration::key)
    ).apply(instance, ((furnace, title, drop, filter, key) -> new Configuration(furnace, title.orElse(null), drop, filter, key))));
    
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
    public Furnace.Instance getInventory(ConfiguredPower<Configuration, ?> power, Entity entity) {
        Configuration config = power.getConfiguration();
        return power.getPowerData(entity, () -> config.furnace.new Instance(config.title) {
            @Override
            public void setChanged() {
                ApoliAPI.synchronizePowerContainer(entity);
            }
    
            @Override
            protected int getBurnTime(ItemStack stack) {
                return ForgeHooks.getBurnTime(stack, config.furnace.recipeType);
            }
        });
    }
    
    public Furnace.Instance getInventory(ConfiguredPower<Configuration, ?> power, IPowerContainer container) {
        Configuration config = power.getConfiguration();
        return power.getPowerData(container, () -> config.furnace.new Instance(config.title) {
            @Override
            public void setChanged() {
                container.sync();
            }
    
            @Override
            protected int getBurnTime(ItemStack stack) {
                return ForgeHooks.getBurnTime(stack, config.furnace.recipeType);
            }
        });
    }
    
    @Override
    public MenuConstructor getMenuCreator(ConfiguredPower<Configuration, ?> power, Entity entity) {
        return getInventory(power, entity);
    }
    
    @Override
    public void tick(ConfiguredPower<Configuration, ?> power, Entity entity) {
        if(!entity.level.isClientSide) {
            getInventory(power, entity).tick((LivingEntity) entity);
        }
    }
    
    @Override
    public void deserialize(ConfiguredPower<Configuration, ?> power, IPowerContainer container, CompoundTag tag) {
        getInventory(power, container).fromTag(tag);
    }
    
    @Override
    public void serialize(ConfiguredPower<Configuration, ?> power, IPowerContainer container, CompoundTag tag) {
        getInventory(power, container).toTag(tag);
    }
    
    public record Configuration(Furnace furnace,
                                @Nullable String title,
                                boolean dropOnDeath,
                                Holder<ConfiguredItemCondition<?, ?>> dropFilter,
                                IActivePower.Key key
    ) implements IDynamicFeatureConfiguration {}
    
}
