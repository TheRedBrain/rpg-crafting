package com.github.theredbrain.rpgcrafting.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record OpensRPGCraftingScreenComponent(
		int crafting_tab
) {
	public static final Codec<OpensRPGCraftingScreenComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Codec.INT.fieldOf("crafting_tab").forGetter(OpensRPGCraftingScreenComponent::crafting_tab)
					)
					.apply(instance, OpensRPGCraftingScreenComponent::new)
	);
	public static final PacketCodec<RegistryByteBuf, OpensRPGCraftingScreenComponent> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER,
			OpensRPGCraftingScreenComponent::crafting_tab,
			OpensRPGCraftingScreenComponent::new
	);

	public OpensRPGCraftingScreenComponent(
			int crafting_tab
	) {
		this.crafting_tab = crafting_tab;
	}
}
