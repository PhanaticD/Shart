package com.phanaticmc.shart;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Lax on 12/17/2016.
 */
public class Sharter implements CommandExecutor {

    Shart shart;

    public Sharter(Shart shart) {
        this.shart = shart;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player && sender.hasPermission("shart.sharter")) {
            shart.scatterShart(((Player) sender));
        }
        return false;
    }
}
