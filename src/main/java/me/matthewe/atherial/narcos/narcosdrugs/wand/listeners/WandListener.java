package me.matthewe.atherial.narcos.narcosdrugs.wand.listeners;

import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.DrugPackHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player.PlayerDrugPack;
import net.atherial.api.event.AtherialEventListener;
import net.atherial.api.plugin.item.AtherialItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 6/21/2019 at 3:51 PM for the project NarcosDrugs
 */
public class WandListener implements net.atherial.api.event.AtherialListener {
    @Override
    public void setup(AtherialEventListener eventListener) {
        eventListener.listen((PlayerInteractEvent event) -> {
            if (event.hasItem() && event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().isSneaking() && event.getItem().hasItemMeta() && AtherialItem.of(event.getItem()).hasData("narcos_wand", boolean.class) && AtherialItem.of(event.getItem()).getData("narcos_wand", boolean.class)) {
                Block clickedBlock = event.getClickedBlock();
                Inventory inventory = null;
                Map<Integer, ItemStack> newInventoryMap = new ConcurrentHashMap<>();
                if (clickedBlock.getState() instanceof DoubleChest) {
                    DoubleChest doubleChest = (DoubleChest) clickedBlock.getState();

                    inventory = doubleChest.getInventory();


                } else if (clickedBlock.getState() instanceof Chest) {
                    Chest chest = (Chest) clickedBlock.getState();
                    inventory = chest.getInventory();
                } else {
                    return;
                }
                List<ItemStack> itemsToAddToDrugPacks = new ArrayList<>();
                if (inventory != null) {
                    eventListener.cancelInteractEvent(event);
                    List<PlayerDrugPack> drugPacks = getDrugPacks(event.getPlayer());
                    for (int i = 0; i < inventory.getContents().length; i++) {
                        ItemStack content = inventory.getContents()[i];
                        if (content != null && content.getType() != Material.AIR && isDrug(content)) {
                            for (PlayerDrugPack drugPack : drugPacks) {
                                if (drugPack.hasSpace(content)) {
                                    drugPack.addItem(content);
                                    newInventoryMap.put(i, new ItemStack(Material.AIR));
                                    break;
                                }
                            }
                        } else {
                            newInventoryMap.put(i, content == null ? new ItemStack(Material.AIR) : content);

                        }

                    }
                    if (clickedBlock.getState() instanceof DoubleChest) {
                        DoubleChest doubleChest = (DoubleChest) clickedBlock.getState();

                        newInventoryMap.forEach((integer, itemStack) -> {
                            doubleChest.getInventory().setItem(integer, itemStack);
                        });

                    } else if (clickedBlock.getState() instanceof Chest) {
                        Chest chest = (Chest) clickedBlock.getState();
                        newInventoryMap.forEach((integer, itemStack) -> {
                            chest.getInventory().setItem(integer, itemStack);
                        });
                    }


                }
            }
        });
    }

    private List<PlayerDrugPack> getDrugPacks(Player player) {
        List<PlayerDrugPack> playerDrugPacks = new ArrayList<>();
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack content = player.getInventory().getContents()[i];
            if (content != null && content.getType() != Material.AIR && DrugPackHandler.get().isDrugPack(content)) {
                PlayerDrugPack drugPack = DrugPackHandler.get().getDrugPack(content);
                if (drugPack != null) {
                    playerDrugPacks.add(drugPack);
                }
            }
        }
        return playerDrugPacks;
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

}
