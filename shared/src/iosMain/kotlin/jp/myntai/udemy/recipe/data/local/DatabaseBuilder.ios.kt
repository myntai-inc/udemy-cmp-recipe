package jp.myntai.udemy.recipe.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSURL
import platform.Foundation.NSURLIsExcludedFromBackupKey

@OptIn(ExperimentalForeignApi::class)
actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val fileManager = NSFileManager.defaultManager
    val appSupportUrl = fileManager.URLForDirectory(
        directory = NSApplicationSupportDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null,
    )!!
    val dbDir = appSupportUrl.path!!
    fileManager.createDirectoryAtPath(dbDir, withIntermediateDirectories = true, attributes = null, error = null)
    val dbFilePath = "$dbDir/myrecipe.db"
    NSURL.fileURLWithPath(dbFilePath).setResourceValue(true, forKey = NSURLIsExcludedFromBackupKey, error = null)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}
