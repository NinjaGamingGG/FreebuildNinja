package gg.ninjagaming.ninjafreebuild.database

import org.ktorm.database.Database
import org.ktorm.database.use

fun initTables(database: Database?) {

    if (database == null) {
        return
    }

    database.useConnection { connection ->
        connection.createStatement().use { statement ->
            statement.execute("CREATE TABLE IF NOT EXISTS PlayerHomes (EntryId VARCHAR(36) PRIMARY KEY," +
                    "PlayerId VARCHAR(36), " +
                    "HomeName VARCHAR(36), " +
                    "HomeLocationWorld VARCHAR(36), " +
                    "HomeLocationX DOUBLE," +
                    "HomeLocationY DOUBLE," +
                    "HomeLocationZ DOUBLE," +
                    "HomeLocationYaw FLOAT," +
                    "HomeLocationPitch FLOAT)")
        }

        connection.createStatement().use { statement ->

            statement.execute("CREATE TABLE IF NOT EXISTS LastPlayerWorldPosition (EntryId VARCHAR(36) PRIMARY KEY,"+
            "PlayerId VARCHAR(36),"+
            "WorldName VARCHAR(36),"+
            "WorldLocationX DOUBLE,"+
            "WorldLocationY DOUBLE,"+
            "WorldLocationZ DOUBLE,"+
            "WorldLocationYaw FLOAT,"+
            "WorldLocationPitch FLOAT)")
        }

        connection.createStatement().use { statement ->
            statement.execute("CREATE TABLE IF NOT EXISTS FarmWorldIndex (FarmWorldId VARCHAR(36) PRIMARY KEY,"+
            "CreatedAt TIMESTAMP)")
        }

    }
}