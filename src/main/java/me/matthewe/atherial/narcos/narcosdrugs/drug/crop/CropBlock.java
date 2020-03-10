package me.matthewe.atherial.narcos.narcosdrugs.drug.crop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Created by Matthew E on 6/9/2019 at 1:35 PM for the project NarcosDrugs
 */
public class CropBlock {
    private Offset offset;
    private Material type;
    private byte data;

    public CropBlock(Offset offset, Material type, byte data) {
        this.offset = offset;
        this.type = type;
        this.data = data;
    }

    public Offset getOffset() {
        return offset;
    }

    public Material getType() {
        return type;
    }

    public byte getData() {
        return data;
    }

    public Location getNewLocation(Location location) {

        double y = location.getY();
        if (offset.up > 0) {
            y += offset.up;
        }
        if (offset.down > 0) {
            y -= offset.down;
        }
        return new Location(location.getWorld(), location.getX(), y, location.getZ(), location.getYaw(), location.getPitch());
    }

    public void remove(Location location) {
        Block block = getNewLocation(location).getBlock();
        if (block.getType() != Material.AIR) {
            block.setType(Material.AIR);
        }
    }

    public void place(Location location) {
        Block block = getNewLocation(location).getBlock();
        if (type == Material.SEEDS) {
            block.setType(Material.CROPS, true);
        } else {
            block.setType(type, true);
        }
        block.setData(data, true);

    }

    public static class Offset {
        private int up;
        private int down;


        public Offset(int up, int down) {
            this.up = up;
            this.down = down;
        }

        public int getUp() {
            return up;
        }

        public int getDown() {
            return down;
        }
    }
}
