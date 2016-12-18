package com.phanaticmc.shart;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Created by Lax on 12/17/2016.
 */
public class Shart extends JavaPlugin implements Listener {

    public Random r = new Random();
    public Shart instance;
    public ItemStack is = new ItemStack(Material.INK_SACK, 1, (short) 3);

    public void onEnable() {
        instance = this;
        getCommand("shart").setExecutor(new Sharter(this));
        saveDefaultConfig();
    }

    public void scatterShart(final Player p) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                i++;
                if (i > getConfig().getInt("sharts")) {
                    this.cancel();
                }
                Item item = p.getLocation().getWorld().dropItemNaturally(p.getLocation(), is);
                if (getConfig().getString("source").equalsIgnoreCase("butt")) {
                    item.setVelocity(p.getLocation().getDirection().normalize().multiply(-1));
                } else {
                    item.setVelocity(new Vector(r.nextFloat() * 2 - 1, 0.5, r.nextFloat() * 2  - 1));
                }
                item.setMetadata("SHART", new FixedMetadataValue(instance, true));
                item.setPickupDelay(Integer.MAX_VALUE);
                p.getLocation().getWorld().spigot().playEffect(item.getLocation(), Effect.POTION_SWIRL, 0, 0, 153/255, 76/255, 0/255, 1, 0, 16);
                deleteItem(item, 40);
            }
        }.runTaskTimer(this, 1, 2);
        Item item = p.getLocation().getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.WOOL, 1, (short) 12));
        item.setCustomName(ChatColor.DARK_PURPLE + p.getName()+"\'s GIANT SHART");
        item.setCustomNameVisible(true);
        item.setPickupDelay(Integer.MAX_VALUE);
        deleteItem(item, 120);
    }

    @EventHandler
    public void onMerge(ItemMergeEvent ev) {
        if (ev.getEntity().hasMetadata("SHART")) ev.setCancelled(true);
    }

    public void deleteItem(final Item item, int time) {
        new BukkitRunnable() {
            @Override
            public void run() {
                item.remove();
            }
        }.runTaskLater(this, time);
    }
}
