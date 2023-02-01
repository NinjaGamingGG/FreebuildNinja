package gg.ninjagaming.ninjafreebuild.database

import org.ktorm.database.Database

fun connectDatabase(connectionString: String, userName: String, password: String, databaseName: String): Database {

    val database = Database.connect(connectionString, user = userName, password = password)

    database.useConnection { connection ->
        connection.createStatement().use { statement ->
            statement.execute("CREATE DATABASE IF NOT EXISTS $databaseName")
        }
    }

    return Database.connect("$connectionString/$databaseName", user = userName, password = password)
}

