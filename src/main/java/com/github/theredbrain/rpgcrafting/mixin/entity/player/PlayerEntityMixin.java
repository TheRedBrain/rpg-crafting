package com.github.theredbrain.rpgcrafting.mixin.entity.player;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.github.theredbrain.rpgcrafting.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgcrafting.inventory.StashInventory;
import com.github.theredbrain.rpgcrafting.registry.GameRulesRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DuckPlayerEntityMixin {

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Unique
    private static final TrackedData<Boolean> USE_STASH_FOR_CRAFTING = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    protected StashInventory stashInventory = new StashInventory(34, ((PlayerEntity) (Object) this));

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("RETURN"))
    protected void rpgcrafting$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(USE_STASH_FOR_CRAFTING, true);

    }

    @Inject(method = "dropInventory", at = @At("TAIL"))
    protected void rpgcrafting$dropInventory(CallbackInfo ci) {
        if (!this.getWorld().getGameRules().getBoolean(GameRulesRegistry.KEEP_STASH_INVENTORY)) {
            if (this.getWorld().getGameRules().getBoolean(GameRulesRegistry.CLEAR_STASH_INVENTORY_ON_DEATH)) {
                this.stashInventory.clear();
            } else {
                this.stashInventory.dropAll();
            }
        }

    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void rpgcrafting$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

        if (nbt.contains("stash_items", NbtElement.LIST_TYPE)) {
            this.stashInventory.readNbtList(nbt.getList("stash_items", NbtElement.COMPOUND_TYPE), this.getRegistryManager());
        }

        if (nbt.contains("use_stash_for_crafting", NbtElement.BYTE_TYPE)) {
            this.rpgcrafting$setUseStashForCrafting(nbt.getBoolean("use_stash_for_crafting"));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void rpgcrafting$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {

        nbt.put("stash_items", this.stashInventory.toNbtList(this.getRegistryManager()));

        nbt.putBoolean("use_stash_for_crafting", this.rpgcrafting$useStashForCrafting());
    }

    @Override
    public boolean rpgcrafting$useStashForCrafting() {
        return this.dataTracker.get(USE_STASH_FOR_CRAFTING);
    }

    @Override
    public void rpgcrafting$setUseStashForCrafting(boolean useStashForCrafting) {
        this.dataTracker.set(USE_STASH_FOR_CRAFTING, useStashForCrafting);
    }

    @Override
    public StashInventory rpgcrafting$getStashInventory() {
        return this.stashInventory;
    }

    @Override
    public void rpgcrafting$setStashInventory(StashInventory stashInventory) {
        this.stashInventory = stashInventory;
    }

    @Override
    public int rpgcrafting$getActiveHandCraftingLevel() {
        return Math.max(0, Math.max(0, RPGCrafting.serverConfig.default_hand_crafting_level) + this.rpgcrafting$getHandCraftingLevel());
    }

    @Override
    public int rpgcrafting$getHandCraftingLevel() {
        return (int) this.getAttributeValue(RPGCrafting.HAND_CRAFTING_LEVEL);
    }

}
