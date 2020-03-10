package me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player;

import me.matthewe.atherial.narcos.narcosdrugs.config.MenuConfig;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.DrugPack;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.DrugPackHandler;
import me.matthewe.atherial.narcos.narcosdrugs.player.DrugPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;

/**
 * Created by Matthew E on 6/10/2019 at 7:25 PM for the project NarcosDrugs
 */
public class PlayerDrugPack {
    private final String id;
    private Map<Integer, ItemStack> items;
    private int tier;

    public PlayerDrugPack(String id, Map<Integer, ItemStack> items, int tier) {
        this.id = id;
        this.items = items;
        this.tier = tier;


    }

    public static PlayerDrugPack get(String id) {
        return DrugPackHandler.get().getDrugPackStorage().getDrugPack(id);
    }

    public String getId() {
        return id;
    }

    public void open(Player player) {
        DrugPlayer drugPlayer = DrugPlayer.get(player);
        if (drugPlayer == null) {
            return;
        }
        DrugPack drugPack = DrugPack.get(tier);
        if (drugPack == null) {
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, drugPack.getRows() * 9, colorize(MenuConfig.get().drugPackMenu.title).replaceAll("%tier%", String.valueOf(tier)));
        items.forEach(inventory::setItem);
        drugPlayer.setViewedDrugPack(id);
        player.openInventory(inventory);
    }

    public void update(Inventory inventory) {
        IntStream.range(0, inventory.getSize()).forEach(i -> items.put(i, inventory.getItem(i) == null ? new ItemStack(Material.AIR) : inventory.getItem(i)));

    }

    public Map<Integer, ItemStack> getItems() {
        return items;
    }

    public int getTier() {
        return tier;
    }

    public void setItem(Integer integer, ItemStack itemStack) {
        if (items.containsKey(integer)) {
            items.remove(integer);
        }
        items.put(integer, itemStack);
    }

    public boolean hasSpace(ItemStack content) {
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            ItemStack itemStack = entry.getValue();
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                return true;
            }
        }
        return false;

    }

    public void addItem(ItemStack content) {
        Map<Integer, ItemStack> itemStackMap = new ConcurrentHashMap<>();


        boolean added = false;
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            ItemStack itemStack = entry.getValue();
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                if (added){
                    continue;
                }
                itemStackMap.put(entry.getKey(), content);
                added = true;
//            } else if (itemStack.isSimilar(content) && itemStack.getAmount() + content.getAmount() <= 64) {
//                if (added){
//                    continue;
//                }
//                itemStack.setAmount(itemStack.getAmount() + content.getAmount());
//                itemStackMap.put(entry.getKey(), itemStack);
//                added = true;

            } else {
                itemStackMap.put(entry.getKey(), itemStack);
            }
        }
        this.items = itemStackMap;
    }
}
