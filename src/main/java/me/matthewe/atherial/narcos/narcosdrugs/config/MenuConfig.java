package me.matthewe.atherial.narcos.narcosdrugs.config;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import net.atherial.api.plugin.config.json.JsonConfig;
import net.atherial.api.plugin.item.AtherialConfigItem;
import net.atherial.api.plugin.item.AtherialConfigMenuItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 5/31/2019 at 6:19 PM for the project ScapeCrates
 */
public class MenuConfig extends JsonConfig<NarcosDrugs, MenuConfig> {


    public String npcSkin = "merchant";
    public LevelsMenu levelsMenu = new LevelsMenu();
    public PricesMenu pricesMenu = new PricesMenu();
    public DrugBuyerMenu drugBuyerMenu = new DrugBuyerMenu();
    public DrugPackMenu drugPackMenu = new DrugPackMenu();

    public MenuConfig(NarcosDrugs plugin) {
        super(plugin, "menus.json");
    }

    public static MenuConfig get() {
        return NarcosDrugs.getInstance().getMenuConfig();
    }

    @Override
    public Class<MenuConfig> getConfigClass() {
        return MenuConfig.class;
    }

    public static class DrugPackMenu {
        public String title = "&3Drug pack";
    }

    public static class Menu {
        public String title = "";
        public int rows = 1;

        public Menu(String title, int rows) {
            this.title = title;
            this.rows = rows;
        }

        public String getTitle() {
            return title;
        }

        public int getRows() {
            return rows;
        }
    }


    public static class DrugBuyerMenu {

        public SellDrugMenu sellDrugMenu = new SellDrugMenu();
        public MainMenu mainMenu = new MainMenu();


        public static class SellDrugMenu extends Menu {
            public AtherialConfigMenuItem sellItem = AtherialConfigMenuItem.builder()
                    .type(Material.STAINED_GLASS_PANE)
                    .amount(1)
                    .slot(53)
                    .data(DyeColor.LIME.getWoolData())
                    .information(AtherialConfigMenuItem.Information.builder()
                            .displayName("&a&lSell &7(&f$%amount%&7)")
                            .lore(new String[]{"&7Click to sell."})
                            .build())
                    .itemFlags(ItemFlag.values())
                    .build();

            public List<Integer> sellSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

            public AtherialConfigMenuItem backItem = AtherialConfigMenuItem.builder()
                    .type(Material.BARRIER)
                    .amount(1)
                    .slot(45)
                    .data(0)
                    .information(AtherialConfigMenuItem.Information.builder()
                            .displayName("&9&l\u00ab &c&lBack")
                            .lore(new String[]{"&7Click to go back."})
                            .build())
                    .itemFlags(ItemFlag.values())
                    .build();

            public AtherialConfigItem backgroundItem = AtherialConfigItem.builder()
                    .type(Material.STAINED_GLASS_PANE)
                    .amount(1)
                    .data(DyeColor.WHITE.getWoolData())
                    .information(AtherialConfigItem.Information.builder()
                            .displayName(" ")
                            .lore(new String[]{})
                            .build())
                    .itemFlags(ItemFlag.values())
                    .build();

            public boolean sideItems = true;

            public SellDrugMenu() {
                super("&3Sell drugs", 6);
            }
        }

        public static class MainMenu extends Menu {

            public MainMenu() {
                super("&3Drug Buyer", 3);
            }

            public AtherialConfigItem backgroundItem = AtherialConfigItem.builder()
                    .type(Material.STAINED_GLASS_PANE)
                    .amount(1)
                    .data(DyeColor.WHITE.getWoolData())
                    .information(AtherialConfigItem.Information.builder()
                            .displayName(" ")
                            .lore(new String[]{})
                            .build())
                    .itemFlags(ItemFlag.values())
                    .build();

            public List<Integer> skipSlots = Arrays.asList(11, 15);
            public boolean fillBackground = true;

            public AtherialConfigMenuItem sellInInventoryItem = AtherialConfigMenuItem.builder()
                    .type(Material.PAPER)
                    .slot(15)
                    .amount(1)
                    .data(0)
                    .information(AtherialConfigMenuItem.Information.builder()
                            .displayName("&3&lSell Drugs")
                            .lore(new String[]{"&7Click to open sell drug menu."})
                            .build())
                    .itemFlags(ItemFlag.values())
                    .build();
            public AtherialConfigMenuItem sellAllItem = AtherialConfigMenuItem.builder()
                    .type(Material.WOOL)
                    .slot(11)
                    .amount(1)
                    .data(DyeColor.RED.getWoolData())
                    .information(AtherialConfigMenuItem.Information.builder()
                            .displayName("&c&lSell All")
                            .lore(new String[]{"&7Click to sell all drugs.", "&f- &aDrug packs", "&f- &aInventory"})
                            .build())
                    .itemFlags(ItemFlag.values())
                    .build();
        }
    }

    public static class PricesMenu extends Menu {

        public AtherialConfigItem backgroundItem = AtherialConfigItem.builder()
                .type(Material.STAINED_GLASS_PANE)
                .amount(1)
                .data(DyeColor.BLACK.getWoolData())
                .information(AtherialConfigItem.Information.builder()
                        .displayName(" ")
                        .lore(new String[]{})
                        .build())
                .itemFlags(ItemFlag.values())
                .build();
        public AtherialConfigItem drugItem = AtherialConfigItem.builder()
                .type(Material.SUGAR)
                .amount(1)
                .data(0)
                .information(AtherialConfigItem.Information.builder()
                        .displayName("&9%drug%")
                        .lore("&7Experience Gained &9\u00bb &3%xp_gained%", "&7Sell price &9\u00bb &3$%price%")
                        .build())
                .itemFlags(ItemFlag.values())
                .build();

        public boolean fillBackground = true;

        public int[] priceSlots = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};


        public PricesMenu() {
            super("&3Drug prices", 4);
        }
    }


    public static class LevelsMenu extends Menu {

        public AtherialConfigItem backgroundItem = AtherialConfigItem.builder()
                .type(Material.STAINED_GLASS_PANE)
                .amount(1)
                .data(DyeColor.BLACK.getWoolData())
                .information(AtherialConfigItem.Information.builder()
                        .displayName(" ")
                        .lore(new String[]{})
                        .build())
                .itemFlags(ItemFlag.values())
                .build();
        public AtherialConfigItem currentLevelItem = AtherialConfigItem.builder()
                .type(Material.STAINED_GLASS)
                .amount(1)
                .data(DyeColor.ORANGE.getWoolData())
                .information(AtherialConfigItem.Information.builder()
                        .displayName("&9Level &7\u00bb &3%level%")
                        .lore("&3Current Experience &7\u00bb &9%current_experience%")
                        .build())
                .itemFlags(ItemFlag.values())
                .build();
        public AtherialConfigItem completedLevelItem = AtherialConfigItem.builder()
                .type(Material.STAINED_GLASS)
                .amount(1)
                .data(DyeColor.LIME.getWoolData())
                .information(AtherialConfigItem.Information.builder()
                        .displayName("&9Level &7\u00bb &3%level%")
                        .lore("&3Required Experience &7\u00bb &90")
                        .build())
                .itemFlags(ItemFlag.values())
                .build();
        public AtherialConfigItem upcomingLevelItem = AtherialConfigItem.builder()
                .type(Material.STAINED_GLASS)
                .amount(1)
                .data(DyeColor.RED.getWoolData())
                .information(AtherialConfigItem.Information.builder()
                        .displayName("&9Level &7\u00bb &3%level%")
                        .lore("&3Required Experience &7\u00bb &9%required_experience%", "", "%prices%")
                        .build())
                .itemFlags(ItemFlag.values())
                .build();
        public String priceHeader = "&3&lPrices";
        public String priceLore = "&9%drug% &7$%price%";
        public boolean fillBackground = true;

        public List<Integer> levelSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

        public LevelsMenu() {
            super("&3Drug levels", 5);
        }

    }

}