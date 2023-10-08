package gg.ninjagaming.ninjafreebuild.managers

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.ktorm.dsl.*
import java.util.*

class HomeManager {

    fun setHome(sender: CommandSender, homeName: String) : Boolean {
        if (sender !is Player) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cOnly players can use this command!")
            return true
        }

        val config = NinjaFreebuild.getConfig()

        val spawnWorldName = config.getString("world_configuration.spawn.world_name")

        if (sender.location.world.name == spawnWorldName) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou cant set a home in spawn!")
            //return true
        }

        val farmWorldName = config.getString("world_configuration.farmworld.world_name")
        if (sender.location.world.name == farmWorldName) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou cant set a home in spawn!")
            //return true
        }

        val database = NinjaFreebuild.getDatabase()

        if (checkIfHomeExists(sender, homeName)) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou already have a home with that name! Please Delete it first to Continue!")
            return true
        }

        val uuid = UUID.randomUUID().toString()

        if (database == null) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cDatabase is not connected!")
            return true
        }

        database.insert(gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable) {
            set(it.EntryId, uuid)
            set(it.PlayerId, sender.uniqueId.toString())
            set(it.HomeName, homeName)
            set(it.HomeLocationWorld, sender.location.world.name)
            set(it.HomeLocationX, sender.location.x)
            set(it.HomeLocationY, sender.location.y)
            set(it.HomeLocationZ, sender.location.z)
            set(it.HomeLocationYaw, sender.location.yaw)
            set(it.HomeLocationPitch, sender.location.pitch)
        }

        sender.sendMessage("${NinjaFreebuild.getPrefix()}§aSuccessfully set home §6$homeName§a!")
        return true
    }

    fun deleteHome(sender: CommandSender, homeName: String): Boolean
    {
        if (sender !is Player) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cOnly players can use this command!")
            return true
        }

        if (homeName == "default") {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou cant delete your default home!")
            return true
        }

        val database = NinjaFreebuild.getDatabase()

        if (database == null) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cDatabase is not connected!")
            return true
        }

        if (!checkIfHomeExists(sender, homeName)) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou dont have a home with that name!")
            return true
        }

        database.delete(gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable) {
            it.PlayerId eq sender.uniqueId.toString() and (it.HomeName eq homeName)
        }

        sender.sendMessage("${NinjaFreebuild.getPrefix()}§aDeleted home §6${homeName}§a!")
        return true
    }

    fun teleportToHome(sender: CommandSender, homeName: String): Boolean
    {
        if (sender !is Player) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cOnly players can use this command!")
            return true
        }

        val database = NinjaFreebuild.getDatabase()

        if (database == null) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cDatabase is not connected!")
            return true
        }

        var homeLocationWorld = ""
        var homeLocationX = 0.0
        var homeLocationY = 0.0
        var homeLocationZ = 0.0
        var homeLocationYaw = 0f
        var homeLocationPitch = 0f

        for (row in database.from(gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable).select().
        where ((gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.PlayerId eq sender.uniqueId.toString()) and
                (gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.HomeName eq homeName))) {
            homeLocationWorld = row[gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.HomeLocationWorld].toString()
            homeLocationX = row[gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.HomeLocationX] as Double
            homeLocationY = row[gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.HomeLocationY] as Double
            homeLocationZ = row[gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.HomeLocationZ] as Double
            homeLocationYaw = row[gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.HomeLocationYaw] as Float
            homeLocationPitch = row[gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.HomeLocationPitch] as Float

        }

        if (!checkIfHomeExists(sender, homeName)) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cHome §6$homeName§c doesnt exist!")
            return true
        }

        sender.teleport(org.bukkit.Location(org.bukkit.Bukkit.getWorld(homeLocationWorld), homeLocationX, homeLocationY, homeLocationZ, homeLocationYaw, homeLocationPitch))

        sender.sendMessage("${NinjaFreebuild.getPrefix()}§aSuccessfully teleported to home §6$homeName§a!")

        return true
    }

    fun listHomes(sender: CommandSender): Boolean
    {
        if (sender !is Player) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cOnly players can use this command!")
            return true
        }

        val database = NinjaFreebuild.getDatabase()

        if (database == null) {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cDatabase is not connected!")
            return true
        }

        var homeList = ""

        for (row in database.from(gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable).select().
        where (gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.PlayerId eq sender.uniqueId.toString())) {
            homeList += row[gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.HomeName].toString() + ", "
        }

        if (homeList == "") {
            sender.sendMessage("${NinjaFreebuild.getPrefix()}§cYou dont have any homes!")
            return true
        }

        sender.sendMessage("${NinjaFreebuild.getPrefix()}§aYour homes: §6$homeList")
        return true
    }

    private fun checkIfHomeExists(player: Player, homeName: String): Boolean
    {
        val database = NinjaFreebuild.getDatabase()

        if (database == null) {
            player.sendMessage("${NinjaFreebuild.getPrefix()}§cDatabase is not connected!")
            return true
        }

        var homeExists = false

        for (row in database.from(gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable).select().
        where ((gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.PlayerId eq player.uniqueId.toString()) and
                (gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.HomeName eq homeName))) {
            homeExists = true
        }

        return homeExists
    }

    companion object {
        fun setHome(sender: CommandSender, homeName: String): Boolean {
            return HomeManager().setHome(sender, homeName)
        }

        fun deleteHome(sender: CommandSender, homeName: String): Boolean {
            return HomeManager().deleteHome(sender, homeName)
        }

        fun teleportHome(sender: CommandSender, homeName: String): Boolean {
            return HomeManager().teleportToHome(sender, homeName)
        }

        fun listHomes(sender: CommandSender): Boolean {
            return HomeManager().listHomes(sender)
        }

        fun hasHomeSet(player: Player): Boolean {
            val playerId = player.uniqueId.toString()

            val database = NinjaFreebuild.getDatabase() ?: return false

            var homeExists = false

            database.from(gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable)
                    .select()
                    .where ( gg.ninjagaming.ninjafreebuild.database.tables.PlayerHomesTable.PlayerId eq playerId )
                    .forEach {  homeExists = true}

            return homeExists
        }
    }

}