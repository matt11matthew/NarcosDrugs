package me.matthewe.atherial.narcos.narcosdrugs.config;

import net.atherial.api.config.SerializedName;
import net.atherial.api.config.YamlConfig;
import net.atherial.api.plugin.utilities.MessageUtils;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class Messages extends YamlConfig {
    @SerializedName("help.givewand")
    public static String HELP_GIVEWAND = "Give wand.";
    @SerializedName("messages.sellAllDrugs")
    public static String MESSAGES_SELL_ALL_DRUGS = "%prefix%Sold all drugs for &7$%amount%";
    @SerializedName("messages.sellDrugs")
    public static String MESSAGES_SELL_DRUGS = "%prefix%Sold drugs for &7$%amount%";
    @SerializedName("messages.errors.npc.npcStillLoading")
    public static String ERRORS_NPCS_LOADING = "%prefix%&cNpcs are still loading...";
    @SerializedName("errors.priceNotSet")
    public static String PRICE_UNKNOWN = "&cPrice not set.";

    @SerializedName("settings.eatSound")
    public static String CONSUME_SOUND = "EAT";
    @SerializedName("settings.qualityLore")
    public static Boolean QUALITY_LORE = true;
    @SerializedName("help.prices")
    public static String HELP_PRICES = "View drug prices.";
    @SerializedName("help.levels")
    public static String HELP_LEVELS = "View levels.";
    @SerializedName("settings.npcFaceDistance")
    public static Double SETTINGS_MAX_FACE_DISTANCE = 10.0D;
    @SerializedName("messages.npc.create")
    public static String MESSAGES_NPC_CREATE = "%prefix%Spawned npc at &7(&b%x%&7, &b%y%&7, &b%z%&7, &b%yaw%&7, &b%pitch%&7)&3.";
    @SerializedName("messages.errors.npc.alreadyExists")
    public static String ERRORS_NPC_ALREADY_EXISTS = "%prefix%&cA NPC already exists at your location.";
    @SerializedName("help.npc.create")
    public static String HELP_NPC_CREATE = "Creates npc.";
    @SerializedName("holograms.npcName")
    public static List<String> NPC_NAME = Arrays.asList("&b&lDrug Buyer", "&7&l(RIGHT-CLICK)");
    @SerializedName("settings.drugCropInfo")
    public static Boolean SETTINGS_DRUG_CROP_INFO = true;
    @SerializedName("help.drugpack.give")
    public static String HELP_DRUGPACK_GIVE = "Gives drug pack.";
    @SerializedName("help.seed.give")
    public static String HELP_SEED_GIVE = "Gives seed.";
    @SerializedName("messages.errors.cantFindDrug")
    public static String ERRORS_DRUG_DOES_NOT_EXIST = "%prefix%&cThe drug %drug% does not exist.";
    @SerializedName("help.help")
    public static String HELP_HELP = "View help.";
    @SerializedName("messages.errors.cantFindPlayer")
    public static String ERRORS_CANT_FIND_PLAYER = "%prefix%&cCould not find the player %player%.";
    @SerializedName("messages.reload")
    public static String MESSAGES_RELOAD = "%prefix%Reloaded config.";
    @SerializedName("help.reload")
    public static String HELP_RELOAD = "Reload config.";
    @SerializedName("permissions.player")
    public static String PERMISSION_PLAYER = "narcosdrugs.player";
    @SerializedName("permissions.admin")
    public static String PERMISSION_ADMIN = "narcosdrugs.admin";
    @SerializedName("prefix")
    public static String PREFIX = "&9&lNarcos&3&lDrugs &7&l\u00bb &3";
    @SerializedName("messages.errors.number.invalid")
    public static String ERRORS_INVALID_NUMBER = "%prefix%&cInvalid number.";
    @SerializedName("settings.consoleDebug")
    public static Boolean SETTINGS_DEBUG = true;
    @SerializedName("settings.developmentMode")
    public static Boolean SETTINGS_DEVELOPMENT_MODE = false;
    @SerializedName("messages.errors.number.toLow")
    public static String ERRORS_NUMBER_TO_LOW = "%prefix%&cThe number &f%number%&c must be above &f%above%&c.";
    @SerializedName("messages.giveDrugSeeds")
    public static String MESSAGES_GIVE_DRUG_SEEDS = "%prefix%You gave &7%player% %amount%x &3of the drug &7%drug%&3.";
    @SerializedName("messages.getDrugSeeds")
    public static String MESSAGES_GET_DRUG_SEEDS = "%prefix%You received %amount%x &3of the drug &7%drug%&3 from &7%sender%&3.";
    @SerializedName("messages.errors.drugpack.doesntExist")
    public static String ERRORS_DRUGPACK_DOESNT_EXIST = "%prefix%&cThe drug pack &7%tier%&c does not exist.";

    @SerializedName("messages.cropStatus.header")
    public static String MESSAGES_CROP_STATUS_HEADER = "&7&m-----------------------";
    @SerializedName("messages.cropStatus.footer")
    public static String MESSAGES_CROP_STATUS_FOOTER = "&7&m-----------------------";
    @SerializedName("messages.cropStatus.text")
    public static String MESSAGES_CROP_STATUS_TEXT = "&3&lStatus";
    @SerializedName("messages.cropStatus.center")
    public static Boolean MESSAGES_CROP_STATUS_CENTER = true;
    @SerializedName("messages.cropStatus.lines")
    public static List<String> MESSAGES_CROP_STATUS_LINES = Arrays.asList("&7Name &9\u00bb &3%name%",
            "&7Planter &9\u00bb &3%planter%", "&7Stage &9\u00bb &3%stage%", "&7Grow Time &9\u00bb &3%growTime%");


    @SerializedName("messages.giveDrugPack")
    public static String MESSAGES_GIVE_DRUG_PACK = "%prefix%Gave &7%amount%x &3of tier &7%tier% &3drug pack to &7%player%&3.";
    @SerializedName("messages.getDrugPack")
    public static String MESSAGES_GET_DRUG_PACK = "%prefix%Received &7%amount%x &3of tier &7%tier% &3drug pack from &7%sender%&3.";
    @SerializedName("messages.errors.number.toHigh")
    public static String ERRORS_NUMBER_TO_HIGH = "%prefix%&cThe number &f%number%&c must be below &f%below%&c.";


    @SerializedName("timeFormat.hours")
    public static String TIME_FORMAT_HOURS = "&7%time%&3h";
    @SerializedName("timeFormat.minutes")
    public static String TIME_FORMAT_MINUTES = "&7%time%&3m";
    @SerializedName("timeFormat.seconds")
    public static String TIME_FORMAT_SECONDS = "&7%time%&3s";

    public Messages(Plugin plugin) {
        super("config.yml", plugin);
    }

    public static String replacePrefix(String input) {
        return MessageUtils.colorize(input).replaceAll("%prefix%", MessageUtils.colorize(PREFIX));
    }
}
