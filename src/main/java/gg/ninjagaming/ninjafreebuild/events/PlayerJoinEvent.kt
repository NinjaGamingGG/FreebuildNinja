package gg.ninjagaming.ninjafreebuild.events

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import gg.ninjagaming.ninjafreebuild.managers.HomeManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoinEventListener: Listener {
    @EventHandler
    fun playerJoinEvent(event: PlayerJoinEvent)
    {
        val config = NinjaFreebuild.getConfig()
        val spawnWorldName = config.getString("world_configuration.spawn.world_name")

        if (event.player.world.name != spawnWorldName)
            return

        //If player hasn't set a home don't tp to spawn on join
        if (!HomeManager.hasHomeSet(event.player))
        {
            event.player.sendMessage("${NinjaFreebuild.getPrefix()}Hey, you didn't got Teleported back to Spawn because you didn't set a home yet! You can do it anytime by using /home set")
            return
        }


        val spawnX = config.getDouble("world_configuration.spawn.spawn_location_x")
        val spawnY = config.getDouble("world_configuration.spawn.spawn_location_y")
        val spawnZ = config.getDouble("world_configuration.spawn.spawn_location_z")
        val spawnPitch = config.getDouble("world_configuration.spawn.spawn_location_pitch")
        val spawnYaw = config.getDouble("world_configuration.spawn.spawn_location_yaw")

        event.player.teleport(Location(Bukkit.getWorld(spawnWorldName),spawnX,spawnY,spawnZ,spawnYaw.toFloat(), spawnPitch.toFloat()))
    }
}