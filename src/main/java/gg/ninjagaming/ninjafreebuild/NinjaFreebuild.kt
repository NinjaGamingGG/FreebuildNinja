package gg.ninjagaming.ninjafreebuild

import gg.ninjagaming.ninjafreebuild.commands.farmworld.FarmWorldCommand
import gg.ninjagaming.ninjafreebuild.commands.home.DeleteHomeCommand
import gg.ninjagaming.ninjafreebuild.commands.home.HomeCommand
import gg.ninjagaming.ninjafreebuild.commands.home.SetHomeCommand
import gg.ninjagaming.ninjafreebuild.commands.home.TeleportHomeCommand
import gg.ninjagaming.ninjafreebuild.commands.spawn.SpawnCommand
import gg.ninjagaming.ninjafreebuild.commands.wilderness.WildernessCommand
import gg.ninjagaming.ninjafreebuild.database.connectDatabase
import gg.ninjagaming.ninjafreebuild.managers.WorldManager
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.ktorm.database.Database
import java.io.File
import java.lang.IllegalArgumentException

@Suppress("SpellCheckingInspection")
class NinjaFreebuild : JavaPlugin() {

    private val instance: NinjaFreebuild = this

    private val configFile: File = File(dataFolder, "config.yml")
    private val config: FileConfiguration = YamlConfiguration.loadConfiguration(configFile)

    override fun onEnable() {
        // Plugin startup logic
        registerCommands()
        loadFiles()

        setDatabase(connectDatabase(
            getConfig().getString("database.connectionString")!!,
            getConfig().getString("database.userName")!!,
            getConfig().getString("database.password")!!,
            getConfig().getString("database.databaseName")!!
        ))

        gg.ninjagaming.ninjafreebuild.database.initTables(mysqlDatabase)

        WorldManager.prepareWildernessWorld()
        WorldManager.prepareSpawnWorld()
        WorldManager.prepareFarmWorld()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun registerCommands() {
        instance.getCommand("teleporthome")?.setExecutor(TeleportHomeCommand())
        instance.getCommand("sethome")?.setExecutor(SetHomeCommand())
        instance.getCommand("deletehome")?.setExecutor(DeleteHomeCommand())
        instance.getCommand("home")?.setExecutor(HomeCommand())

        instance.getCommand("wilderness")?.setExecutor(WildernessCommand())
        instance.getCommand("spawn")?.setExecutor(SpawnCommand())
        instance.getCommand("farmworld")?.setExecutor(FarmWorldCommand())
    }

    private fun loadFiles(){
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        if (!configFile.exists()) {
            val classLoader = javaClass.classLoader
            val resource = classLoader.getResource("config.yml") ?: throw IllegalArgumentException("Config file not found")

            configFile.createNewFile()
            File(dataFolder, "config.yml").writeBytes(resource.readBytes())
        }

        config.load(configFile)
    }

    companion object{
        private var mysqlDatabase: Database? = null

        fun getConfig(): FileConfiguration {
            return Bukkit.getPluginManager().getPlugin("NinjaFreebuild")!!.config
        }

        fun getDatabase(): Database? {
            return mysqlDatabase
        }

        fun setDatabase(database: Database) {
            mysqlDatabase = database
        }

        fun getPrefix(): String {

            val config =  Bukkit.getPluginManager().getPlugin("NinjaFreebuild")!!.config

            if (!config.contains("prefix") || config.getString("prefix") == null) {
                return "§c[§4NinjaFreebuild§c]§r"
            }

            return config.getString("prefix")!!
        }
    }
}