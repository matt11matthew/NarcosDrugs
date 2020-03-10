package me.matthewe.atherial.narcos.narcosdrugs.drug.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by Matthew E on 6/10/2019 at 6:23 PM for the project NarcosDrugs
 */
public class WorldDrugLocation {
    private String world;
    private int x;
    private int y;
    private int z;

    public WorldDrugLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static WorldDrugLocation fromLocation(Location location) {
        return new WorldDrugLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return world + ":" + x + ":" + y + ":" + z;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public static WorldDrugLocation fromString(String input) {
        if (input.contains(":")) {
            String[] strings = input.replaceAll(":", ",").split(",");
            String world = strings[0].trim();
            int x = Integer.parseInt(strings[1].trim());
            int y = Integer.parseInt(strings[2].trim());
            int z = Integer.parseInt(strings[3].trim());
            return new WorldDrugLocation(world, x, y, z);
        }
        return null;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
