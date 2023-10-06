package gg.ninjagaming.ninjafreebuild.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.float
import org.ktorm.schema.varchar

object LastPlayerWorldPosition: Table<Nothing>("LastPlayerWorldPosition"){
    val EntryId = varchar("EntryId").primaryKey()
    val PlayerId = varchar("PlayerId")
    val WorldName = varchar("WorldName")
    val WorldLocationX = double("WorldLocationX")
    val WorldLocationY = double("WorldLocationY")
    val WorldLocationZ = double("WorldLocationZ")
    val WorldLocationYaw = float("WorldLocationYaw")
    val WorldLocationPitch = float("WorldLocationPitch")
}