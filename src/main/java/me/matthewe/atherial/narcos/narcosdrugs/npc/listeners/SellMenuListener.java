package me.matthewe.atherial.narcos.narcosdrugs.npc.listeners;

import me.matthewe.atherial.narcos.narcosdrugs.menus.SellMenu;
import net.atherial.api.event.AtherialEventListener;
import net.atherial.api.event.AtherialListener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Created by Matthew E on 6/20/2019 at 10:06 PM for the project NarcosDrugs
 */
public class SellMenuListener implements AtherialListener {
    @Override
    public void setup(AtherialEventListener eventListener) {
        eventListener.listen((InventoryClickEvent event) -> {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (SellMenu.hasSellMenu(player)) {
                    SellMenu.get(player).onClick(event);
                }
            }
        });
        eventListener.listen((InventoryCloseEvent event) -> {
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();
                if (SellMenu.hasSellMenu(player)) {
                    SellMenu.removeSellMenu(player,true,false);
                }
            }
        });
    }
}
