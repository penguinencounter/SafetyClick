package org.penguinencounter.safetyclick.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class SafetyclickClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("PenguinEncounter SafetyClick");
    @Override
    public void onInitializeClient() {

        LOGGER.info("SafetyClick Client initialized");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("Debug is Enabled");
        } else {
            LOGGER.warn("Debug is not Enabled!");
        }
    }
}
