package gg.ninjagaming.ninjafreebuild.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar

object FarmWorldIndex: Table<Nothing>("FarmWorldIndex") {
    val FarmWorldId = varchar("FarmworldId").primaryKey()
    val CreatedAt = timestamp("CreatedAt")
}