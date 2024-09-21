package com.github.theredbrain.rpgcrafting.mixin.entity.attribute;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    static {
        RPGCrafting.HAND_CRAFTING_LEVEL = Registry.registerReference(Registries.ATTRIBUTE, RPGCrafting.identifier("generic.hand_crafting_level"), new ClampedEntityAttribute("attribute.name.generic.hand_crafting_level", 0.0, -64.0, 64.0).setTracked(true));
    }
}
