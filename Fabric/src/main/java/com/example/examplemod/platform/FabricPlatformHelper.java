package com.example.examplemod.platform;

import com.example.examplemod.platform.services.IPlatformHelper;
import com.example.examplemod.power.data.IPowerData;
import com.google.auto.service.AutoService;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableDataType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

@AutoService(IPlatformHelper.class)
public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <P extends Power> PowerFactory<P> registerPowerFactory(ResourceLocation id, IPowerData power) {
        return Registry.register(ApoliRegistries.POWER_FACTORY, id, power.createFabricFactory(id));
    }

    @Override
    public <P extends Power> List<P> getPowers(LivingEntity entity, Class<P> powerClass, PowerFactory<P> powerFactory) {
        return PowerHolderComponent.getPowers(entity, powerClass);
    }

    @Override
    public <P extends Power> boolean hasPower(LivingEntity entity, Class<P> powerClass, PowerFactory<P> powerFactory) {
        return PowerHolderComponent.hasPower(entity, powerClass);
    }

    @Override
    public SerializableDataType<?> getBiEntityConditionDataType() {
        return ApoliDataTypes.BIENTITY_CONDITION;
    }

    @Override
    public SerializableDataType<?> getBiomeConditionDataType() {
        return ApoliDataTypes.BIOME_CONDITION;
    }

    @Override
    public SerializableDataType<?> getBlockConditionDataType() {
        return ApoliDataTypes.BLOCK_CONDITION;
    }

    @Override
    public SerializableDataType<?> getDamageConditionDataType() {
        return ApoliDataTypes.DAMAGE_CONDITION;
    }

    @Override
    public SerializableDataType<?> getEntityConditionDataType() {
        return ApoliDataTypes.ENTITY_CONDITION;
    }

    @Override
    public SerializableDataType<?> getFluidConditionDataType() {
        return ApoliDataTypes.FLUID_CONDITION;
    }

    @Override
    public SerializableDataType<?> getItemConditionDataType() {
        return ApoliDataTypes.ITEM_CONDITION;
    }

    @Override
    public SerializableDataType<?> getBiEntityActionDataType() {
        return ApoliDataTypes.BIENTITY_ACTION;
    }

    @Override
    public SerializableDataType<?> getBlockActionDataType() {
        return ApoliDataTypes.BLOCK_ACTION;
    }

    @Override
    public SerializableDataType<?> getEntityActionDataType() {
        return ApoliDataTypes.ENTITY_ACTION;
    }

    @Override
    public SerializableDataType<?> getItemActionDataType() {
        return ApoliDataTypes.ITEM_ACTION;
    }
}
