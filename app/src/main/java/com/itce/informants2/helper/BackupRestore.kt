package com.itce.informants2.helper

import android.app.backup.*
import android.os.ParcelFileDescriptor
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.text.format.DateFormat
import com.itce.informants2.helper.Data.Companion.COUNT_EDITS
import com.itce.informants2.helper.Data.Companion.LAST_BACKUP
import com.itce.informants2.helper.Data.Companion.MAX_EDITS
import com.itce.informants2.helper.Data.Companion.TRY_BACKUP
import com.itce.informants2.helper.Utility.Companion.storeToPreferences
import java.io.IOException
import java.util.*


class BackupRestore : BackupAgentHelper() {


    // The name of the SharedPreferences file
    private val PREFS = "user_preferences"

    // A key to uniquely identify the set of backup data
    private val PREFS_BACKUP_KEY = "BackupInformantsPREFS"

    // The name of the app files
    private val STRUCTURES = "anagrafica.txt"
    //  private val PRODUCTS = "products.txt"
    //  private val ORDERS = "orders.txt"

    // A key to uniquely identify the set of backup data
    private val FILES_BACKUP_KEY = "BackupInformantsFILES"


    override fun onCreate() {

        SharedPreferencesBackupHelper(this, PREFS).also {
            addHelper(PREFS_BACKUP_KEY, it)
        }
        // Allocate a helper and add it to the backup agent
        FileBackupHelper(this, STRUCTURES, STRUCTURES).also {
            addHelper(FILES_BACKUP_KEY, it)

        }
    }

    @Throws(IOException::class)
    override fun onBackup(
        oldState: ParcelFileDescriptor,
        data: BackupDataOutput,
        newState: ParcelFileDescriptor
    ) {
        // Hold the lock while the FileBackupHelper performs backup
        synchronized(sDataLock) {
            super.onBackup(oldState, data, newState)
        }
    }

    @Throws(IOException::class)
    override fun onRestore(
        data: BackupDataInput,
        appVersionCode: Int,
        newState: ParcelFileDescriptor
    ) {
        // Hold the lock while the FileBackupHelper restores the file
        synchronized(sDataLock) {
            super.onRestore(data, appVersionCode, newState)
        }
    }


    // Object for intrinsic lock
    companion object {
        val sDataLock = Any()


        fun executeFullBackup() {

            //region Description
            TRY_BACKUP =
                DateFormat.format("yyyy-MM-dd HH:mm:ss", Calendar.getInstance().time).toString()
            // val backupFile = "Informants_backup_$TRY_BACKUP.txt"


            //  val outStream = contentResolver.openOutputStream(data.data!!)
            //  val inPath: Path = File(PROFILE_PATH_FILE).toPath()
            //  Files.copy(inPath, outStream!!)
            //  outStream.close() ///very important
            LAST_BACKUP = TRY_BACKUP

            BackupManager.dataChanged(PACKAGE_NAME)

            storeToPreferences("backup", LAST_BACKUP)
            storeToPreferences("dataEdited", "NO")
            COUNT_EDITS = 0
            MAX_EDITS = 0
            //endregion

        }


    }

}

