package me.matthewe.atherial.narcos.narcosdrugs.menus;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.config.MenuConfig;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import me.matthewe.atherial.narcos.narcosdrugs.level.LevelHandler;
import me.matthewe.atherial.narcos.narcosdrugs.normal.Normal;
import me.matthewe.atherial.narcos.narcosdrugs.normal.NormalHandler;
import me.matthewe.atherial.narcos.narcosdrugs.normal.NormalType;
import me.matthewe.atherial.narcos.narcosdrugs.player.DrugPlayer;
import me.matthewe.atherial.narcos.narcosdrugs.quality.QualityHandler;
import net.atherial.api.plugin.AtherialTasks;
import net.atherial.api.plugin.item.AtherialItem;
import net.atherial.api.vault.VaultDependency;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;
import static net.atherial.api.plugin.utilities.MessageUtils.message;

/**
 * Created by Matthew E on 6/20/2019 at 9:51 PM for the project NarcosDrugs
 */
public class SellMenu {
    private Inventory inventory;
    private Player player;
    private MenuConfig.DrugBuyerMenu drugBuyerMenu;

    public static Map<UUID, SellMenu> sellMenuMap;

    public SellMenu(Player player) {
        this.player = player;
        this.drugBuyerMenu = MenuConfig.get().drugBuyerMenu;
        if (hasSellMenu(player)) {
            removeSellMenu(player, true, true);
        }
        createInventory();
    }

    public static void update() {
        NarcosDrugs.getInstance().getPlayers(player -> sellMenuMap.containsKey(player.getUniqueId())).forEach(player -> {
            SellMenu sellMenu = sellMenuMap.get(player.getUniqueId());
            AtherialTasks.run(sellMenu::updateItems);
        });
    }

    private void createInventory() {
        this.inventory = Bukkit.createInventory(null, drugBuyerMenu.sellDrugMenu.rows * 9, colorize(drugBuyerMenu.sellDrugMenu.title));

        updateItems();
    }

    public void onClick(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            return;
        }
        if (event.getInventory().getType() != InventoryType.PLAYER && event.getInventory().getTitle().equals(inventory.getTitle()) && event.getWhoClicked().getUniqueId().equals(player.getUniqueId())) {
            if (event.getClickedInventory().getType() != InventoryType.PLAYER) {
                if (isSlotCancelled(event.getSlot())) {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    onClick(event.getSlot());
                }
            } else {

                if (!isDrug(event.getCurrentItem(), true)) {
                    boolean hasNormalType = false;
                    for (NormalType normalType : NormalType.values()) {

                        if (normalType.is(event.getCurrentItem())) {
                            hasNormalType = true;
                            break;
                        }
                    }
                    if (!hasNormalType){

                        event.setCancelled(true);
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
    }

    private void onClick(int slot) {
        if (slot == this.drugBuyerMenu.sellDrugMenu.sellItem.getSlot()) {
            sellItems();
        } else if (slot == this.drugBuyerMenu.sellDrugMenu.backItem.getSlot()) {
            removeSellMenu(player, true, true);
            DrugBuyerMenu.switchMenu(player, DrugBuyerMenuType.MAIN);
        }
        updatePriceItem();
    }

    private void updatePriceItem() {
        inventory.setItem(drugBuyerMenu.sellDrugMenu.sellItem.getSlot(), drugBuyerMenu.sellDrugMenu.sellItem.create().replace(s -> colorize(s).replaceAll("%amount%", new DecimalFormat("#,###.##").format(getSellPriceInMenu()))).getItemStack());

    }

    private void sellItems() {
        double amount = getSellPriceInMenu();
        if (amount > 0) {
            VaultDependency vaultDependency = NarcosDrugs.getInstance().getDependency(VaultDependency.class);
            if (vaultDependency != null) {
                vaultDependency.deposit(player, amount);
                message(player, Messages.replacePrefix(Messages.MESSAGES_SELL_DRUGS).replaceAll("%amount%", new DecimalFormat("#,###.##").format(amount)));
                clearItems();
            }
        }
    }

    public boolean isSlotCancelled(int slot) {
        Map<Integer, ItemStack> items = getItems();
        if (items.containsKey(slot)) {
            return true;
        }
        return false;
    }

    private void updateItems() {
        Map<Integer, ItemStack> items = getItems();
        items.forEach((integer, itemStack) -> this.inventory.setItem(integer, itemStack));
    }

    static {
        sellMenuMap = new ConcurrentHashMap<>();
    }

    public static SellMenu get(Player player) {
        return sellMenuMap.get(player.getUniqueId());

    }

    public static boolean hasSellMenu(Player player) {
        return sellMenuMap.containsKey(player.getUniqueId());
    }

    public void open() {

        player.openInventory(inventory);
        sellMenuMap.put(player.getUniqueId(), this);
    }

    private Map<Integer, ItemStack> getItems() {
        Map<Integer, ItemStack> itemMap = new ConcurrentHashMap<>();

        itemMap.put(drugBuyerMenu.sellDrugMenu.backItem.getSlot(), drugBuyerMenu.sellDrugMenu.backItem.create().getItemStack());
        itemMap.put(drugBuyerMenu.sellDrugMenu.sellItem.getSlot(), drugBuyerMenu.sellDrugMenu.sellItem.create().replace(s -> {
            return colorize(s).replaceAll("%amount%", new DecimalFormat("#,###.##").format(getSellPriceInMenu()));
        }).getItemStack());
        if (drugBuyerMenu.sellDrugMenu.sideItems) {
            ItemStack itemStack = drugBuyerMenu.sellDrugMenu.backgroundItem.create().getItemStack();
            for (int i = 0; i < 9; i++) {
                if (!itemMap.containsKey(i)) {
                    itemMap.put(i, itemStack);
                }
                if (!itemMap.containsKey((DrugBuyerMenuType.SELL_IN_SLOT.getRows() * 9) - (i + 1))) {
                    itemMap.put((DrugBuyerMenuType.SELL_IN_SLOT.getRows() * 9) - (i + 1), itemStack);
                }
            }

            int startSlot = 9;
            for (int i = 1; i <= 4; i++) {
                if (!itemMap.containsKey(startSlot)) {
                    itemMap.put(startSlot, itemStack);
                }
                startSlot += 8;
                if (!itemMap.containsKey(startSlot)) {
                    itemMap.put(startSlot, itemStack);
                }
                startSlot++;
            }
        }

        return itemMap;
    }

    public static String getDrug(ItemStack currentItem) {
        if (currentItem != null && currentItem.getType() != Material.AIR && AtherialItem.of(currentItem).hasData("narcos_drug", String.class)) {
            String drug = AtherialItem.of(currentItem).getData("narcos_drug", String.class);
//            if (AtherialItem.of(currentItem).hasData("narcos_drug_seed", boolean.class)) {
//                return null;
//            }
            if (DrugHandler.get().getDrugConfig().isDrug(drug)) {
                return drug;
            }
        }
        return null;
    }

    public static boolean isDrugSeed(ItemStack currentItem) {
        if (currentItem != null && currentItem.getType() != Material.AIR && AtherialItem.of(currentItem).hasData("narcos_drug_seed", boolean.class)) {
            if (AtherialItem.of(currentItem).hasData("narcos_drug_seed", boolean.class)) {
                return AtherialItem.of(currentItem).getData("narcos_drug_seed", boolean.class);
            }
        }
        return false;
    }


    public static boolean isDrug(ItemStack currentItem, boolean seed) {
        if (currentItem != null && currentItem.getType() != Material.AIR && AtherialItem.of(currentItem).hasData("narcos_drug", String.class)) {
            String drug = AtherialItem.of(currentItem).getData("narcos_drug", String.class);
            if (AtherialItem.of(currentItem).hasData("narcos_drug_seed", boolean.class) && seed) {
                return false;
            }
            if (DrugHandler.get().getDrugConfig().isDrug(drug)) {
                return true;
            }
        }
        return false;
    }

    private double getSellPriceInMenu() {
        int level = DrugPlayer.get(player).getLevel();
        double amount = 0;
        for (Integer slot : drugBuyerMenu.sellDrugMenu.sellSlots) {
            ItemStack item = inventory.getItem(slot);
            if ((item != null) && (item.getType() != Material.AIR) && isDrug(item, true)) {
                Drug drug = DrugHandler.get().getDrugConfig().getDrug(AtherialItem.of(item).getData("narcos_drug", String.class));
                double price = 0;
                if (level == 0) {
                    price = LevelHandler.get().getLevel(1).getPrices().getOrDefault(drug.getName(), 0.0D);
                } else {
                    price = LevelHandler.get().getLevel(level).getPrices().getOrDefault(drug.getName(), 0.0D);
                }
                if (price > 0) {
                    price = price * item.getAmount();
                }
                AtherialItem atherialItem = AtherialItem.of(item);
                if (atherialItem.hasData("narcos_drug_quality", String.class)) {
                    String quality = atherialItem.getData("narcos_drug_quality", String.class);
                    if (QualityHandler.get().getQualityMap().containsKey(quality)) {
                        double sellMultiplier = QualityHandler.get().getQualityMap().get(quality).getSellMultiplier();
                        price = price * sellMultiplier;
                    }
                }
                amount += price;
            }
            if ((item != null) && (item.getType() != Material.AIR) && ((!isDrug(item, true) || !isDrug(item, false)))) {
                for (NormalType normalType : NormalType.values()) {

                    if (normalType.is(item)) {
                        Normal normal = NormalHandler.get().getNormalConfig().getNormal(normalType);
                        if (normal != null) {
                            double sellPrice = normal.getSellPrice(level,item);
                            if (sellPrice > 0) {
                                sellPrice = sellPrice * item.getAmount();
                            }
                            amount += sellPrice;
                        }
                    }
                }

            }
        }
        return amount;
    }


    public static void removeSellMenu(Player player, boolean returnItems, boolean closeInventory) {
        if (hasSellMenu(player)) {
            SellMenu sellMenu = sellMenuMap.get(player.getUniqueId());
            sellMenu.onClose(returnItems);
            sellMenuMap.remove(player.getUniqueId());
            if (closeInventory) {
                player.closeInventory();
            }
        }
    }

    private void clearItems() {
        for (Integer sellSlot : drugBuyerMenu.sellDrugMenu.sellSlots) {
            inventory.setItem(sellSlot, new ItemStack(Material.AIR));
        }
    }

    private void returnItems() {
        List<ItemStack> toReturnList = new ArrayList<>();
        for (Integer sellSlot : drugBuyerMenu.sellDrugMenu.sellSlots) {
            ItemStack item = inventory.getItem(sellSlot);
            if ((item != null) && (item.getType() != Material.AIR) && isDrug(item, true)) {
                toReturnList.add(item);
            }
        }
        for (ItemStack itemStack : toReturnList) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItemNaturally(player.getEyeLocation(), itemStack);
            } else {
                player.getInventory().addItem(itemStack);
            }
        }
    }

    private void onClose(boolean returnItems) {
        if (returnItems) {
            returnItems();
        }
    }
}
