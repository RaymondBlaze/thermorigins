package dev.limonblaze.thermorigins.platform;

import dev.limonblaze.thermorigins.data.MultiloaderDataTypes;
import dev.limonblaze.thermorigins.platform.services.IPlatformHelper;
import dev.limonblaze.thermorigins.power.data.IPowerData;
import dev.limonblaze.thermorigins.registry.ThermoPowersForge;
import com.google.auto.service.AutoService;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.action.configuration.ChangeResourceConfiguration;
import io.github.edwinmindcraft.apoli.common.registry.action.ApoliEntityActions;
import io.github.edwinmindcraft.apoli.fabric.FabricPowerConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.List;

@AutoService(IPlatformHelper.class)
public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public <P extends Power> PowerFactory<P> registerPowerFactory(ResourceLocation id, IPowerData<P> power) {
        PowerFactory<P> powerFactory = new PowerFactory<>(id, power.getSerializableData(), power.getPowerConstructorForge()).allowCondition();
        ThermoPowersForge.REGISTRY.register(id.getPath(), powerFactory::getWrapped);
        return powerFactory;
    }

    @Override
    public <P extends Power> List<P> getPowers(LivingEntity entity, Class<P> powerClass, PowerFactory<P> powerFactory) {
        return IPowerContainer.getPowers(entity, powerFactory.getWrapped()).stream().map(configuredPower -> ((FabricPowerConfiguration<P>)configuredPower.getConfiguration()).power().apply((PowerType<P>) configuredPower.getPowerType(), entity)).toList();
    }

    @Override
    public <P extends Power> boolean hasPower(LivingEntity living, Class<P> powerClass, PowerFactory<P> powerFactory) {
        return IPowerContainer.hasPower(living, powerFactory.getWrapped());
    }
    
    @Override
    public boolean hasPowerType(LivingEntity entity, ResourceLocation resource) {
        return IPowerContainer.get(entity).filter(container -> {
            ConfiguredPower<?, ?> power = container.getPower(resource);
            return power != null && power.isActive(entity);
        }).isPresent();
    }
    
    public void changeResource(LivingEntity entity, ResourceLocation resource, int change) {
        IPowerContainer.get(entity).ifPresent(container -> {
            ConfiguredPower<?, ?> power = container.getPower(resource);
            if(power != null && power.isActive(entity)) {
                ApoliEntityActions.CHANGE_RESOURCE.get().execute(
                    new ChangeResourceConfiguration(Holder.direct(power), change, ResourceOperation.ADD),
                    entity
                );
            }
        });
    }

    @Override
    public SerializableDataType<?> getBiEntityConditionDataType() {
        return MultiloaderDataTypes.BIENTITY_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getBiomeConditionDataType() {
        return MultiloaderDataTypes.BIOME_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getBlockConditionDataType() {
        return MultiloaderDataTypes.BLOCK_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getDamageConditionDataType() {
        return MultiloaderDataTypes.DAMAGE_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getEntityConditionDataType() {
        return MultiloaderDataTypes.ENTITY_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getFluidConditionDataType() {
        return MultiloaderDataTypes.FLUID_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getItemConditionDataType() {
        return MultiloaderDataTypes.ITEM_CONDITION.get();
    }

    @Override
    public SerializableDataType<?> getBiEntityActionDataType() {
        return MultiloaderDataTypes.BIENTITY_ACTION.get();
    }

    @Override
    public SerializableDataType<?> getBlockActionDataType() {
        return MultiloaderDataTypes.BLOCK_ACTION.get();
    }

    @Override
    public SerializableDataType<?> getEntityActionDataType() {
        return MultiloaderDataTypes.ENTITY_ACTION.get();
    }

    @Override
    public SerializableDataType<?> getItemActionDataType() {
        return MultiloaderDataTypes.ITEM_ACTION.get();
    }
    
    @Override
    public int getBurnTimeForFuel(ItemStack stack, RecipeType<?> recipeType) {
        return ForgeHooks.getBurnTime(stack, recipeType);
    }
    
}
