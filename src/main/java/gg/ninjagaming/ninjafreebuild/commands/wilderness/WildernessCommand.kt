package gg.ninjagaming.ninjafreebuild.commands.wilderness

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class WildernessCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cOnly players can use this command!")
            return true
        }

        if (sender.world.name == "wilderness") {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou can only use this command from the wilderness!")
            return true
        }

        val randomX =-3000.0 + Random.nextDouble() * (3000.0 - -3000.0)
        val randomZ =-3000.0 + Random.nextDouble() * (3000.0 - -3000.0)

        val wildernessWorld = Bukkit.getWorld("wilderness")

        if (wildernessWorld == null) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cThe wilderness world is not loaded, please try again later")

            return true
        }

        sender.sendMessage("${NinjaFreebuild.getPrefix()}§aTeleporting you to the wilderness...")

        val randomY = wildernessWorld.getHighestBlockAt(Location(wildernessWorld, randomX, 0.0, randomZ)).location

        sender.teleport(Location(wildernessWorld, randomX, randomY.y, randomZ))


     return true
    }
}