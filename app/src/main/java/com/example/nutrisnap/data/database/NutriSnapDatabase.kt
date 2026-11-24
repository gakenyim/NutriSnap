package com.example.nutrisnap.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nutrisnap.data.models.Meal

@Database(
    entities = [Meal::class], // List all entity classes in this database
    version = 1,            // Bump this number later if you change table columns
    exportSchema = false
)
abstract class NutriSnapDatabase : RoomDatabase() {

    // Room generates the implementation of this DAO automatically
    abstract fun mealDao(): MealDao

    companion object {
        // @Volatile ensures changes to INSTANCE are immediately visible across threads
        @Volatile
        private var INSTANCE: NutriSnapDatabase? = null

        fun getDatabase(context: Context): NutriSnapDatabase {
            // Return existing database if already created; otherwise build it once safely
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutriSnapDatabase::class.java,
                    "nutrisnap_database" // Name of the database file on device
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}