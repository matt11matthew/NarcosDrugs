package me.matthewe.atherial.narcos.narcosdrugs.menus;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.config.MenuConfig;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.DrugPackHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player.PlayerDrugPack;
import me.matthewe.atherial.narcos.narcosdrugs.level.LevelHandler;
import me.matthewe.atherial.narcos.narcosdrugs.normal.Normal;
import me.matthewe.atherial.narcos.narcosdrugs.normal.NormalHandler;
import me.matthewe.atherial.narcos.narcosdrugs.normal.NormalType;
import me.matthewe.atherial.narcos.narcosdrugs.player.DrugPlayer;
import me.matthewe.atherial.narcos.narcosdrugs.quality.QualityHandler;
import net.atherial.api.menu.AtherialMenu;
import net.atherial.api.menu.item.MenuItem;
import net.atherial.api.menu.item.MenuItemUpdate;
import net.atherial.api.plugin.item.AtherialItem;
import net.atherial.api.vault.VaultDependency;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;
import static net.atherial.api.plugin.utilities.MessageUtils.message;

/**
 * Created by Matthew E on 6/20/2019 at 7:29 PM for the project NarcosDrugs
 */
public class DrugBuyerMenu extends AtherialMenu {
    private MenuConfig.DrugBuyerMenu drugBuyerMenu;
    private DrugBuyerMenuType type;
    private List<Integer> airSlots = new ArrayList<>();


    public DrugBuyerMenu(Player player, DrugBuyerMenuType type) {
        super(player, colorize(type.getTitle()), type.getRows());
        this.type = type;

        this.drugBuyerMenu = MenuConfig.get().drugBuyerMenu;


        for (int i = 0; i < 9 * this.getRows(); i++) {

            final int slot = i;
            int finalI = i;
            MenuItemUpdate menuItemUpdate = new MenuItemUpdate() {
                @Override
                public ItemStack update(Player player, ItemStack itemStack) {
                    return getItemStack(finalI);
                }
            };
            MenuItem menuItem = new MenuItem(i, getItemStack(slot), menuItemUpdate, (player1, clickType) -> {
                onClick(player, slot);
            });

            addItem(fixMenuItem(menuItem));

        }
    }

    private void switchMenu(DrugBuyerMenuType drugBuyerMenuType) {

        switchMenu(player, drugBuyerMenuType);
    }

    public static void switchMenu(Player player, DrugBuyerMenuType drugBuyerMenuType) {
        new DrugBuyerMenu(player, drugBuyerMenuType).open();
    }

    private void onClick(Player player, int slot) {
        switch (type) {
            case MAIN:
                if (slot == drugBuyerMenu.mainMenu.sellAllItem.getSlot()) {
                    sellAll();
                } else if (slot == drugBuyerMenu.mainMenu.sellInInventoryItem.getSlot()) {
                    new SellMenu(player).open();
                }
                break;
            case SELLALL:
                break;
            case SELL_IN_SLOT:

                break;
        }

    }


    private Map<Integer, ItemStack> getItems() {
        Map<Integer, ItemStack> itemMap = new ConcurrentHashMap<>();
        switch (type) {
            case MAIN:
                itemMap.put(drugBuyerMenu.mainMenu.sellAllItem.getSlot(), drugBuyerMenu.mainMenu.sellAllItem.create().getItemStack());
                itemMap.put(drugBuyerMenu.mainMenu.sellInInventoryItem.getSlot(), drugBuyerMenu.mainMenu.sellInInventoryItem.create().getItemStack());
                if (drugBuyerMenu.mainMenu.fillBackground) {
                    for (int i = 0; i < 9 * this.getRows(); i++) {
                        if (!itemMap.containsKey(i) && !drugBuyerMenu.mainMenu.skipSlots.contains(i)) {
                            itemMap.put(i, drugBuyerMenu.mainMenu.backgroundItem.create().getItemStack());
                        }
                    }
                }
                break;
            case SELL_IN_SLOT:

                break;
            default:
                break;
        }
        return itemMap;
    }

    protected MenuItem fixMenuItem(MenuItem menuItem) {
        if (type == DrugBuyerMenuType.SELL_IN_SLOT && airSlots.contains(menuItem.getSlot())) {
            return menuItem;
        }
        menuItem.setMenuClickEvent((player1, inventoryClickEvent) -> {

            if (inventoryClickEvent.getClickedInventory().getType() == InventoryType.PLAYER) {
                inventoryClickEvent.setCancelled(true);
                inventoryClickEvent.setResult(Event.Result.DENY);
                return;
            }
            if (inventoryClickEvent.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                inventoryClickEvent.setCancelled(true);
                inventoryClickEvent.setResult(Event.Result.DENY);
                return;
            }
            if (inventoryClickEvent.getCursor() != null || inventoryClickEvent.getCursor().getType() != Material.AIR) {
                inventoryClickEvent.setCancelled(true);
                inventoryClickEvent.setResult(Event.Result.DENY);
                menuItem.getMenuClick().click(player, inventoryClickEvent.getClick());
                return;
            }

            if (menuItem.isClicked()) {
                inventoryClickEvent.setCancelled(true);
                inventoryClickEvent.setResult(Event.Result.DENY);
                menuItem.getMenuClick().click(player, inventoryClickEvent.getClick());
                return;
            }

            if (menuItem.isCancelled()) {
                inventoryClickEvent.setCancelled(true);
                inventoryClickEvent.setResult(Event.Result.DENY);
            }

            menuItem.setClicked(true);
            menuItem.getMenuClick().click(player, inventoryClickEvent.getClick());
            (new BukkitRunnable() {
                public void run() {
                    menuItem.setClicked(false);
                }
            }).runTaskLater(NarcosDrugs.getInstance(), 5L);
        });
        return menuItem;
    }

    private boolean isDrug(ItemStack currentItem) {
        if (currentItem != null && currentItem.getType() != Material.AIR && AtherialItem.of(currentItem).hasData("narcos_drug", String.class)) {
            String drug = AtherialItem.of(currentItem).getData("narcos_drug", String.class);
            if (AtherialItem.of(currentItem).hasData("narcos_drug_seed", boolean.class)) {
                return false;
            }
            if (DrugHandler.get().getDrugConfig().isDrug(drug)) {
                return true;
            }
        }
        return false;
    }

    private void sellAll() {
        double amount = 0;
        List<Integer> toRemoveList = new ArrayList<>();
        int level = DrugPlayer.get(player).getLevel();
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack item = player.getInventory().getContents()[i];
            if ((item != null) && (item.getType() != Material.AIR) && isDrug(item)) {
                AtherialItem atherialItem = AtherialItem.of(item);
                Drug drug = DrugHandler.get().getDrugConfig().getDrug(atherialItem.getData("narcos_drug", String.class));
                double price = 0;
                if (level == 0) {
                    price = LevelHandler.get().getLevel(1).getPrices().getOrDefault(drug.getName(), 0.0D);
                } else {
                    price = LevelHandler.get().getLevel(level).getPrices().getOrDefault(drug.getName(), 0.0D);
                }
                if (price > 0) {
                    price = price * item.getAmount();
                }
                if (atherialItem.hasData("narcos_drug_quality", String.class)) {
                    String quality = atherialItem.getData("narcos_drug_quality", String.class);
                    if (QualityHandler.get().getQualityMap().containsKey(quality)) {
                        double sellMultiplier = QualityHandler.get().getQualityMap().get(quality).getSellMultiplier();
                        price = price * sellMultiplier;
                    }
                }
                amount += price;
                toRemoveList.add(i);
            }
            if ((item != null) && (item.getType() != Material.AIR) && !isDrug(item) && !toRemoveList.contains(i)) {
                for (NormalType value : NormalType.values()) {
                    if (toRemoveList.contains(i)) {
                        continue;
                    }
                    if (value.is(item)) {
                        Normal normal = NormalHandler.get().getNormalConfig().getNormal(value);
                        if (normal != null) {
                            double sellPrice = normal.getSellPrice(level, item);
                            if (sellPrice > 0) {
                                sellPrice = sellPrice * item.getAmount();
                            }
                            amount += sellPrice;
                            toRemoveList.add(i);
                        }
                    }
                }

            }
        }
        for (Integer integer : toRemoveList) {
            player.getInventory().setItem(integer, new ItemStack(Material.AIR));
        }
        toRemoveList.clear();
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack item = player.getInventory().getContents()[i];
            if ((item != null) && (item.getType() != Material.AIR) && DrugPackHandler.get().isDrugPack(item)) {
                PlayerDrugPack drugPack = DrugPackHandler.get().getDrugPack(item);
                List<Integer> drugPackToRemoveList = new ArrayList<>();

                for (int slot : drugPack.getItems().keySet()) {
                    ItemStack value = drugPack.getItems().get(slot);
                    if ((value != null) && (value.getType() != Material.AIR) && isDrug(value)) {
                        Drug drug = DrugHandler.get().getDrugConfig().getDrug(AtherialItem.of(value).getData("narcos_drug", String.class));
                        double price = 0;
                        if (level == 0) {
                            price = LevelHandler.get().getLevel(1).getPrices().getOrDefault(drug.getName(), 0.0D);
                        } else {
                            price = LevelHandler.get().getLevel(level).getPrices().getOrDefault(drug.getName(), 0.0D);
                        }
                        if (price > 0) {
                            price = price * item.getAmount();
                        }
                        AtherialItem atherialItem = AtherialItem.of(value);
                        if (atherialItem.hasData("narcos_drug_quality", String.class)) {
                            String quality = atherialItem.getData("narcos_drug_quality", String.class);
                            if (QualityHandler.get().getQualityMap().containsKey(quality)) {
                                double sellMultiplier = QualityHandler.get().getQualityMap().get(quality).getSellMultiplier();
                                price = price * sellMultiplier;
                            }
                        }
                        amount += price;
                        drugPackToRemoveList.add(slot);
                    }
                    if ((item != null) && (item.getType() != Material.AIR) && !isDrug(item) && !drugPackToRemoveList.contains(i)) {
                        for (NormalType normalType : NormalType.values()) {
                            if (drugPackToRemoveList.contains(i)) {
                                continue;
                            }
                            if (normalType.is(item)) {
                                Normal normal = NormalHandler.get().getNormalConfig().getNormal(normalType);
                                if (normal != null) {
                                    double sellPrice = normal.getSellPrice(level, item);
                                    if (sellPrice > 0) {
                                        sellPrice = sellPrice * item.getAmount();
                                    }
                                    amount += sellPrice;
                                    drugPackToRemoveList.add(slot);
                                }
                            }
                        }

                    }
                }
                for (Integer integer : drugPackToRemoveList) {
                    drugPack.setItem(integer, new ItemStack(Material.AIR));

                }
            }
        }

        if (amount > 0) {
            VaultDependency vaultDependency = NarcosDrugs.getInstance().getDependency(VaultDependency.class);
            if (vaultDependency != null) {
                player.closeInventory();
                vaultDependency.deposit(player, amount);
                message(player, Messages.replacePrefix(Messages.MESSAGES_SELL_ALL_DRUGS).replaceAll("%amount%", new DecimalFormat("#,###.##").format(amount)));
                Sound sound = null;

                try {
                    sound = Sound.valueOf("NOTE_PLING");
                } catch (Exception e) {
                    return;
                }
                player.playSound(player.getLocation(), sound, 1, 1);
            }

        }
    }


    private ItemStack getItemStack(int slot) {
        Map<Integer, ItemStack> items = getItems();
        return items.getOrDefault(slot, new ItemStack(Material.AIR));
    }
}
