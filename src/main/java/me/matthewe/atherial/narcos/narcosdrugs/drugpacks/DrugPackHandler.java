package me.matthewe.atherial.narcos.narcosdrugs.drugpacks;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.listeners.DrugPackListener;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player.DrugPackStorage;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player.PlayerDrugPack;
import net.atherial.api.plugin.item.AtherialItem;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;

/**
 * Created by Matthew E on 6/10/2019 at 7:19 PM for the project NarcosDrugs
 */
public class DrugPackHandler extends NarcosHandler {
    private DrugPackStorage drugPackStorage;
    private DrugPackConfig drugPackConfig;

    public DrugPackHandler(NarcosDrugs narcosDrugs) {
        super(narcosDrugs);
    }

    public static DrugPackHandler get() {
        return NarcosDrugs.getInstance().getHandler(DrugPackHandler.class);
    }

    public DrugPackStorage getDrugPackStorage() {
        return drugPackStorage;
    }

    public DrugPackConfig getDrugPackConfig() {
        return drugPackConfig;
    }

    @Override
    public void onEnable() {
        this.loadDrugPacks();
        this.loadPlayerDrugPacks();

        registerListener(new DrugPackListener());
        this.narcosDrugs.getPlayers().forEach(HumanEntity::closeInventory);

    }

    @Override
    public void onDisable() {
        this.narcosDrugs.getPlayers().forEach(HumanEntity::closeInventory);

        saveDrugPacks();
        savePlayerDrugPacks();
    }

    public boolean giveDrugPack(int tier, Player player) {
        if (this.drugPackConfig.isDrugPack(tier)) {
            DrugPack drugPack = this.drugPackConfig.getDrugPack(tier);
            PlayerDrugPack playerDrugPack = this.drugPackStorage.createDrugPack(tier);
            ItemStack itemStack = drugPack.getDisplayItem().getItemStack(1, s -> colorize(s)
                    .replaceAll("%tier%", String.valueOf(playerDrugPack.getTier()))
                    .replaceAll("%id%", playerDrugPack.getId())
                    .replaceAll("%player%", player.getUniqueId().toString())
                    .replaceAll("%slots%", String.valueOf((drugPack.getRows() * 9))));
            AtherialItem atherialItem = AtherialItem.of(itemStack);
            atherialItem.setData("narcos_drugpack_id", playerDrugPack.getId());
            itemStack = atherialItem.getItemStack();
            player.getInventory().addItem(itemStack);
            return true;
        }
        return false;
    }

    private void loadDrugPacks() {
        this.logger.debug("Loading drug packs...");
        this.drugPackConfig = new DrugPackConfig(this.plugin).load();
        this.drugPackConfig.drugPacks.values().forEach(drugPack -> this.logger.debug("Loaded drug pack " + drugPack.getTier() + "."));
        this.logger.debug("Loaded " + this.drugPackConfig.drugPacks.keySet().size() + " drug packs.");
    }

    private void savePlayerDrugPacks() {

        this.logger.debug("Saving player drug packs...");
        this.drugPackStorage.save();
        this.drugPackStorage.drugPacks.values().forEach(drugPack -> this.logger.debug("Saved player drug pack " + drugPack.getId() + "."));
        this.logger.debug("Saved " + this.drugPackStorage.drugPacks.keySet().size() + " player drug packs.");

        if (isReloading()) {
            loadPlayerDrugPacks();
        }
    }

    private void loadPlayerDrugPacks() {
        this.logger.debug("Loading player drug packs...");
        this.drugPackStorage = new DrugPackStorage(this.plugin, this).load();
        this.drugPackStorage.drugPacks.values().forEach(drugPack -> this.logger.debug("Loaded player drug pack " + drugPack.getId() + "."));
        this.logger.debug("Loaded " + this.drugPackStorage.drugPacks.keySet().size() + " player drug packs.");
    }

    private void saveDrugPacks() {
        this.logger.debug("Saving drug packs...");
        this.drugPackConfig.save();
        this.drugPackConfig.drugPacks.values().forEach(drugPack -> this.logger.debug("Saved drug pack " + drugPack.getTier() + "."));
        this.logger.debug("Saved " + this.drugPackConfig.drugPacks.keySet().size() + " drug packs.");
        if (isReloading()) {
            loadDrugPacks();
        }
    }

    @Override
    public void reloadHandler() {
        this.narcosDrugs.getPlayers().forEach(HumanEntity::closeInventory);
        savePlayerDrugPacks();
        loadDrugPacks();
    }

    public boolean isDrugPack(ItemStack itemStack) {
        if ((itemStack == null) || (itemStack.getType() == Material.AIR)) {
            return false;
        }
        AtherialItem atherialItem = AtherialItem.of(itemStack);
        return (atherialItem.hasData("narcos_drugpack_id", String.class)) && drugPackStorage.isDrugPack(atherialItem.getData("narcos_drugpack_id", String.class));
    }

    public PlayerDrugPack getDrugPack(ItemStack itemStack) {
        if (isDrugPack(itemStack)) {
            AtherialItem atherialItem = AtherialItem.of(itemStack);
            return this.drugPackStorage.getDrugPack(atherialItem.getData("narcos_drugpack_id", String.class));
        }
        return null;
    }

    public boolean isDrugPack(int tier) {
        return drugPackConfig.isDrugPack(tier);
    }
}
