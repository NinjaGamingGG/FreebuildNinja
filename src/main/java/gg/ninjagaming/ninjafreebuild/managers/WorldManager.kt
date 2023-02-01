package gg.ninjagaming.ninjafreebuild.managers

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.WorldCreator
import java.lang.IllegalArgumentException

class WorldManager {

    companion object {
        fun prepareWildernessWorld() {
            val worldName = NinjaFreebuild.getConfig().getString("world_configuration.wilderness.world_name")

            if (worldName == null) {
                throw IllegalArgumentException("Wilderness world name not found in config.yml")
            }

            val wildernessWorld = Bukkit.getWorld(worldName)


            if (wildernessWorld == null) {
                Bukkit.getLogger().warning("Wilderness world not found!")

                val newWorld = Bukkit.createWorld(WorldCreator(worldName))

                Bukkit.getWorlds().add(newWorld)
                return
            }
        }

        fun prepareSpawnWorld() {
            val worldName = NinjaFreebuild.getConfig().getString("world_configuration.spawn.world_name")


            if (worldName == null) {
                throw IllegalArgumentException("Spawn world name not found in config.yml")
            }

            val spawnWorld = Bukkit.getWorld(worldName)

            if (spawnWorld == null) {
                Bukkit.getLogger().warning("Spawn world not found!")

                val newWorld = Bukkit.createWorld(WorldCreator(worldName))

                Bukkit.getWorlds().add(newWorld)
                return
            }

            spawnWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            spawnWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false)

            spawnWorld.time = 6000
            spawnWorld.clearWeatherDuration = 1

            spawnWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false)

        }
    }
}