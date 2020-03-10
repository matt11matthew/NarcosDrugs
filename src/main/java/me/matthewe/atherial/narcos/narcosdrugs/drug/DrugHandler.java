package me.matthewe.atherial.narcos.narcosdrugs.drug;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosHandler;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.drug.listeners.PlantListener;
import me.matthewe.atherial.narcos.narcosdrugs.drug.listeners.TestListener;
import me.matthewe.atherial.narcos.narcosdrugs.drug.world.WorldDrug;
import me.matthewe.atherial.narcos.narcosdrugs.drug.world.WorldDrugConfig;
import me.matthewe.atherial.narcos.narcosdrugs.menus.SellMenu;
import net.atherial.api.plugin.AtherialTasks;
import net.atherial.api.plugin.item.AtherialItem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Map;

/**
 * Created by Matthew E on 6/9/2019 at 2:06 PM for the project NarcosDrugs
 */
public class DrugHandler extends NarcosHandler {
    private DrugConfig drugConfig;
    private WorldDrugConfig worldDrugConfig;

    public DrugHandler(NarcosDrugs narcosDrugs) {
        super(narcosDrugs);
    }

    public static DrugHandler get() {
        return NarcosDrugs.getInstance().getHandler(DrugHandler.class);
    }

    @Override
    public void onEnable() {
        this.loadDrugs(true);
        this.loadWorldDrugs(true);

        if (Messages.SETTINGS_DEVELOPMENT_MODE) {
            registerListener(new TestListener());
            if (!drugConfig.isDrug("test")) {
                drugConfig.createDrug("test");
            }
            if (!drugConfig.isDrug("test1")) {
                drugConfig.createDrug("test1");
            }
        }
        convertJsonToYml();
        AtherialTasks.repeatAsync(() -> {
            SellMenu.update();
        }, 5L);
        registerListener(new PlantListener());

        registerCommand(new NarcosDrugsCommand(this));

        AtherialTasks.repeatAsync(() -> worldDrugConfig.drugs.values().forEach(worldDrug -> AtherialTasks.run(worldDrug::tick)), 20L);
    }

    private void convertJsonToYml() {

        File file = new File(narcosDrugs.getDataFolder(), "drugs.json");
        if (file.exists()) {
            boolean converted = false;
            DrugConfigOld drugConfigOld = new DrugConfigOld(narcosDrugs).load();
            for (Map.Entry<String, Drug> entry : drugConfigOld.drugs.entrySet()) {
                String s = entry.getKey();
                Drug drug = entry.getValue();
                this.drugConfig.addDrug(drug);
                converted = true;
            }
            if (converted){
                saveDrugs(false);
                loadDrugs(false);
            }
        }
    }

    public void giveDrugSeed(Player player, Drug drug, int amount) {
        ItemStack itemStack = drug.getSeed().getItemStack(drug, null);
        itemStack.setAmount(amount);
        AtherialItem atherialItem = AtherialItem.of(itemStack);
        atherialItem.setData("narcos_drug", drug.getName());
        atherialItem.setData("narcos_drug_seed", true);
        atherialItem.setData("narcosdrugs_drug", true);
        player.getInventory().addItem(atherialItem.getItemStack());

    }

    public Drug getDrugBySeed(ItemStack itemStack) {
//        for (Drug drug : drugConfig.drugs.values()) {
//            ItemStack itemStack1 = drug.getSeed().getItemStack(drug);
//            if (itemStack1.equals(itemStack)) {
//                return drug;
//            }
//        }
        AtherialItem atherialItem = AtherialItem.of(itemStack);
        if (atherialItem.hasData("narcos_drug_seed", boolean.class) && atherialItem.getData("narcos_drug_seed", boolean.class) && atherialItem.hasData("narcos_drug", String.class)) {
            String drug = atherialItem.getData("narcos_drug", String.class);
            return drugConfig.getDrug(drug.toLowerCase());
        }
        return null;
    }

    @Override
    public void onDisable() {
        this.saveWorldDrugs(true);
        this.saveDrugs(true);
    }

    public WorldDrugConfig getWorldDrugConfig() {
        return worldDrugConfig;
    }

    @Override
    public void reloadHandler() {

        this.loadDrugs(true);
        this.saveWorldDrugs(true);

    }

    public DrugConfig getDrugConfig() {
        return drugConfig;
    }


    private void loadWorldDrugs(boolean debug) {
        if (debug) {
            this.logger.info("Loading world drugs...");
        }
        this.worldDrugConfig = new WorldDrugConfig(this.narcosDrugs).load();
        if (!this.worldDrugConfig.drugs.isEmpty()) {
            this.worldDrugConfig.drugs.forEach((uuid, drug) -> {
                if (debug) {
                    logger.info("Loaded world drug &7" + drug.getLocation().toString() + " " + drug.getDrug() + ".");
                }
            });
        }
        if (debug) {
            logger.info("Loaded &7" + this.worldDrugConfig.drugs.keySet().size() + " world drugs.");
        }
    }

    private void loadDrugs(boolean debug) {
        if (debug) {
            this.logger.info("&bLoading drugs...");
        }
        this.drugConfig = new DrugConfig(this.narcosDrugs);
        drugConfig.loadDrugs();
        if (!this.drugConfig.getDrugs().isEmpty()) {
            this.drugConfig.getDrugs().forEach((uuid, drug) -> {
                if (debug) {
                    logger.info("&bLoaded drug &7" + drug.getName() + "&b.");
                }
            });
        }
        if (debug) {
            logger.info("&bLoaded &7" + this.drugConfig.getDrugs().keySet().size() + " &bdrugs.");
        }
    }

    private void saveWorldDrugs(boolean debug) {
        if (debug) {
            this.logger.info("&bSaving world drugs...");
        }
        int saved = this.worldDrugConfig.drugs.keySet().size();
        this.worldDrugConfig.save();
        if (debug) {
            this.logger.info("&bSaved &7" + saved + " &bworld drugs.");
        }
        this.loadWorldDrugs(debug);
    }

    private void saveDrugs(boolean debug) {
        if (debug) {
            this.logger.info("&bSaving drugs...");
        }
        int saved = this.drugConfig.getDrugs().keySet().size();
        this.drugConfig.saveDrugs();
        if (debug) {
            this.logger.info("&bSaved &7" + saved + " &bdrugs.");
        }
        this.loadDrugs(debug);
    }


    public boolean isDrug(Location location) {
        return worldDrugConfig.isDrug(location);
    }

    public WorldDrug getDrug(Location location) {
        return worldDrugConfig.getDrug(location);
    }
}
