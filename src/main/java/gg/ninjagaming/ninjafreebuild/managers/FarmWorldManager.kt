package gg.ninjagaming.ninjafreebuild.managers

import gg.ninjagaming.ninjafreebuild.NinjaFreebuild
import gg.ninjagaming.ninjafreebuild.database.tables.FarmWorldIndex
import org.ktorm.database.Database
import org.ktorm.database.iterator
import org.ktorm.dsl.*
import java.io.File
import java.util.Calendar

class FarmWorldManager {
companion object{


    fun runnableTimer() :Runnable
    {
        val r = Runnable{
            tick()

        }
        return r
    }

    private fun tick()
    {
        val database = NinjaFreebuild.getDatabase()?: return

        val farmWorlds = database.from(FarmWorldIndex).select()

        for(row in farmWorlds.rowSet)
        {
            handleWorld(row)
        }

        if (farmWorlds.rowSet.size() == 0)
        {
            createNewFarmWorld()
        }
    }

    private fun handleWorld(row :QueryRowSet)
    {
        val database = NinjaFreebuild.getDatabase()?: return

        val worldId = row[FarmWorldIndex.FarmWorldId] as String

        val worldName = "Farmworld-$worldId"

        if (!WorldManager.worldExists(worldName))
        {
            database.delete(FarmWorldIndex){it.FarmWorldId eq worldId}

            return
        }

        val created = row[FarmWorldIndex.CreatedAt]?: return

        val lifetime = NinjaFreebuild.getConfig().getLong("world_configuration.FarmWorld.life_time_minutes")

        //Lifetime is added in minutes
        val expires = created.plusSeconds(60*lifetime)

        val now = Calendar.getInstance().time.toInstant()


        if (expires.isBefore(now))
        {
            deleteFarmWorld(database,worldId,worldName)
        }

        WorldManager.prepareFarmWorld(worldName)
    }

    private fun createNewFarmWorld()
    {
        val database = NinjaFreebuild.getDatabase()?: return

        val newId = randomString

        val worldName = "Farmworld-${newId}"

        WorldManager.prepareFarmWorld(worldName)

        database.insert(FarmWorldIndex){
            set(it.FarmWorldId,newId)
            set(it.CreatedAt,Calendar.getInstance().time.toInstant())
        }

        WorldManager.prepareFarmWorld(worldName)

    }

    private fun deleteFarmWorld(database: Database, worldId: String, worldName: String)
    {
        database.delete(FarmWorldIndex){it.FarmWorldId eq worldId}

        WorldManager.unloadWorld(worldName,false, delete = true)

        File("./$worldName").delete()

        createNewFarmWorld()
        return
    }

    private val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private val randomString = (1..5)
        .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}
}