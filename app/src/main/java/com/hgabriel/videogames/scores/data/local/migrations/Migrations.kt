package com.hgabriel.videogames.scores.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE game ADD COLUMN liked INTEGER DEFAULT 0 NOT NULL")
    }
}