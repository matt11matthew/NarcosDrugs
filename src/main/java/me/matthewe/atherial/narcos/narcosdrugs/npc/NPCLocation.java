package me.matthewe.atherial.narcos.narcosdrugs.npc;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class NPCLocation {
    private String world;
    private int x;
    private int y;
    private int z;
    private float yaw;
    private float pitch;

    public NPCLocation(String world, int x, int y, int z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static NPCLocation fromLocation(Location location) {
        return new NPCLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return world + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public Location getCenterLocation() {
        return new Location(Bukkit.getWorld(world), x+0.5, y, z+0.5, yaw, pitch);
    }
    public static NPCLocation fromString(String input) {
        if (input.contains(":")) {
            String[] strings = input.replaceAll(":", ",").split(",");
            String world = strings[0].trim();
            int x = Integer.parseInt(strings[1].trim());
            int y = Integer.parseInt(strings[2].trim());
            int z = Integer.parseInt(strings[3].trim());
            float yaw = Float.parseFloat(strings[4].trim());
            float pitch = Float.parseFloat(strings[5].trim());
            return new NPCLocation(world, x, y, z, yaw, pitch);
        }
        return null;
    }

    public String getWorld() {
        return world;
    }



    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
