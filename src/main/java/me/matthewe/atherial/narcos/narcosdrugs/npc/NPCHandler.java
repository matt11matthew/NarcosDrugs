package me.matthewe.atherial.narcos.narcosdrugs.npc;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosHandler;
import me.matthewe.atherial.narcos.narcosdrugs.npc.listeners.NpcRightClickListener;
import me.matthewe.atherial.narcos.narcosdrugs.npc.listeners.SellMenuListener;
import net.atherial.api.config.BukkitConfig;
import net.atherial.api.plugin.AtherialTasks;
import net.atherial.api.plugin.hologram.HolographicDisplaysHologramManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matthew E on 6/19/2019 at 3:01 PM for the project NarcosDrugs
 */
public class NPCHandler extends NarcosHandler {
    private HolographicDisplaysHologramManager<NarcosDrugs> hologramManager;
    private List<NarcosNPC> npcs = new ArrayList<>();
    private List<NPC> drugBuyers = new ArrayList<>();

    private BukkitConfig npcConfig;
    private boolean loading = false;

    public NPCHandler(NarcosDrugs narcosDrugs, HolographicDisplaysHologramManager<NarcosDrugs> hologramManager) {
        super(narcosDrugs);
        this.hologramManager = hologramManager;
    }

    @Override
    public void onEnable() {

        registerListener(new NpcRightClickListener());
        registerListener(new SellMenuListener());
        loading = true;

        List<NPC> toRemoveList = new ArrayList<>();

        for (NPC npc : CitizensAPI.getNPCRegistry()) {
            if (npc.getEntity().hasMetadata("NARCOS_DRUGS_NPC") || ChatColor.stripColor(npc.getEntity().getCustomName()).startsWith("(RIGHT-")) {
                toRemoveList.add(npc);
            }
            if (npc.getName().equalsIgnoreCase("drugbuyer")) {
                drugBuyers.add(npc);
            }
        }
        for (NPC npc : toRemoveList) {
            npc.destroy();
        }


        AtherialTasks.runIn(() -> {
            loadNpcs();
        }, 100L);
        AtherialTasks.runIn(() -> {
            for (NPC drugBuyer : drugBuyers) {
                NarcosNPC narcosNPC = new NarcosNPC(drugBuyer);
                narcosNPC.update();
                npcs.add(narcosNPC);
            }
            drugBuyers.clear();
        }, 200L);
        AtherialTasks.runIn(this::spawnNpcs, 140L);


    }

    public List<NPC> getDrugBuyers() {
        return drugBuyers;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isNPC(Location location) {
        for (NarcosNPC npc : npcs) {
            Location location1 = npc.getLocation();
            if (location1.getBlockX() == location.getBlockX() && location1.getBlockY() == location.getBlockY() && location1.getBlockZ() == location.getBlockZ()) {
                return true;
            }
        }
        return false;
    }


    public NarcosNPC getNPC(Entity entity) {
        if (entity.hasMetadata("NPC") && entity.hasMetadata("NARCOS_DRUGS_NPC")) {
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);

            for (NarcosNPC narcosNPC : npcs) {
                if (narcosNPC.isSpawned() && narcosNPC.getNpc().getUniqueId().toString().equalsIgnoreCase(npc.getUniqueId().toString())) {
                    return narcosNPC;
                }
            }
        }
        return null;
    }

    public boolean isNPC(Entity entity) {
        return entity.hasMetadata("NPC") && entity.hasMetadata("NARCOS_DRUGS_NPC");
    }

    private void spawnNpcs() {
        for (NarcosNPC npc : npcs) {
            npc.spawn();
        }
        loading = false;
    }

    @Override
    public void onDisable() {

        despawnNpcs();

        saveNpcs();


    }


    private void despawnNpcs() {
        for (NarcosNPC npc : npcs) {
            npc.despawn();
        }
    }

    @Override
    public void reloadHandler() {
        saveNpcs();
    }

    public void saveNpcs() {
        FileConfiguration configuration = npcConfig.getConfiguration();

        for (NarcosNPC npc : npcs) {
            configuration.set("npcs." + npc.getUuid().toString() + ".uuid", npc.getUuid().toString());
            configuration.set("npcs." + npc.getUuid().toString() + ".location.x", npc.getLocation().getX());
            configuration.set("npcs." + npc.getUuid().toString() + ".location.y", npc.getLocation().getY());
            configuration.set("npcs." + npc.getUuid().toString() + ".location.world", npc.getLocation().getWorld().getName());
            configuration.set("npcs." + npc.getUuid().toString() + ".location.z", npc.getLocation().getZ());
            configuration.set("npcs." + npc.getUuid().toString() + ".location.yaw", Float.valueOf(npc.getLocation().getYaw()).doubleValue());
            configuration.set("npcs." + npc.getUuid().toString() + ".location.pitch", Float.valueOf(npc.getLocation().getPitch()).doubleValue());
        }
        npcConfig.setConfiguration(configuration);
        npcConfig.saveConfiguration();
    }

    public void loadNpcs() {
        this.npcConfig = new BukkitConfig("npcs.yml", this.plugin);

        FileConfiguration configuration = this.npcConfig.getConfiguration();
        if (configuration.isSet("npcs")) {
            for (String key : configuration.getConfigurationSection("npcs").getKeys(false)) {
                UUID uuid = UUID.fromString(key);

                ConfigurationSection npcSection = configuration.getConfigurationSection("npcs." + key);
                ConfigurationSection locationSection = npcSection.getConfigurationSection("location");
                double x = locationSection.getDouble("x");
                double y = locationSection.getDouble("y");
                double z = locationSection.getDouble("z");
                float yaw = ((float) (locationSection.getDouble("yaw")));
                float pitch = ((float) (locationSection.getDouble("pitch")));
                World world = Bukkit.getWorld(locationSection.getString("world"));

                Location location = new Location(world, x, y, z, yaw, pitch);
                NarcosNPC narcosNPC = new NarcosNPC(location, uuid);
                boolean add = npcs.add(narcosNPC);
                if (add) {
                    logger.info("Loaded npc " + narcosNPC.getUuid().toString());

                }

            }
        }
    }


    public HolographicDisplaysHologramManager<NarcosDrugs> getHologramManager() {
        return hologramManager;
    }


    public static NPCHandler get() {
        return NarcosDrugs.getInstance().getHandler(NPCHandler.class);
    }

    public boolean createNPC(Location location) {
        if (isNPC(location)) {
            return false;
        }
        NarcosNPC narcosNPC = new NarcosNPC(location, UUID.randomUUID());
        npcs.add(narcosNPC);
        npcs.get(npcs.size() - 1).spawn();

        saveNpcs();
        return true;
    }
}
