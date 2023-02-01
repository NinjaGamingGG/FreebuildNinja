package gg.ninjagaming.ninjafreebuild.database

import org.ktorm.database.Database

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

    }
}