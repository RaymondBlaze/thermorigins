package dev.limonblaze.thermorigins.power;

import dev.limonblaze.thermorigins.data.Furnace;
import dev.limonblaze.thermorigins.data.ThermoDataTypes;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class FurnacePower extends Power implements Active {
    private final Furnace.Instance furnace;
    private final boolean shouldDropOnDeath;
    private final Predicate<ItemStack> dropOnDeathFilter;
    private Key key;
    
    public FurnacePower(PowerType<?> type, LivingEntity entity, Furnace furnace, @Nullable String containerName, boolean shouldDropOnDeath, Predicate<ItemStack> dropOnDeathFilter) {
        super(type, entity);
        this.furnace = furnace.new Instance(containerName) {
            @Override
            public void setChanged() {
                PowerHolderComponent.syncPower(entity, type);
            }
    
            @Override
            protected int getBurnTime(ItemStack stack) {
                if(stack.isEmpty()) {
                    return 0;
                } else {
                    Item item = stack.getItem();
                    return AbstractFurnaceBlockEntity.getFuel().getOrDefault(item, 0);
                }
            }
        };
        this.shouldDropOnDeath = shouldDropOnDeath;
        this.dropOnDeathFilter = dropOnDeathFilter;
        this.setTicking();
    }
    
    public boolean shouldDropOnDeath() {
        return shouldDropOnDeath;
    }
    
    public boolean shouldDropOnDeath(ItemStack stack) {
        return shouldDropOnDeath && dropOnDeathFilter.test(stack);
    }
    
    @Override
    public Key getKey() {
        return key;
    }
    
    @Override
    public void setKey(Key key) {
        this.key = key;
    }
    
    @Override
    public void onUse() {
        if(this.isActive()) {
            if (!this.entity.level.isClientSide && this.entity instanceof Player player) {
                player.openMenu(furnace);
            }
        }
    }
    
    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        furnace.toTag(tag);
        return tag;
    }
    
    @Override
    public void fromTag(Tag tag) {
        super.fromTag(tag);
        furnace.fromTag((CompoundTag) tag);
    }
    
    @Override
    public void tick() {
        if(!entity.level.isClientSide) {
            furnace.tick(entity);
        }
    }
    
    public static PowerFactory<FurnacePower> createFactory(ResourceLocation id) {
        return new PowerFactory<FurnacePower>(id, new SerializableData()
            .add("furnace_type", ThermoDataTypes.FURNACE)
            .add("title", SerializableDataTypes.STRING, null)
            .add("drop_on_death", SerializableDataTypes.BOOLEAN, false)
            .add("drop_on_death_filter", ApoliDataTypes.ITEM_CONDITION, null)
            .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Key()),
            (data) -> (type, entity) -> {
                FurnacePower power = new FurnacePower(type, entity,
                    data.get("furnace_type"),
                    data.get("title"),
                    data.get("drop_on_death"),
                    data.get("drop_on_death_filter")
                );
                power.setKey(data.get("key"));
                return power;
            }
        ).allowCondition();
    }
    
}
