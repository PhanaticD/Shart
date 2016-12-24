package com.phanaticmc.shart

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by Lax on 12/17/2016.
 */
class Sharter(internal var shart: Shart) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>): Boolean {
        if (sender is Player && sender.hasPermission("shart.sharter")) {
            shart.scatterShart(sender)
        }
        return false
    }
}
