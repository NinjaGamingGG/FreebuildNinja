package gg.ninjagaming.ninjafreebuild.managers

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.WorldCreator
import kotlin.IllegalArgumentException

class WorldManager {

    companion object {
        fun prepareWildernessWorld() {
            val worldName = NinjaFreebuild.getConfig().getString("world_configuration.wilderness.world_name")
                ?: throw IllegalArgumentException("Wilderness world name not found in config.yml")

            prepareWorld(worldName, false)
        }

        fun prepareSpawnWorld() {
            val worldName = NinjaFreebuild.getConfig().getString("world_configuration.spawn.world_name")
                ?: throw IllegalArgumentException("Spawn world name not found in config.yml")


            val spawnWorld = prepareWorld(worldName, false) ?: return

            spawnWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            spawnWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false)

            spawnWorld.time = 6000
            spawnWorld.clearWeatherDuration = 1

            spawnWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        }

        fun prepareFarmWorld(){
            val worldName = NinjaFreebuild.getConfig().getString("world_configuration.FarmWorld.world_name")
                ?: throw IllegalArgumentException("Farm world name not found in config.yml")

            prepareWorld(worldName, true)
        }

        private fun prepareWorld(worldName: String, generateStructures: Boolean): World?
        {
            val loadWorld = Bukkit.getWorld(worldName)

            if (loadWorld != null)
                return loadWorld

            val newWorld = Bukkit.createWorld(WorldCreator(worldName).generateStructures(generateStructures))

            Bukkit.getWorlds().add(newWorld)

            return newWorld
        }
    }
}