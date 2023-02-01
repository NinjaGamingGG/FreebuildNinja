package gg.ninjagaming.ninjafreebuild.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.float
import org.ktorm.schema.varchar

object PlayerHomesTable : Table<Nothing>("PlayerHomes")    {
    val EntryId = varchar("EntryId").primaryKey()
    val PlayerId = varchar("PlayerId")
    var HomeName = varchar("HomeName")
    var HomeLocationWorld = varchar("HomeLocationWorld")
    var HomeLocationX = double("HomeLocationX")
    var HomeLocationY = double("HomeLocationY")
    var HomeLocationZ = double("HomeLocationZ")
    var HomeLocationYaw = float("HomeLocationYaw")
    var HomeLocationPitch = float("HomeLocationPitch")
}