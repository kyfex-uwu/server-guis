package com.kyfexuwu.server_guis;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerGUIs implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Server GUIs");

	@Override
	public void onInitialize() {
		LOGGER.info("Server GUIs loaded!");
	}
}
