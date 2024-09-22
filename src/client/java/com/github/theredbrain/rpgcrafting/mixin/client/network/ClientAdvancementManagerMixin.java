package com.github.theredbrain.rpgcrafting.mixin.client.network;
// TODO find a way to check for advancements on client side
//import com.github.theredbrain.rpgcrafting.network.DuckClientAdvancementManagerMixin;
//import net.minecraft.advancement.Advancement;
//import net.minecraft.advancement.AdvancementProgress;
//import net.minecraft.client.network.ClientAdvancementManager;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//
//import java.util.Map;
//
//@Mixin(ClientAdvancementManager.class)
//public class ClientAdvancementManagerMixin implements DuckClientAdvancementManagerMixin {
//    @Shadow
//	@Final
//	private Map<Advancement, AdvancementProgress> advancementProgresses;
//
//    @Override
//    public AdvancementProgress rpgcrafting$getAdvancementProgress(Advancement advancement) {
//        return this.advancementProgresses.get(advancement);
//    }
//}