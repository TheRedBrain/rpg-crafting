package com.github.theredbrain.rpgcrafting.advancement.criterion;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class InteractWithRPGCraftingStationCriterion extends AbstractCriterion<InteractWithRPGCraftingStationCriterion.Conditions> {

	@Override
	public Codec<Conditions> getConditionsCodec() {
		return Conditions.CODEC;
	}

	public void trigger(ServerPlayerEntity player, int tab, int level) {
		this.trigger(player, conditions -> conditions.matches(tab, level));
	}

	// needed for data gen
	public static AdvancementCriterion<InteractWithRPGCraftingStationCriterion.Conditions> create(int tab, int level) {
		return RPGCrafting.INTERACTED_WITH_RPG_CRAFTING_STATION.create(new InteractWithRPGCraftingStationCriterion.Conditions(Optional.empty(), Optional.of(tab), Optional.of(level)));
	}

	public record Conditions(Optional<LootContextPredicate> player, Optional<Integer> tab,
							 Optional<Integer> level)
			implements AbstractCriterion.Conditions {
		public static final Codec<InteractWithRPGCraftingStationCriterion.Conditions> CODEC = RecordCodecBuilder.<InteractWithRPGCraftingStationCriterion.Conditions>create(
				instance -> instance.group(
								EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(InteractWithRPGCraftingStationCriterion.Conditions::player),
								Codec.INT.optionalFieldOf("tab").forGetter(InteractWithRPGCraftingStationCriterion.Conditions::tab),
								Codec.INT.optionalFieldOf("level").forGetter(InteractWithRPGCraftingStationCriterion.Conditions::level)
						)
						.apply(instance, InteractWithRPGCraftingStationCriterion.Conditions::new)
		);

		public boolean matches(int tab, int level) {
			return this.tab.isPresent() && this.level.isPresent() && this.tab.get() == tab && this.level.get() <= level;
		}
	}
}
