package me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.DrugPack;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.DrugPackHandler;
import net.atherial.api.config.BukkitConfig;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class DrugPackStorage {
    private NarcosDrugs narcosPlugin;
    public Map<String, PlayerDrugPack> drugPacks = new ConcurrentHashMap<>();
    private DrugPackHandler drugPackHandler;
    private BukkitConfig drugPackConfig;


    public DrugPackStorage(NarcosDrugs narcosPlugin, DrugPackHandler drugPackHandler) {
        this.narcosPlugin = narcosPlugin;
        this.drugPackHandler = drugPackHandler;
    }


    public void save() {
        File file = new File(this.narcosPlugin.getDataFolder() + "/data/", "drugpacks.yml");
        if (file.exists()) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        this.drugPackConfig = new BukkitConfig("data/drugpacks.yml", this.narcosPlugin);
        FileConfiguration configuration = this.drugPackConfig.getConfiguration();
        for (Map.Entry<String, PlayerDrugPack> entry : drugPacks.entrySet()) {
            PlayerDrugPack drugPack = entry.getValue();

            ConfigurationSection section = configuration.createSection("packs." + entry.getKey());
            section.set("tier", drugPack.getTier());

            ConfigurationSection itemsSection = section.createSection("items");
            drugPack.getItems().forEach((slot, itemStack) -> {
                itemsSection.set(String.valueOf(slot), itemStack);
            });

            section.set("items", itemsSection);

            configuration.set("packs." + entry.getKey(), section);
        }
        drugPackConfig.setConfiguration(configuration);
        drugPackConfig.saveConfiguration();
    }

    public DrugPackStorage load() {
        this.drugPackConfig = new BukkitConfig("data/drugpacks.yml", this.narcosPlugin);
        FileConfiguration configuration = this.drugPackConfig.getConfiguration();
        this.drugPacks = new ConcurrentHashMap<>();
        if (configuration.isSet("packs")) {
            for (String id : configuration.getConfigurationSection("packs").getKeys(false)) {
                ConfigurationSection packSection = configuration.getConfigurationSection("packs." + id);
                int tier = packSection.getInt("tier");

                Map<Integer, ItemStack> itemStackMap = new ConcurrentHashMap<>();

                if (packSection.isConfigurationSection("items")) {
                    for (String slotString : packSection.getConfigurationSection("items").getKeys(false)) {
                        int slot = Integer.parseInt(slotString.trim());

                        ItemStack itemStack = packSection.getItemStack("items."+slotString, new ItemStack(Material.AIR));
                        itemStackMap.put(slot, itemStack);
                    }
                }
                drugPacks.put(id, new PlayerDrugPack(id, itemStackMap, tier));
            }
        }
        this.drugPackConfig.setConfiguration(configuration);
        return this;
    }


    public boolean isDrugPack(String id) {
        return drugPacks.containsKey(id);
    }


    public PlayerDrugPack createDrugPack(int tier) {
        String id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6).trim();
        if (drugPackHandler.getDrugPackConfig().isDrugPack(tier)) {
            DrugPack drugPack = drugPackHandler.getDrugPackConfig().getDrugPack(tier);

            int slots = drugPack.getRows() * 9;

            Map<Integer, ItemStack> itemStackMap = new ConcurrentHashMap<>();
            for (int i = 0; i < slots; i++) {
                itemStackMap.put(i, new ItemStack(Material.AIR));
            }

            PlayerDrugPack playerDrugPack = new PlayerDrugPack(id, itemStackMap, tier);
            this.drugPacks.put(id, playerDrugPack);
            return drugPacks.get(id);
        }
        return null;
    }

    public PlayerDrugPack getDrugPack(String id) {
        return drugPacks.get(id);
    }
}
