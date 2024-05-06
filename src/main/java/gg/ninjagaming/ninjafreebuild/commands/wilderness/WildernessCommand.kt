package gg.ninjagaming.ninjafreebuild.commands.wilderness

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
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
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou can't use this command from the wilderness!")
            return true
        }

        val config = NinjaFreebuild.getConfig()

        val rtpAreaString = config.getString("world_configuration.wilderness.rtp_area")

        var rtpArea = rtpAreaString?.toDouble()

        if (rtpArea == null)
            rtpArea = 1000.0

        var isLocationValid = false
        var retries = 0

        while (!isLocationValid)
        {
            retries++

            val randomX =-rtpArea + Random.nextDouble() * (rtpArea - -rtpArea)
            val randomZ =-rtpArea + Random.nextDouble() * (rtpArea - -rtpArea)

            val wildernessWorld = Bukkit.getWorld("wilderness")

            if (wildernessWorld == null) {
                sender.sendMessage("${NinjaFreebuild.getPrefix()}§cThe wilderness world is not loaded, please try again later")

                return true
            }

            val randomY = wildernessWorld.getHighestBlockAt(Location(wildernessWorld, randomX, 0.0, randomZ)).location.y

            val blockedMaterials = listOf(Material.WATER,Material.LAVA,Material.END_PORTAL,Material.NETHER_PORTAL)

            if (blockedMaterials.contains(wildernessWorld.getBlockAt(Location(wildernessWorld,randomX,randomY-1,randomZ)).type)){
                isLocationValid = false
                continue
            }

            if (blockedMaterials.contains(wildernessWorld.getBlockAt(Location(wildernessWorld,randomX,randomY-2,randomZ)).type)){
                isLocationValid = false
                continue
            }

            isLocationValid = true
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§aTeleporting you to the wilderness...")

            sender.teleport(Location(wildernessWorld, randomX, randomY, randomZ))
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§aFinding your new Location took $retries attempts")
        }

     return true
    }
}