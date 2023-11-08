package gg.ninjagaming.ninjafreebuild.managers

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import org.bukkit.*
import java.io.File
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
            spawnWorld.difficulty = Difficulty.PEACEFUL
            spawnWorld.pvp = false

            spawnWorld.time = 6000
            spawnWorld.clearWeatherDuration = 1

            spawnWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        }

        fun prepareFarmWorld(worldName: String){

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

        fun worldExists(worldName: String): Boolean {

            return File("./$worldName").exists()
        }

        fun unloadWorld(woldName: String, save: Boolean, delete: Boolean = false)
        {
            val worldFolder = Bukkit.getWorld(woldName)?.worldFolder
            Bukkit.unloadWorld(woldName,save)

            if (delete && worldFolder != null)
                worldFolder.delete()

        }
    }
}