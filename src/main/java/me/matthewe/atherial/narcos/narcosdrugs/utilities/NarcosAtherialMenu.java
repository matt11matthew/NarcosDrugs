package me.matthewe.atherial.narcos.narcosdrugs.utilities;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import net.atherial.api.menu.AtherialMenu;
import net.atherial.api.menu.item.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Matthew E on 6/19/2019 at 5:37 PM for the project NarcosDrugs
 */
public abstract class NarcosAtherialMenu extends AtherialMenu {

    public NarcosAtherialMenu(Player player, String title, int rows) {
        super(player, title, rows);
    }


    protected MenuItem fixMenuItem(MenuItem menuItem) {
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
}
