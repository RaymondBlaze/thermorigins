package dev.limonblaze.thermorigins.data;

import dev.limonblaze.thermorigins.platform.Services;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Locale;

public enum FurnaceFactory {
    FURNACE(RecipeType.SMELTING) {
        @Override
        public AbstractFurnaceMenu createMenu(int syncId, Inventory inventory, Container container, ContainerData containerData) {
            return new FurnaceMenu(syncId, inventory, container, containerData);
        }
    },
    BLAST_FURNACE(RecipeType.BLASTING) {
        @Override
        public AbstractFurnaceMenu createMenu(int syncId, Inventory inventory, Container container, ContainerData containerData) {
            return new BlastFurnaceMenu(syncId, inventory, container, containerData);
        }
    },
    SMOKER(RecipeType.SMOKING) {
        @Override
        public AbstractFurnaceMenu createMenu(int syncId, Inventory inventory, Container container, ContainerData containerData) {
            return new SmokerMenu(syncId, inventory, container, containerData);
        }
    };
    
    public final String defaultName;
    public final RecipeType<? extends AbstractCookingRecipe> recipeType;
    
    FurnaceFactory(RecipeType<? extends AbstractCookingRecipe> recipeType) {
        this.defaultName = "container." + name().toLowerCase(Locale.ROOT);
        this.recipeType = recipeType;
    }
    
    public Instance createInstance(@Nullable String name) {
        return new Instance(name);
    }
    
    public abstract AbstractFurnaceMenu createMenu(int syncId, Inventory inventory, Container container, ContainerData containerData);
    
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    public class Instance implements Container, MenuProvider {
        private final TranslatableComponent name;
        private final NonNullList<ItemStack> items;
        private final ContainerData data;
        private LivingEntity entity;
        private Level level;
        private float xp;
        private int litTime;
        private int litDuration;
        private int cookingProgress;
        private int cookingTotalTime;
        private boolean changed;
        
        public Instance(@Nullable String name) {
            this.items = NonNullList.withSize(3, ItemStack.EMPTY);
            this.name = new TranslatableComponent(name == null ? defaultName : name);
            this.data = new ContainerData() {
                public int get(int index) {
                    return switch(index) {
                        case 0 -> litTime;
                        case 1 -> litDuration;
                        case 2 -> cookingProgress;
                        case 3 -> cookingTotalTime;
                        default -> 0;
                    };
                }
                
                public void set(int index, int value) {
                    switch(index) {
                        case 0 -> litTime = value;
                        case 1 -> litDuration = value;
                        case 2 -> cookingProgress = value;
                        case 3 -> cookingTotalTime = value;
                    }
            
                }
                
                public int getCount() {
                    return 4;
                }
            };
        }
        
        @Override
        public Component getDisplayName() {
            return name;
        }
        
        @Override
        public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
            return FurnaceFactory.this.createMenu(syncId, inventory, this, data);
        }
    
        public void fromTag(CompoundTag tag) {
            ContainerHelper.loadAllItems(tag, items);
            litTime = tag.getShort("BurnTime");
            cookingProgress = tag.getShort("CookTime");
            cookingTotalTime = tag.getShort("CookTimeTotal");
            litDuration = getScaledBurnTime(items.get(1));
            xp = tag.getFloat("Experience");
        }
        
        public void toTag(CompoundTag tag) {
            ContainerHelper.saveAllItems(tag, items);
            tag.putShort("BurnTime", (short)litTime);
            tag.putShort("CookTime", (short)cookingProgress);
            tag.putShort("CookTimeTotal", (short)cookingTotalTime);
            tag.putFloat("Experience", xp);
        }
        
        public boolean tick(LivingEntity entity) {
            this.entity = entity;
            this.level = entity.level;
            if(isLit()) {
                --litTime;
                changed = true;
            }
            ItemStack fuel = items.get(1);
            if(!isLit() && (fuel.isEmpty() || items.get(0).isEmpty())) {
                if(cookingProgress > 0) {
                    cookingProgress = Mth.clamp(cookingProgress - 2, 0, cookingTotalTime);
                    changed = true;
                }
            } else {
                changed = true;
                AbstractCookingRecipe recipe = level.getRecipeManager().getRecipeFor(recipeType, this, level).orElse(null);
                if(canBurn(recipe)) {
                    assert recipe != null;
                    if(!isLit()) {
                        litTime = getScaledBurnTime(fuel);
                        litDuration = litTime;
                        if(isLit()) {
                            if(!fuel.isEmpty()) {
                                Item item = fuel.getItem();
                                fuel.shrink(1);
                                if(fuel.isEmpty()) {
                                    Item remainder = item.getCraftingRemainingItem();
                                    items.set(1, remainder == null ? ItemStack.EMPTY : new ItemStack(remainder));
                                }
                            }
                        }
                        cookingProgress = 0;
                    } else {
                        ++cookingProgress;
                        if(cookingProgress == cookingTotalTime) {
                            cookingProgress = 0;
                            cookingTotalTime = recipe.getCookingTime();
                            if(burn(recipe) && entity instanceof ServerPlayer player) {
                                player.awardRecipes(Collections.singleton(recipe));
                                xp += recipe.getExperience();
                                if(xp > 1) {
                                    xp -= 1;
                                    player.giveExperiencePoints(1);
                                }
                            }
                        }
                        return true;
                    }
                } else {
                    cookingProgress = 0;
                }
            }
            return false;
        }
        
        private int getBurnTime(ItemStack stack) {
            return Services.PLATFORM.getBurnTimeForFuel(stack, recipeType);
        }
        
        private int getScaledBurnTime(ItemStack stack) {
            return switch(FurnaceFactory.this) {
                case BLAST_FURNACE, SMOKER -> getBurnTime(stack) / 2;
                default -> getBurnTime(stack);
            };
        }
        
        public boolean isLit() {
            return litTime > 0;
        }
        
        private boolean canBurn(@Nullable Recipe<?> recipe) {
            if(!items.get(0).isEmpty() && recipe != null) {
                ItemStack newResult = recipe.getResultItem();
                if(newResult.isEmpty()) {
                    return false;
                } else {
                    ItemStack existingResult = items.get(2);
                    if(existingResult.isEmpty()) {
                        return true;
                    } else if(!existingResult.sameItem(newResult)) {
                        return false;
                    } else if(existingResult.getCount() < 64 && existingResult.getCount() < existingResult.getMaxStackSize()) {
                        return true;
                    } else {
                        return existingResult.getCount() < newResult.getMaxStackSize();
                    }
                }
            } else {
                return false;
            }
        }
    
        private boolean burn(@Nullable Recipe<?> recipe) {
            if(recipe != null && canBurn(recipe)) {
                ItemStack input = items.get(0);
                ItemStack newResult = recipe.getResultItem();
                ItemStack existingResult = items.get(2);
                if(existingResult.isEmpty()) {
                    items.set(2, newResult.copy());
                } else if(existingResult.is(newResult.getItem())) {
                    existingResult.grow(newResult.getCount());
                }
                if(input.is(Blocks.WET_SPONGE.asItem()) &&
                   !items.get(1).isEmpty() &&
                   items.get(1).is(Items.BUCKET)
                ) {
                    items.set(1, new ItemStack(Items.WATER_BUCKET));
                }
                input.shrink(1);
                return true;
            }
            return false;
        }
        
        private int getTotalCookTime() {
            return level == null ? 200 : level.getRecipeManager()
                .getRecipeFor(recipeType, this, level)
                .map(AbstractCookingRecipe::getCookingTime)
                .orElse(200);
        }
        
        public boolean getChanged() {
            boolean changed = this.changed;
            this.changed = false;
            return changed;
        }
    
        @Override
        public void setChanged() {
            this.changed = true;
        }
    
        public int getContainerSize() {
            return this.items.size();
        }
    
        public boolean isEmpty() {
            for(ItemStack itemstack : this.items) {
                if(!itemstack.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    
        public ItemStack getItem(int index) {
            return this.items.get(index);
        }
    
        public ItemStack removeItem(int index, int amount) {
            return ContainerHelper.removeItem(this.items, index, amount);
        }
    
        public ItemStack removeItemNoUpdate(int index) {
            return ContainerHelper.takeItem(this.items, index);
        }
    
        public void setItem(int index, ItemStack stack) {
            ItemStack original = this.items.get(index);
            boolean flag = !stack.isEmpty() && stack.sameItem(original) && ItemStack.tagMatches(stack, original);
            this.items.set(index, stack);
            if(stack.getCount() > this.getMaxStackSize()) {
                stack.setCount(this.getMaxStackSize());
            }
            if(index == 0 && !flag) {
                this.cookingTotalTime = getTotalCookTime();
                this.cookingProgress = 0;
                this.setChanged();
            }
        }
        
        public boolean stillValid(Player player) {
            return player.level.isClientSide || player == entity;
        }
    
        public boolean canPlaceItem(int index, ItemStack stack) {
            if(index == 2) {
                return false;
            } else if(index != 1) {
                return true;
            } else {
                ItemStack itemstack = this.items.get(1);
                return getBurnTime(stack) > 0 || stack.is(Items.BUCKET) && !itemstack.is(Items.BUCKET);
            }
        }
        
        public void clearContent() {
            this.items.clear();
        }
        
    }
    
}