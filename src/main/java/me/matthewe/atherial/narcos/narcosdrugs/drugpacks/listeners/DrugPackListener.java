package me.matthewe.atherial.narcos.narcosdrugs.drugpacks.listeners;

import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.DrugPackHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.player.PlayerDrugPack;
import me.matthewe.atherial.narcos.narcosdrugs.player.DrugPlayer;
import net.atherial.api.event.AtherialEventListener;
import net.atherial.api.event.AtherialListener;
import net.atherial.api.plugin.item.AtherialItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

import static net.atherial.api.item.utilities.InteractUtils.isRightClick;

/**
 * Created by Matthew E on 6/10/2019 at 8:04 PM for the project NarcosDrugs
 */
public class DrugPackListener implements AtherialListener {
    private DrugPackHandler drugPackHandler;

    public DrugPackListener() {
        this.drugPackHandler = DrugPackHandler.get();
    }


    @Override
    public void setup(AtherialEventListener eventListener) {
        eventListener.onJoin(event -> {
            if (Messages.SETTINGS_DEVELOPMENT_MODE) {
                if (Arrays.stream(event.getPlayer().getInventory().getContents()).filter(Objects::nonNull).noneMatch(itemStack -> itemStack.getType() != Material.AIR)) {
                    drugPackHandler.getDrugPackConfig().drugPacks.keySet().forEach(integer -> drugPackHandler.giveDrugPack(integer, event.getPlayer()));
                }
            }
        });

        eventListener.listen((InventoryCloseEvent event) -> {
            if (event.getInventory().getType() == InventoryType.PLAYER) {
                return;
            }
            if ((event.getPlayer() instanceof Player) && (DrugPlayer.get((Player) event.getPlayer()) != null) && (DrugPlayer.get((Player) event.getPlayer()).isViewingDrugPack())) {
                Player player = (Player) event.getPlayer();
                DrugPlayer drugPlayer = DrugPlayer.get(player);
                PlayerDrugPack playerDrugPack = PlayerDrugPack.get(drugPlayer.getViewedDrugPack());
                if (playerDrugPack == null) {
                    return;
                }
                playerDrugPack.update(event.getInventory());
                drugPackHandler.logger.debug("Updated drug pack " + playerDrugPack.getId() + ".");
                drugPlayer.setViewedDrugPack(null);
            }
        });

        eventListener.listen((InventoryClickEvent event) -> {
            if (!(event.getWhoClicked() instanceof Player)) {
                return;
            }
            Player player = (Player) event.getWhoClicked();
            if ((DrugPlayer.get(player) != null) && (DrugPlayer.get(player).isViewingDrugPack())) {
                DrugPlayer drugPlayer = DrugPlayer.get(player);
                PlayerDrugPack playerDrugPack = PlayerDrugPack.get(drugPlayer.getViewedDrugPack());
                if (playerDrugPack == null) {
                    return;
                }
                ItemStack itemStack = event.getCurrentItem();
                if ((itemStack != null) && (itemStack.getType() != Material.AIR)) {
                    AtherialItem atherialItem = AtherialItem.of(itemStack);
                    if (!atherialItem.hasData("narcosdrugs_drug",Boolean.class)||(((atherialItem.hasData("narcos_drug_seed",boolean.class)&&atherialItem.getData("narcos_drug_seed",boolean.class))))) {
                        event.setCancelled(true);
                    }
                }
            }
        });
        eventListener.listen( EventPriority.LOWEST, false,(PlayerInteractEvent event) -> {
            if (isRightClick(event.getAction()) && event.hasItem()) {
                Player player = event.getPlayer();
                ItemStack itemStack = event.getItem();

                if (this.drugPackHandler.isDrugPack(itemStack)) {
                    eventListener.cancelInteractEvent(event);
                    DrugPlayer drugPlayer = DrugPlayer.get(player);
                    if (drugPlayer == null) {
                        return;
                    }

                    PlayerDrugPack drugPack = this.drugPackHandler.getDrugPack(itemStack);
                    if (drugPack==null){
                        return;
                    }
                    if (drugPlayer.isViewingDrugPack()) {
                        return;
                    }
                    drugPack.open(player);
                }
            }
        });

    }
}
