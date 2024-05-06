package gg.ninjagaming.ninjafreebuild.managers

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import org.bukkit.*
import java.io.File
import kotlin.IllegalArgumentException

/**
 * The WorldManager class provides utility functions for preparing different types of worlds.
 */
class WorldManager {

    /**
     * The Companion object of the WorldManager class.
     *
     * This class provides utility functions for preparing different types of worlds.
     */
    companion object {
        /**
         * Prepares the wilderness world with the specified configurations.
         * It retrieves the wilderness world name from the configuration file and throws an exception if the name is not found.
         * Then, it calls the prepareWorld function to prepare the world with the retrieved name.
         *
         * @throws IllegalArgumentException if the wilderness world name is not found in the config.yml file.
         */
        fun prepareWildernessWorld() {
            val worldName = NinjaFreebuild.getConfig().getString("world_configuration.wilderness.world_name")
                ?: throw IllegalArgumentException("Wilderness world name not found in config.yml")

            prepareWorld(worldName, false)
        }

        /**
         * Prepares the spawn world with specified configurations.
         * It retrieves the spawn world name from the configuration file and throws an exception if the name is not found.
         * Then, it calls the prepareWorld function to prepare the world with the retrieved name.
         * If the prepareWorld function returns null, it means the world could not be created, and the method returns without further operations.
         *
         * The spawn world is configured with the following settings:
         * - GameRule: DO_DAYLIGHT_CYCLE is set to false
         * - GameRule: DO_WEATHER_CYCLE is set to false
         * - Difficulty is set to PEACEFUL
         * - PvP is disabled
         * - Time is set to 6000
         * - Weather clear duration is set to 1 (tick)
         * - GameRule: DO_MOB_SPAWNING is set to false
         */
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


        /**
         * Prepares a farm world by generating structures and initializing necessary components.
         *
         * @param worldName The name of the farm world.
         * @param generateStructures Indicates whether to generate structures in the farm world. Default is true.
         */
        fun prepareFarmWorld(worldName: String, generateStructures: Boolean = true){

            prepareWorld(worldName, generateStructures)
        }

        /**
         * Prepares a world with the given [worldName] and generates structures if [generateStructures] is true.
         * If the world is already loaded, it is returned. Otherwise, a new world is created with the specified parameters,
         * added to the list of Bukkit worlds, and then returned.
         *
         * @param worldName The name of the world to prepare.
         * @param generateStructures Whether to generate structures in the world.
         * @return The prepared world, or null if it could not be created.
         */
        private fun prepareWorld(worldName: String, generateStructures: Boolean): World?
        {
            val loadWorld = Bukkit.getWorld(worldName)

            //If world is already loaded return it
            if (loadWorld != null)
                return loadWorld

            val newWorld = Bukkit.createWorld(WorldCreator(worldName).generateStructures(generateStructures))

            Bukkit.getWorlds().add(newWorld)

            return newWorld
        }

        /**
         * Checks if a world exists in the file system.
         *
         * @param worldName The name of the world to check.
         * @return true if the world exists, false otherwise.
         */
        fun worldExists(worldName: String): Boolean {

            return File("./$worldName").exists()
        }

        /**
         * Unloads a world with the given name.
         *
         * @param woldName The name of the world to unload.
         * @param save Indicates whether to save the world before unloading it.
         * @param delete Indicates whether to delete the world folder after unloading it.
         */
        fun unloadWorld(woldName: String, save: Boolean, delete: Boolean = false)
        {
            val worldFolder = Bukkit.getWorld(woldName)?.worldFolder
            Bukkit.unloadWorld(woldName,save)

            if (delete && worldFolder != null)
                worldFolder.delete()

        }
    }
}