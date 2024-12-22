package com.github.theredbrain.rpgcrafting.config;

import com.github.theredbrain.rpgcrafting.RPGCrafting;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;

public class ClientConfig extends Config {

	public ClientConfig() {
		super(RPGCrafting.identifier("client"));
	}

	public ValidatedBoolean show_inactive_slots = new ValidatedBoolean(false);

}

