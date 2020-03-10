package me.matthewe.atherial.narcos.narcosdrugs.quality;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import me.matthewe.atherial.narcos.narcosdrugs.quality.listeners.QualityDrugListener;
import me.matthewe.atherial.narcos.narcosdrugs.utilities.ConfigUtils;
import net.atherial.api.config.BukkitConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 6/20/2019 at 11:23 PM for the project NarcosDrugs
 */
public class QualityHandler extends NarcosHandler {
    private Map<String, Quality> qualityMap;

    public QualityHandler(NarcosDrugs narcosDrugs) {
        super(narcosDrugs);
    }

    public Map<String, Quality> getQualityMap() {
        return qualityMap;
    }

    @Override
    public void onEnable() {
        this.loadDrugQualities();
        registerListener(new QualityDrugListener());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void reloadHandler() {
        loadDrugQualities();
    }

    public static QualityHandler get() {
        return NarcosDrugs.getInstance().getHandler(QualityHandler.class);
    }




    private void loadDrugQualities() {
        this.qualityMap = new ConcurrentHashMap<>();
        BukkitConfig bukkitConfig = new BukkitConfig("quality.yml", this.plugin);

        FileConfiguration configuration = bukkitConfig.getConfiguration();
        for (String key : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(key);
            String lore = section.getString("lore");
            double defaultChance = ConfigUtils.getChance("defaultChance",section);
            double sellMultiplier = section.getDouble("sellMultiplier",1.0D);

            qualityMap.put(key, new Quality(key, lore, defaultChance, sellMultiplier));
            logger.debug("Loaded drug quality " + key + ".");
        }
    }

    public void onDrugCreate(Drug drug) {
        logger.debug("&eDrug created " + drug.getName());
    }

    public Quality getRandom() {

        List<String> foundRewards = new ArrayList<>();
        for (String reward : qualityMap.keySet()) {
            if (Math.random() * 100.0D <= qualityMap.get(reward).getDefaultChance()) {
                foundRewards.add(reward);
            }
        }
        if (foundRewards.isEmpty()) {
            return getRandom();
        }
        return qualityMap.get(foundRewards.get(0));

    }
}
