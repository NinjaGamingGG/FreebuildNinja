package gg.ninjagaming.ninjafreebuild.events

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import gg.ninjagaming.ninjafreebuild.database.tables.LastPlayerWorldPosition
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import org.ktorm.dsl.*
import org.ktorm.dsl.eq
import java.util.*

object PlayerTeleportEventListener: Listener {

        @EventHandler
        fun playerTeleportEvent(event: PlayerTeleportEvent){

            val config = NinjaFreebuild.getConfig()

            val lastPosition = event.from

            val farmWorldName = config.getString("world_configuration.farmworld.world_name")
            if (lastPosition.world.name != farmWorldName)
                return

            //If user Teleported inside a world i.e. /home command
            if (lastPosition.world.name == event.to.world.name)
                return

            val database = NinjaFreebuild.getDatabase()

            if (database == null){
                println("${NinjaFreebuild.getPrefix()}§cError while Saving LastPlayerWorldPosition: Database is not connected!")
                return
            }

            val uuid = UUID.randomUUID().toString()
            val inserted = database.insert(LastPlayerWorldPosition){
                set(it.EntryId, uuid)
                set(it.PlayerId, event.player.uniqueId.toString())
                set(it.WorldName, lastPosition.world.name)
                set(it.WorldLocationX, lastPosition.x)
                set(it.WorldLocationY, lastPosition.y)
                set(it.WorldLocationZ, lastPosition.z)
                set(it.WorldLocationPitch, lastPosition.pitch)
                set(it.WorldLocationYaw, lastPosition.yaw)
            }

            if (inserted == 0) {
                event.player.sendMessage("${NinjaFreebuild.getPrefix()}§cError: Unable to Update Last Position in Database!")
                println("${NinjaFreebuild.getPrefix()}§cError: Unable to save last player position in Database! User:" + event.player.name + " Position XYZ: "+ lastPosition.x+ ","+ lastPosition.y+ ","+ lastPosition.z)
            }

            val deleted = database.delete(LastPlayerWorldPosition){it.PlayerId eq event.player.uniqueId.toString() and (it.WorldName eq lastPosition.world.name) and (it.EntryId notEq uuid)}
            if (deleted == 0 ){
                event.player.sendMessage("${NinjaFreebuild.getPrefix()}§cError: Unable to Remove Last Saved Position from Database! Please Contact an Administrator")
            }


        }

}