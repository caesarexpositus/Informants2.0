package com.itce.informants2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.documentfile.provider.DocumentFile
import com.itce.informants2.helper.Data.Companion.APPLICATION_EXIT
import com.itce.informants2.helper.DataProfiles
import com.itce.informants2.helper.DataProfiles.Companion.ARE_PROFILES_LOADED
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_PATH_FILE
import com.itce.informants2.helper.Utility.Companion.showToast
import com.itce.informants2.helper.UtilityProfiles
import com.itce.informants2.products.ListProductActivityUc
import com.itce.informants2.profiles.ListProfileActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    // state of activitY:   0=begin, 1=select directory, 2=select file, 3= copy file
    var step = 0

    lateinit var inStream: InputStream
    var selectedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        APPLICATION_EXIT = 0

        btnClose.setOnClickListener {
            finishAndRemoveTask()
        }

        btnExit.setOnClickListener {
            finishAndRemoveTask()
        }
        btnBack.setOnClickListener {
            userSelectDataDirecory()
        }
        btnOKLoad.setOnClickListener {
            val outPath = File(PROFILE_PATH_FILE).toPath()
            Files.copy(inStream, outPath)
            inStream.close()
            ARE_PROFILES_LOADED = true
            activateProfileList()                               //showMainMenu()
        }

        btnProfile.setOnClickListener {
            activateProfileList()
        }

        btnProduct.setOnClickListener {
            activateProductList()
        }

    }

    override fun onStart() {
        super.onStart()

        when (APPLICATION_EXIT) {
            1 -> {
                finish()
            }
            2 -> {
                finishAndRemoveTask()
            }
            else -> {
                if (step == 0)
                    try {
                        if (DataProfiles.IS_PROFILE_FIRST_TIME) {
                            DataProfiles.IS_PROFILE_FIRST_TIME = false

                            UtilityProfiles.setApplicationSettings(this)
                        }

                        val checkFile = File(PROFILE_PATH_FILE)
                        if (!checkFile.exists()) {
                            llLoad.isVisible = true
                        } else {
                            ARE_PROFILES_LOADED = true
                            activateProfileList()                       //showMainMenu()
                        }

                    } catch (exc: Exception) {
                        val message = "ERROR: " + exc.message
                        showToast(this, message)
                    }
            }
        }
    }

    private fun showMainMenu() {
        llLoad.isVisible = false
        tlListFiles.isVisible = false
        llMainMenu.isVisible = true
    }

    fun onloadclickNo(view: View) {
        UtilityProfiles.buildProfilesFillerData()
        ARE_PROFILES_LOADED = true
        activateProfileList() // showMainMenu()
    }

    fun onloadclickYes(view: View) {
        userSelectDataDirecory()
    }

    val RQS_OPEN_DOCUMENT_TREE: Int = 2;

    private fun userSelectDataDirecory() {
        step = 2

        try {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, RQS_OPEN_DOCUMENT_TREE)


        } catch (exp: Exception) {
            Toast.makeText(
                baseContext,
                "No File (Manager / Explorer)etc Found In Your Device",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == RQS_OPEN_DOCUMENT_TREE
            && resultCode == Activity.RESULT_OK
        ) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            resultData?.data?.also { uri ->

                ARE_PROFILES_LOADED = false

                val uriTree: Uri? = uri
                val documentFile = uriTree?.let { DocumentFile.fromTreeUri(this, it) }

                if (documentFile != null) {
                    //           ARE_PROFILES_LOADED = true
                    userDataSelectFile(documentFile)
                    return@also
                }
            }
        }
    }

    private fun userDataSelectFile(documentFile: DocumentFile) {
        step = 3
        try {

            val list: MutableList<String> = ArrayList()
            var element: String

            textInfo.text = "Select your data file."

            llLoad.isVisible = false
            tlListFiles.isVisible = true


            val documentFileByName = documentFile.listFiles().sortedBy { it ->
                it.name
            }

            for (file in documentFileByName) {
                element = file.name.toString()
                element += "              \nmodif:" + convertLongToTime(file.lastModified())
                list.add(element)
            }

            val adapter = ArrayAdapter(
                this,
                R.layout.list_datafiles_to_restore, list
            )
            listView.adapter = adapter

            listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, index, id -> // value of item that is clicked

                    if (selectedView != null)
                        selectedView!!.setBackgroundColor(0xffffffff.toInt())

                    selectedView = view
                    view.setBackgroundColor(0xffccccff.toInt())


                    val itemValue = listView.getItemAtPosition(index) as String

                    inStream =
                        contentResolver.openInputStream(documentFileByName[index].uri)!!

                }


        } catch (exp: Exception) {
            Toast.makeText(
                baseContext,
                "No File (Manager / Explorer)etc Found In Your Device",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(date)
    }


    private fun activateProfileList() {
        val intent = Intent(this, ListProfileActivity::class.java)
        //    .apply { putExtra("", "") }
        startActivity(intent)

    }

    private fun activateProductList() {
        val intent = Intent(this, ListProductActivityUc::class.java)
        //    .apply { putExtra("", "") }
        startActivity(intent)

    }

}




