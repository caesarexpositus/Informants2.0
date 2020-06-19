package com.itce.informants2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.itce.informants2.helper.BackupRestore.Companion.executeFullBackup
import com.itce.informants2.helper.Data
import com.itce.informants2.helper.Data.Companion.TRY_BACKUP
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_PATH_FILE
import com.itce.informants2.helper.UtilityProfiles
import com.itce.informants2.helper.Utility.Companion.showToast
import com.itce.informants2.helper.Utility.Companion.storeToPreferences
import com.itce.informants2.profiles.ListProfileActivity
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settings_toolbar?.title = title
        setSupportActionBar(settings_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
    }

    override fun onOptionsItemSelected(menuItem: MenuItem) =
        when (menuItem.itemId) {
            android.R.id.home -> {
                UtilityProfiles.setApplicationSettings(this)
                navigateUpTo(Intent(this, ListProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    private val BACKUP_CODE = 99

    fun executeBackup(view: View) {

        executeFullBackup()

        val todayDate = Calendar.getInstance().time
        TRY_BACKUP = LocalDateTime.now().toString()

        TRY_BACKUP =
            android.text.format.DateFormat.format(
                "yyyy-MM-dd HH:mm:ss",
                Calendar.getInstance().time
            ).toString()
        val backupFile = "Informants_Profiles_backup_${TRY_BACKUP}.txt"
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/txt"
            putExtra(Intent.EXTRA_TITLE, backupFile)
        }
        startActivityForResult(intent, BACKUP_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                BACKUP_CODE -> {
                    // The result data contains a URI for the document or directory that
                    // the user selected.
                    data.also { //uri ->
                        //   val dt = uri.data

                        try {
                            val outStream = contentResolver.openOutputStream(data.data!!)
                            val inPath: Path = File(PROFILE_PATH_FILE).toPath()
                            Files.copy(inPath, outStream!!)
                            outStream.close() ///very important
                            Data.LAST_BACKUP = Data.TRY_BACKUP

                            storeToPreferences("backup", Data.LAST_BACKUP)
                            storeToPreferences("dataEdited", "NO")
                            showToast(this, "INFO: Backup done.", true)
                            recreate()

                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }


    }


}

