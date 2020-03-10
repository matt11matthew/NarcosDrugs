package me.matthewe.atherial.narcos.narcosdrugs.menus;

import me.matthewe.atherial.narcos.narcosdrugs.config.MenuConfig;
import me.matthewe.atherial.narcos.narcosdrugs.level.Level;
import me.matthewe.atherial.narcos.narcosdrugs.level.LevelHandler;
import me.matthewe.atherial.narcos.narcosdrugs.player.DrugPlayer;
import me.matthewe.atherial.narcos.narcosdrugs.utilities.NarcosAtherialMenu;
import net.atherial.api.menu.item.MenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;
import static net.atherial.api.plugin.utilities.MessageUtils.formatEnum;

/**
 * Created by Matthew E on 6/21/2019 at 1:32 AM for the project NarcosDrugs
 */
public class PlayerLevelsMenu extends NarcosAtherialMenu {

    public PlayerLevelsMenu(Player player) {
        super(player, colorize(MenuConfig.get().levelsMenu.title), MenuConfig.get().levelsMenu.rows);
        MenuConfig menuConfig = MenuConfig.get();
        MenuConfig.LevelsMenu levelsMenu = menuConfig.levelsMenu;

        if (levelsMenu.fillBackground) {
            for (int i = 0; i < 9; i++) {
                addBackgroundItem(i);
                addBackgroundItem((rows * 9) - (i + 1));
            }

            int startSlot = 9;
            for (int i = 1; i <= rows - 1; i++) {
                addBackgroundItem(startSlot);
                startSlot += 8;
                addBackgroundItem(startSlot);
                startSlot++;
            }
        }
        Level[] levels = new ArrayList<>(LevelHandler.get().getLevelMap().values()).toArray(new Level[0]);
        addItem(fixMenuItem(new MenuItem(levelsMenu.levelSlots.get(0), getLevelItem(null), (player1, clickType) -> getLevelItem(null), (player1, clickType) -> {
        })));

        addItem(fixMenuItem(new MenuItem(levelsMenu.levelSlots.get(levelsMenu.levelSlots.size() - 1), getLevelItem(levels[levels.length - 1]), (player1, clickType) -> getLevelItem(levels[levels.length - 1]), (player1, clickType) -> {
        })));
        for (int i = 1; i < levels.length; i++) {
            int slot = -1;
            try {
                slot = levelsMenu.levelSlots.get(i);
            } catch (Exception ignored) {
                continue;
            }
            Level level = levels[i - 1];

            addItem(fixMenuItem(new MenuItem(slot, getLevelItem(level), (player1, clickType) -> getLevelItem(level), (player1, clickType) -> {
            })));

        }
    }

    private ItemStack getLevelItem(Level level) {
        DrugPlayer drugPlayer = DrugPlayer.get(player);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        if (drugPlayer == null) {
            return new ItemStack(Material.AIR);
        }
        if (level == null) {
            if (drugPlayer.getLevel() == 0) {

                return MenuConfig.get().levelsMenu.currentLevelItem.create()
                        .replace(s -> s
                                .replaceAll("%current_experience%", decimalFormat.format(drugPlayer.getExperience()))
                                .replaceAll("%level%", "0")).build().getItemStack();

            }
            return MenuConfig.get().levelsMenu.completedLevelItem.create()
                    .replace(s -> s
                            .replaceAll("%current_experience%", decimalFormat.format(drugPlayer.getExperience()))
                            .replaceAll("%level%", "0")).build().getItemStack();
        }
        long requiredExperience = 0;
        ItemStack itemStack = null;
        if (drugPlayer.getLevel() == level.getLevel()) {
            long finalRequiredExperience1 = requiredExperience;
            itemStack = MenuConfig.get().levelsMenu.currentLevelItem.create()
                    .replace(s -> s
                            .replaceAll("%current_experience%", decimalFormat.format(drugPlayer.getExperience()))
                            .replaceAll("%level%", String.valueOf(level.getLevel()))).build().getItemStack();
        } else if (drugPlayer.getLevel() > level.getLevel()) {
            long finalRequiredExperience1 = requiredExperience;
            itemStack = MenuConfig.get().levelsMenu.completedLevelItem.create()
                    .replace(s -> s
                            .replaceAll("%current_experience%", decimalFormat.format(drugPlayer.getExperience()))
                            .replaceAll("%level%", String.valueOf(level.getLevel()))
                            .replaceAll("%required_experience%", String.valueOf(finalRequiredExperience1))
                            .replaceAll("%needed_experience%", String.valueOf(level.getNeededExperience()))).build().getItemStack();
        } else if (drugPlayer.getLevel() < level.getLevel()) {
            requiredExperience = drugPlayer.getExperience() >= level.getNeededExperience() ? 0 : level.getNeededExperience() - drugPlayer.getExperience();
            long finalRequiredExperience = requiredExperience;
            itemStack = MenuConfig.get().levelsMenu.upcomingLevelItem.create()
                    .replace(s -> s
                            .replaceAll("%current_experience%", decimalFormat.format(drugPlayer.getExperience()))
                            .replaceAll("%level%", String.valueOf(level.getLevel()))
                            .replaceAll("%required_experience%", String.valueOf(finalRequiredExperience))
                            .replaceAll("%needed_experience%", String.valueOf(level.getNeededExperience()))).build().getItemStack();
        } else {
            itemStack = new ItemStack(Material.AIR);
        }
        if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            List<String> newLore = new ArrayList<>();
            for (String loreLine : itemStack.getItemMeta().getLore()) {
                if (ChatColor.stripColor(loreLine).equalsIgnoreCase("%prices%")) {
                    newLore.add(colorize(MenuConfig.get().levelsMenu.priceHeader));
                    level.getPrices().forEach((drug, price) -> {
                        newLore.add(colorize(MenuConfig.get().levelsMenu.priceLore).replaceAll("%drug%", formatEnum(drug)).replaceAll("%price%", new DecimalFormat("#,###.##").format(price)));
                    });
                } else {
                    newLore.add(colorize(loreLine));
                }
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(newLore);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    private void addBackgroundItem(int slot) {
        addItem(fixMenuItem(new MenuItem(slot, MenuConfig.get().levelsMenu.backgroundItem.create().getItemStack(), (player1, clickType) -> {
        })));
    }
}
