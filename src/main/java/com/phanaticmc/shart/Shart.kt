package com.phanaticmc.shart

import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.Material
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
    val `is` = ItemStack(Material.INK_SACK, 1, 3.toShort())

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
                val item = p.location.world.dropItemNaturally(p.location, `is`)
                if (config.getString("source").equals("butt", true)) {
                    item.velocity = p.location.direction.normalize().multiply(-1)
                } else {
                    item.velocity = Vector((r.nextFloat() * 2 - 1).toDouble(), 0.5, (r.nextFloat() * 2 - 1).toDouble())
                }
                item.setMetadata("SHART", FixedMetadataValue(instance, true))
                item.pickupDelay = Integer.MAX_VALUE
                p.location.world.spigot().playEffect(item.location, Effect.POTION_SWIRL, 0, 0, (153 / 255).toFloat(), (76 / 255).toFloat(), (0 / 255).toFloat(), 1f, 0, 16)
                deleteItem(item, 40)
            }
        }.runTaskTimer(this, 1, 2)
        val item = p.location.world.dropItemNaturally(p.location, ItemStack(Material.WOOL, 1, 12.toShort()))
        item.customName = "${p.name}'s GIANT SHART"
        item.setMetadata("SHART", FixedMetadataValue(instance, true))
        item.isCustomNameVisible = true
        item.pickupDelay = Integer.MAX_VALUE
        deleteItem(item, 120)
    }

    @EventHandler
    fun onMerge(ev: ItemMergeEvent) {
        if (ev.entity.hasMetadata("SHART")) ev.isCancelled = true
    }

    @EventHandler
    fun onPickUp(ev: InventoryPickupItemEvent) {
        if (ev.item.hasMetadata("SHART")) ev.isCancelled = true
    }

    fun deleteItem(item: Item, time: Int) {
        object : BukkitRunnable() {
            override fun run() {
                item.remove()
            }
        }.runTaskLater(this, time.toLong())
    }
}
