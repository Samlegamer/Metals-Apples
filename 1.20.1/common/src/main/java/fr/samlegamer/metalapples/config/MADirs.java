package fr.samlegamer.metalapples.config;

import fr.samlegamer.metalapples.MetalApple;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public record MADirs(Path configDir) {

    public void addDirs() {
        if (Files.exists(configDir)) {
            MetalApple.LOGGER.info("Creating config directory for Metal Apples !");
            try {
                Path ma_cfg = Paths.get(configDir.toString(), MetalApple.MODID);
                Path ma_cfg_vanilla = Paths.get(configDir.toString(), MetalApple.MODID, "vanilla");
                Path ma_cfg_custom = Paths.get(configDir.toString(), MetalApple.MODID, "custom");

                if (!Files.exists(ma_cfg)) {
                    Files.createDirectory(ma_cfg);
                }

                if (!Files.exists(ma_cfg_vanilla)) {
                    Files.createDirectory(ma_cfg_vanilla);
                }

                if (!Files.exists(ma_cfg_custom)) {
                    Files.createDirectory(ma_cfg_custom);
                }
            } catch (Exception e) {
                MetalApple.LOGGER.error(e.getMessage());
            }
        }
    }
}