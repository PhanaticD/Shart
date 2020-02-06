package com.phanaticmc.shart

import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ItemMergeEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*


/**
 * Created by Lax on 12/17/2016.
 */
class Shart : JavaPlugin(), Listener {

    val r = Random()
    lateinit var instance: Shart
    val `is` = ItemStack(Material.COCOA_BEANS)

    override fun onEnable() {
        instance = this
        getCommand("shart").executor = Sharter(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig()
    }

    fun scatterShart(p: Player) {
        object : BukkitRunnable() {
            internal var i = 0
            override fun run() {
                i++
                if (i > config.getInt("sharts")) {
                    this.cancel()
                }
                val item = p.location.world.dropItem(p.location.add(0.0,0.3,0.0).subtract(p.location.direction.normalize().multiply(0.3)), `is`)
                if (config.getString("source").equals("butt", true)) {
                    item.velocity = p.location.direction.normalize().multiply(-0.5)
                } else {
                    item.velocity = Vector((r.nextFloat() * 2 - 1).toDouble(), 0.5, (r.nextFloat() * 2 - 1).toDouble())
                }
                item.setMetadata("no_pickup", FixedMetadataValue(instance, true))
                item.pickupDelay = Integer.MAX_VALUE
                p.location.world.spawnParticle(Particle.REDSTONE, item.location.add(0.0, 0.3,0.0), 1, DustOptions(Color.fromRGB(102, 51, 0), 1F))
                deleteItem(item, 40)

                if(i % 5 == 0) {
                    val bigitem = p.location.world.dropItem(p.location.add(0.0, 0.3, 0.0).subtract(p.location.direction.normalize().multiply(0.3)), ItemStack(Material.BROWN_WOOL, 2))
                    if (config.getString("source").equals("butt", true)) {
                        bigitem.velocity = p.location.direction.normalize().multiply(-0.1)
                    }
                    bigitem.setMetadata("no_pickup", FixedMetadataValue(instance, true))
                    bigitem.pickupDelay = Integer.MAX_VALUE
                    deleteItem(bigitem, 120)
                }
            }
        }.runTaskTimer(this, 1, 2)
    }

    @EventHandler
    fun onMerge(ev: ItemMergeEvent) {
        if (ev.entity.hasMetadata("no_pickup")) ev.isCancelled = true
    }

    @EventHandler
    fun onPickUp(ev: InventoryPickupItemEvent) {
        if (ev.item.hasMetadata("no_pickup")) ev.isCancelled = true
    }

    fun deleteItem(item: Item, time: Int) {
        object : BukkitRunnable() {
            override fun run() {
                item.remove()
                item.removeMetadata("no_pickup", instance)
            }
        }.runTaskLater(this, time.toLong())
    }
}
