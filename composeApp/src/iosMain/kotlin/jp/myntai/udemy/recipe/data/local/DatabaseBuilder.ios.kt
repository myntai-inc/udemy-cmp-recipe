package jp.myntai.udemy.recipe.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/myrecipe.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}
