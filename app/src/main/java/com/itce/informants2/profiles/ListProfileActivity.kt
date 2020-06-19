package com.itce.informants2.profiles

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat.LayoutParams
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itce.informants2.*
import com.itce.informants2.R.menu.menu_list_profile
import com.itce.informants2.adapters.ProfileAdapter
import com.itce.informants2.databinding.ActivityListProfileBinding
import com.itce.informants2.helper.BackupRestore.Companion.executeFullBackup
import com.itce.informants2.helper.Data
import com.itce.informants2.helper.DataProfiles.Companion.ADDING_PROFILE_ITEM
import com.itce.informants2.helper.Data.Companion.APPLICATION_EXIT
import com.itce.informants2.helper.DataProfiles.Companion.ARG_ITEM_ID
import com.itce.informants2.helper.Data.Companion.COUNT_EDITS
import com.itce.informants2.helper.DataProfiles.Companion.CURRENT_GROUP
import com.itce.informants2.helper.DataProfiles.Companion.GROUP_ACTIVE
import com.itce.informants2.helper.Data.Companion.LAST_BACKUP
import com.itce.informants2.helper.DataProfiles
import com.itce.informants2.helper.DataProfiles.Companion.LAST_PROFILE_ID
import com.itce.informants2.helper.DataProfiles.Companion.LAST_PROFILE_SEARCH
import com.itce.informants2.helper.DataProfiles.Companion.LIST_PROFILE_ITEMS
import com.itce.informants2.helper.DataProfiles.Companion.POPUP_NOTE_CODE
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_ASK_NAME
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_ASK_SEARCH
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_FILE_LINES
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_ITEM_SELECTED
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_MAPS_QUERY
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_SCROLL_POSITION
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_SORT_MODE
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_VOICE_COMMANDS
import com.itce.informants2.helper.DataProfiles.Companion.SETTINGS_SHOW_PROFILE_ACTIVE
import com.itce.informants2.helper.DataProfiles.Companion.SPEECH_ARGUMENT_CODE
import com.itce.informants2.helper.DataProfiles.Companion.SPEECH_COMMAND_CODE
import com.itce.informants2.helper.Utility.Companion.EDIT_TEXT_RETURN
import com.itce.informants2.helper.Utility.Companion.checkBackup
import com.itce.informants2.helper.Utility.Companion.checkForDigitsOnly
import com.itce.informants2.helper.Utility.Companion.popUpWithEdit
import com.itce.informants2.helper.Utility.Companion.showToast
import com.itce.informants2.helper.Utility.Companion.storeToPreferences
import com.itce.informants2.helper.UtilityProfiles
import com.itce.informants2.helper.UtilityProfiles.Companion.loadProfilesData
import kotlinx.android.synthetic.main.activity_list_profile.*
import kotlinx.android.synthetic.main.list_profile.*
import java.util.*


class ListProfileActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val TAG = this.toString()
    private lateinit var tts: TextToSpeech

    var mainListView: View? = null

    lateinit var profileRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //    setContentView(R.layout.activity_list_profile)  // <-- Comment it out  if "View Binding"
        val binding = ActivityListProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list_toolbar?.title = "   Profiles" //$title"
        list_toolbar?.subtitle =
            if (Data.LAST_EDIT.isNotEmpty()) "    at ${Data.LAST_EDIT}" else ""

        setSupportActionBar(list_toolbar)

        // ATTENTION!  ----------------------------------------------------------------------------
        // With "MainActivity" menu enabled:
        list_toolbar?.setLogo(R.drawable.informants_r72)             // <-- Comment to remove logo
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)     // <-- UnComment (it is the buttom <- on top)


        mainListView = llList    //context.findViewById<View>(R.id.llList)
        tts = TextToSpeech(this, this)

        if (checkBackup()) {
            helperBackupDialog()
        }

        UtilityProfiles.loadProfilesData(this, "")

        // original   setupRecyclerView(recyclerView = this.recyclerView_item_list)

        setupRecyclerView()

        when {
            PROFILE_SCROLL_POSITION > 0 -> {
                setRecyclerViewLayoutManager(PROFILE_SCROLL_POSITION)
            }
            PROFILE_SCROLL_POSITION == -1 -> LIST_PROFILE_ITEMS.forEachIndexed { index, item ->
                if (item._id.replace("-", "") == LAST_PROFILE_ID.toString()) {
                    setRecyclerViewLayoutManager(index)
                    return@forEachIndexed
                }
            }
        }
        setSortLabel()

//        registerForContextMenu(profileRecyclerView)
    }

    override fun onBackPressed() {   // is the virtual button ( <- ) at bottom of the devica
        APPLICATION_EXIT = 1
        finish()
    }

    fun onHomePressed() {
        APPLICATION_EXIT = 1
    }

    /*
       override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
           super.onCreateContextMenu(menu, v, menuInfo)
           val inflater = menuInflater
           inflater.inflate(R.menu.context_menu_profile, menu)
       }

       override fun onContextItemSelected(item: MenuItem?): Boolean {

           val sel = PROFILE_ITEM_SELECTED.a_structure

           return
           when (item!!.itemId) {
               R.id.action_detail ->{
                   Toast.makeText(applicationContext, "DETAIL: $sel", Toast.LENGTH_LONG).show()
                   return true
               }
               R.id.action_map ->{
                   Toast.makeText(applicationContext, "MAP: $sel", Toast.LENGTH_LONG).show()
                   return true
               }
               R.id.action_quick_note ->{
                   Toast.makeText(applicationContext, "MOTE: $sel", Toast.LENGTH_LONG).show()
                   return true
               }
               R.id.action_order ->{
                   Toast.makeText(applicationContext, "ORDER: $sel", Toast.LENGTH_LONG).show()
                   return true
               }
               else -> super.onOptionsItemSelected(item)
           }
       }

     */

    override fun onDestroy() {
        //Close the Text to Speech Library
        if (tts != null) {
            tts.stop()
            tts.shutdown()
            Log.d(TAG, "TTS Destroyed")
        }
        super.onDestroy()
    }

// orig fun setupRecyclerView(recyclerView: RecyclerView) {
//        tvCount.text = "Counter: " + LIST_PROFILE_ITEMS.size.toString()
//        recyclerView.adapter = RecyclerViewAdapter(this, LIST_PROFILE_ITEMS)
//    }

    private fun setupRecyclerView() {
        profileRecyclerView = findViewById(R.id.recyclerView_item_list)
        tvCount.text = "Counter: ${LIST_PROFILE_ITEMS.size}"
        profileRecyclerView.layoutManager = LinearLayoutManager(this)
        profileRecyclerView.adapter =
            ProfileAdapter(this, LIST_PROFILE_ITEMS)
    }


    // for SCROLL --------------------------------------------------------------
    private fun setRecyclerViewLayoutManager(position: Int) {
        // If a layout manager has already been set, get current scroll position.
        if (recyclerView_item_list.layoutManager != null) {
            PROFILE_SCROLL_POSITION = (recyclerView_item_list.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        }
        with(recyclerView_item_list) {
            layoutManager = this.layoutManager
            scrollToPosition(position)
        }
        PROFILE_SCROLL_POSITION = 0
    }
// -----------------------------------------------------------------------------------------
//   RecyclerView Adapter moved to new class file
// =========================================================================================

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(menu_list_profile, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView


        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                setSortLabel()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }
        })

        searchView.queryHint = "Search text or ?"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {

                LAST_PROFILE_SEARCH = newText  // to preserve search on sort
                UtilityProfiles.loadProfilesData(this@ListProfileActivity, newText)
                setupRecyclerView()
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                LAST_PROFILE_SEARCH = query  // to preserve search on sort
                UtilityProfiles.loadProfilesData(this@ListProfileActivity, query)
                setupRecyclerView()
                return false
            }
        })

        // DOES NOT WORK ON SDK > ICE_CREAM
        searchView.setOnCloseListener {
            LAST_PROFILE_SEARCH = ""
            executeSort()
            false
        }

        return super.onCreateOptionsMenu(menu)
    }

    private var searchDialog: AlertDialog? = null
    private lateinit var menuItem: MenuItem
    private fun helpSearchDialog(item: MenuItem) {
        //searchField = ""
        menuItem = item
        val linearLayout = LinearLayout(this)
        with(linearLayout) {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
            setBackgroundColor(0xffdddddd.toInt())
        }
        for (i in 0..12) {
            val tv = TextView(this)
            with(tv) {
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                val iRes = resources.getIdentifier("search_dlg_$i", "string", packageName)
                text = resources.getString(iRes)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                setOnClickListener {
                    onSearchItemClick(i.toString(), text.toString())
                }
                setPadding(80, 15, 0, 5)
                linearLayout.addView(tv)
            }
        }
        val dialog = AlertDialog.Builder(this@ListProfileActivity)
            .setTitle("Select the Field you want to search for:")
            //   .setView(dialogLayout)
            .setView(linearLayout)
            .setNegativeButton("Cancel") { dialog, _ ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        searchDialog = dialog.create()
        searchDialog!!.show()
    }


    fun onQuickNoteClicked(it: View, selectedId: String, selectedStructure: String) {

        //    val ref = it.parent.parent as View
        //    currentId =  ref.tvId.text.toString()
        //    val structure = ref.tvStructure.text.toString() + " #"  + currentId
        val subT = "$selectedStructure #${selectedId}"
        popUpWithEdit(
            this,
            "Quick NOTE",
            subT,
            "Cancel",
            "SAVE",
            prepareNoteToAdd(selectedId)
        )
    }

    private fun prepareNoteToAdd(selectedId: String) = Runnable()
    {
        //   fun run() {

        // prepare note to add
        val popupNote = EDIT_TEXT_RETURN
        val todayDate =
            DateFormat.format("yyyy-MM-dd", Calendar.getInstance().time)
                .toString()
                .trim()
        val newNote = todayDate + "°" + popupNote + "§"

        //---------------------------------------------------
        val index = PROFILE_FILE_LINES.indexOfFirst { ln ->
            ln.startsWith("$selectedId|")
        }
        val oldDataLine = PROFILE_FILE_LINES[index]
        val aParts = oldDataLine.split("££")
        val oldListNotes = if (aParts.size > 1) aParts[1] else ""
        val newListNotes = newNote + oldListNotes
        val newDataLine = aParts[0] + "££" + newListNotes

        replaceLine(selectedId, selectedId, newDataLine)

        UtilityProfiles.saveProfilesDataFile()
        COUNT_EDITS += 1
        storeToPreferences(
            "dataEdited",
            "YES ($COUNT_EDITS) - execute backup ASAP"
        )
        reloadList(true)

        //    }
    }


    //  fun onSearchItemClick(view: View) old signature{
    private fun onSearchItemClick(id: String, item: String) {
        if (id != "0") {
            //---------------
            val searchField = "$id#"
            val searchView = menuItem.actionView as SearchView
            menuItem.expandActionView()
            searchView.setQuery(searchField, false)
            // searchView.clearFocus()
            //-------------
            val tvM: TextView = tvMessage // findViewById(R.id.tvMessage)
            tvM.isVisible = true
            tvM.text = "Search in: $item"
        }
        searchDialog?.dismiss()
    }


    private fun helperBackupDialog() {
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle("Backup execution")
            .setMessage(
                "The last backup was on " + LAST_BACKUP
                        + " and should be executed now because of $COUNT_EDITS updates."
                        + "\nIt's strongly recommended to backup your data at least once a day. "
            )
            .setCancelable(false)
            .setPositiveButton("BACKUP now") { _, _ ->
                // Mark as deleted

                executeFullBackup()


            }
            .setNeutralButton("not now") { dialog, _ ->
                // Mark as deleted
                dialog.dismiss()
            }
        /*
    .setNegativeButton("Cancel") { dialog, _ ->
        // Dismiss the dialog
        dialog.dismiss()
    }
         */

        val alert = builder.create()
        alert.show()
    }

    //   private fun executeBackup() {
    //       executeFullBackup()
    //   }


    /*
    private lateinit var menuSort : MenuItem
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_list_profile, menu)
    menuSort = menu.findItem(R._id.action_sort)
    menuSort.actionView
    .setOnClickListener(View.OnClickListener {
    this@ItemListActivity.onOptionsItemSelected(menuSort)
    })
    return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem) =
        when (menuItem.itemId) {
            android.R.id.home -> {


        //    detailClose("")
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
*/

/*
    fun onSettingsClick(item: MenuItem) {
        val intent = Intent(this, SettingsActivity::class.java).apply { }
        this.startActivity(intent)
    }

 */

    fun onSettingsClick(item: MenuItem) {
        val intent = Intent(this, SettingsActivity::class.java).apply { }
        this.startActivity(intent)
    }


    fun onAboutClick(item: MenuItem) {
        val intent = Intent(this, AboutActivity::class.java).apply { }
        this.startActivity(intent)
    }

    fun onAddProfileClick(item: MenuItem?) {
        val intent = Intent(this, DetailProfileActivity::class.java).apply {
            //  putExtra(DetailProfileFragment.ARG_ITEM_ID, (LIST_PROFILE_ITEMS.size + 1).toString())
            putExtra(ARG_ITEM_ID, (LAST_PROFILE_ID + 1).toString())

            ADDING_PROFILE_ITEM = true
            PROFILE_SCROLL_POSITION = -1
        }
        this.startActivity(intent)
    }

    private fun executeSort() {
        setSortLabel()
        UtilityProfiles.loadProfilesData(this@ListProfileActivity, "")
        setupRecyclerView()
    }

    fun executeShow(item: MenuItem) {
        SETTINGS_SHOW_PROFILE_ACTIVE = !SETTINGS_SHOW_PROFILE_ACTIVE
        reloadList(false)
    }

    fun onGroupClick(item: MenuItem) {
        val navList: BottomNavigationView =
            bottom_navigation // findViewById(R.id.bottom_navigation)
        val navVisit: BottomNavigationView =
            bottom_navigation_group //  findViewById(R.id.bottom_navigation_group)
        GROUP_ACTIVE = !GROUP_ACTIVE

        navList.isVisible = !GROUP_ACTIVE
        navVisit.isVisible = GROUP_ACTIVE

        val tvM: TextView = tvMessage //findViewById(R.id.tvMessage)
        if (GROUP_ACTIVE) {
            CURRENT_GROUP = "A"
            tvM.text = "Tap the colored square to assign k_group"
            tvM.isVisible = true
        }
        PROFILE_SORT_MODE = DataProfiles.Companion.ProfilesSortBy.GROUP

        val navMenu = navList.menu
        val navItem = navMenu.findItem(R.id.action_sort)
        reloadList(false)
    }

    fun onClearGroupsClick(item: MenuItem) {
        // 0     1       2       3     4    5       6        7    8        9         10      11     12
        // _id|a_structure|b_address|c_city|d_time|e_phone|f_reference|g_flag1|h_flag2|i_lyTurnover|j_cyTurnover|k_group|l_NextVisit
        val newDataLines = mutableListOf<String>()
        PROFILE_FILE_LINES.forEachIndexed { index, line ->
            try {
                val fields = line.split("|").toMutableList()
                if (index > 0) fields[11] = "_"
                newDataLines.add(UtilityProfiles.composeProfileLine(fields, fields.size - 1))
            } catch (exc: Exception) {
                throw exc
            }
        }
        PROFILE_FILE_LINES = newDataLines
        UtilityProfiles.saveProfilesDataFile()
        reloadList(false)
    }

    var storeAssignItem: MenuItem? = null

    fun onAssignGroupClick(item: MenuItem) {
        storeAssignItem = item
        var chk = (CURRENT_GROUP.hashCode()) + 1
        if (chk > 90) chk = 65
        CURRENT_GROUP = chk.toChar().toString()
        item.title = "Group: $CURRENT_GROUP"
    }

    fun onRestartGroupClick(item: MenuItem) {
        var chk = 90
        CURRENT_GROUP = chk.toChar().toString()
        storeAssignItem?.let { onAssignGroupClick(it) }
    }

    fun onSaveGroupsClick(item: MenuItem) {
        LAST_PROFILE_SEARCH = ""
        UtilityProfiles.saveProfilesDataFile()
        reloadList(false)
    }

    fun onExitGroupsClick(item: MenuItem) {
        onGroupClick(item)
    }

    fun onSortClick(item: MenuItem) {
        PROFILE_SORT_MODE = when (PROFILE_SORT_MODE) {
            DataProfiles.Companion.ProfilesSortBy.STRUCT -> DataProfiles.Companion.ProfilesSortBy.ID
            DataProfiles.Companion.ProfilesSortBy.ID -> DataProfiles.Companion.ProfilesSortBy.GROUP
            DataProfiles.Companion.ProfilesSortBy.GROUP -> DataProfiles.Companion.ProfilesSortBy.STRUCT
        }
        executeSort()
    }

    private fun setSortLabel() {
        val tvM: TextView = tvMessage // findViewById(R.id.tvMessage)
        tvM.isVisible = true
        tvM.text = when (PROFILE_SORT_MODE) {
            DataProfiles.Companion.ProfilesSortBy.ID -> "List by ID"
            DataProfiles.Companion.ProfilesSortBy.GROUP -> "List by GROUP, CiTY"
            DataProfiles.Companion.ProfilesSortBy.STRUCT -> "List by STRUCTURE"
        }
    }

    fun onSearchClick(item: MenuItem) {
        helpSearchDialog(item)
        /*
        val searchView = item.actionView as SearchView
        item.expandActionView()
        searchView.setQuery(searchField, false)
        searchView.clearFocus()
         */
    }

    private fun reloadList(scrollToLast: Boolean = false) {
        UtilityProfiles.loadProfilesData(this@ListProfileActivity, LAST_PROFILE_SEARCH)
        setupRecyclerView()
        setSortLabel()
        if (scrollToLast)
            setRecyclerViewLayoutManager(PROFILE_SCROLL_POSITION)
    }

//region  -- VOICE COMMAND manager

    fun onVoiceClick(item: MenuItem) {
        startRecognizer(SPEECH_COMMAND_CODE)
    }

    private var recognizerIntent: Intent? = null
    private fun startRecognizer(requestCode: Int) {
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
        startActivityForResult(recognizerIntent, requestCode)
    }


    var speechWords: ArrayList<String>? = null
    var commandString = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // if (resultCode == Activity.RESULT_OK && data != null) {                  // <-- old NORMAL Way

        if (data != null)                                                           // <-- / my way to receive from popup
            if (resultCode == Activity.RESULT_OK || resultCode == requestCode) {    // <-- \

                when (requestCode) {

                    POPUP_NOTE_CODE -> {
                        // prepare note to add
                        val popupNote =
                            data?.extras!!["note"].toString().replace("  ", " ").trim()
                        val todayDate =
                            DateFormat.format("yyyy-MM-dd", Calendar.getInstance().time)
                                .toString()
                                .trim()
                        val newNote = todayDate + "°" + popupNote + "§"


                        val idToSaVe = PROFILE_ITEM_SELECTED._id

                        //---------------------------------------------------
                        val index = PROFILE_FILE_LINES.indexOfFirst { ln ->
                            ln.startsWith("$idToSaVe|")
                        }
                        val oldDataLine = PROFILE_FILE_LINES[index]

                        val aParts = oldDataLine.split("££")

                        val oldListNotes = if (aParts.size > 1) aParts[1] else ""

                        val newListNotes = newNote + oldListNotes

                        val newDataLine = aParts[0] + "££" + newListNotes

                        replaceLine(idToSaVe, idToSaVe, newDataLine)

                        UtilityProfiles.saveProfilesDataFile()
                        COUNT_EDITS += 1
                        storeToPreferences(
                            "dataEdited",
                            "YES ($COUNT_EDITS) - execute backup ASAP"
                        )
                        reloadList(true)
                    }

/*
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
                            LAST_BACKUP = TRY_BACKUP

// Also Android Backup------------------------

                            //     val info: PackageInfo? = try {
                            //         packageManager.getPackageInfo(packageName, 0)
                            //     } catch (e: PackageManager.NameNotFoundException) {
                            //         null
                            //     }
//
                            //  if (info != null) {

                            BackupRestore.executeAndroidBackup(PACKAGE_NAME)

                            //}
// -------------------------
                            UtilityProfiles.storeToPreferences("backup", LAST_BACKUP)
                            UtilityProfiles.storeToPreferences("dataEdited", "NO")

                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
 */
                    SPEECH_COMMAND_CODE -> {
                        speechWords =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        commandString = getKeyCommand(speechWords)
                        if (commandString.isNotEmpty()) {
                            if (!commandString.contains("?"))
                                executeCommand(commandString)
                        } else {
                            /*
                            Toast.makeText(applicationContext,
                                "Sorry, I didn't catch commands! Please try again",
                                Toast.LENGTH_LONG
                            ).show()
            */
                            speakText("Non ho capito! Riprova.")

                        }
                    }
                    SPEECH_ARGUMENT_CODE -> {
                        speechWords =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        //   val commandStringArgument = getKeyCommand(speechWords)
                        if (speechWords != null) {
                            val commandFinal = commandString.replace(
                                "?",
                                "§" + (speechWords as ArrayList<String>)[0]
                            )
                            executeCommand(commandFinal)
                        } else {
                            /*
                            Toast.makeText(applicationContext,
                                "Sorry, I didn't catch commands! Please try again",
                                Toast.LENGTH_LONG
                            ).show()
            */
                            speakText("Non ho ricevuto! Riprova.")
                        }
                    }
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Failed to recognize speech!",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

    }

    private fun replaceLine(editedId: String, idToSave: String, dataLine: String) {
        val index = PROFILE_FILE_LINES.indexOfFirst { ln ->
            ln.startsWith("$editedId|")
        }
        if (!idToSave.startsWith("XX"))
        // replace element
            PROFILE_FILE_LINES[index] = dataLine
        else
        // remove element
            PROFILE_FILE_LINES.removeAt(index)
    }

    private fun setMicrophoneActive(mode: Boolean) {
        val am = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.isMicrophoneMute = mode
    }

    private fun getKeyCommand(speechWords: ArrayList<String>?): String {
        var command = ""
        var ask: String

        if (speechWords != null) {
            mainLoop@ for (speech in speechWords) {
                command = ""
                val words = speech.toLowerCase(Locale.ROOT).trim().replace("  ", " ")
                loop@ for (sentence in PROFILE_VOICE_COMMANDS) {
                    if (sentence.speech == words) {
                        command = sentence.command
                        if (sentence.command.contains("?")) {
                            ask = if (sentence.command.contains("search"))
                                PROFILE_ASK_SEARCH
                            else
                                PROFILE_ASK_NAME
                            // --- ask more
                            setMicrophoneActive(false)
                            speakText(ask)

                            Thread.sleep(1500)
                            setMicrophoneActive(true)
                            startRecognizer(SPEECH_ARGUMENT_CODE)
                        }
                        break@loop
                    } else {
                        if (words.startsWith(sentence.speech)) {
                            var repl = "§"
                            if (sentence.speech == "searchFrom")
                                repl = ">"
                            command = sentence.command.replace(
                                "?",
                                repl + (words.replace(sentence.speech, "").trim())
                            )
                            break@loop
                        }
                    }
                }
                if (command.isNotEmpty())
                    break@mainLoop
            }
        }
        return command
    }


    private fun executeCommand(commandString: String) {
        var action = commandString
        var subject = ""
        var isValid = true
        /*
        tts.speak(
            "ricevuto " + commandString,
            TextToSpeech.QUEUE_ADD,
            null
        )
         */


        val aCommand = commandString.split("§")
        if (aCommand.size > 1) {
            action = aCommand[0].toLowerCase(Locale.ROOT)
            subject = aCommand[1].toLowerCase(Locale.ROOT)
        }

        when (action) {
            "addNew" -> onAddProfileClick(null)
            "sortId" -> {
                PROFILE_SORT_MODE = DataProfiles.Companion.ProfilesSortBy.ID
                executeSort()
            }
            "sortGroup" -> {
                PROFILE_SORT_MODE = DataProfiles.Companion.ProfilesSortBy.GROUP
                executeSort()
            }
            "sortStruct" -> {
                PROFILE_SORT_MODE = DataProfiles.Companion.ProfilesSortBy.STRUCT
                executeSort()
            }

            "listAll" -> {
                SETTINGS_SHOW_PROFILE_ACTIVE = false
                reloadList()
            }
            "listActive" -> {
                SETTINGS_SHOW_PROFILE_ACTIVE = true
                reloadList()
            }
            "cancelSearch" -> {
                LAST_PROFILE_SEARCH = ""
                reloadList()
            }


            else -> {
                // region Commands SEARCH or DETAIL/MAP
                var searchIn: String
                if (action.startsWith("search")) {
                    // SEARCH or SEARCHFROM
                    if (action.startsWith("search-")) {
                        searchIn = action.replace("search-", "")
                        searchIn = if (searchIn == "0") "" else "$searchIn#"

                    } else {
                        searchIn = action.replace("searchfrom-", "")
                        searchIn = if (searchIn == "0") "" else "$searchIn>"
                    }
                    LAST_PROFILE_SEARCH = "$searchIn$subject"
                    reloadList()

                } else {
                    // DETAIL/MAP
                    var itemToFind: DataProfiles.Companion.ProfileItem?
                    itemToFind = (if (checkForDigitsOnly(subject)) {
                        LIST_PROFILE_ITEMS.firstOrNull { it._id == subject }
                    } else {
                        LIST_PROFILE_ITEMS.firstOrNull { it ->
                            it.a_structure.toLowerCase(Locale.ROOT).contains(subject)
                        }
                    })

                    if (itemToFind == null) {
                        speakText("$subject non trovato. Riprova.")
                    } else {
                        PROFILE_ITEM_SELECTED = itemToFind


                        if (action == "detail_profile") {
                            ADDING_PROFILE_ITEM = false
                            val intent = Intent(this, DetailProfileActivity::class.java).apply {
                                putExtra(
                                    ARG_ITEM_ID,
                                    PROFILE_ITEM_SELECTED._id
                                )
                            }
                            this.startActivity(intent)
                        } else {
                            if (action == "map") {
                                PROFILE_MAPS_QUERY =
                                    PROFILE_ITEM_SELECTED.b_address + " " + PROFILE_ITEM_SELECTED.c_city
                                val intent = Intent(this, MapsActivity::class.java).apply {
                                }
                                this.startActivity(intent)
                            } else {
                                isValid = false
                            }
                        }
                    }

                }
                //endregion
            }
        }

        if (!isValid) {
            speakText("Segnalare problema per comando non eseguito $action")
            showToast(
                this,
                "Info: Segnalare problema per comando non eseguito: $action"
            )
        }

    }

    private fun speakText(text: String) {

        tts.speak(text, TextToSpeech.QUEUE_ADD, null, null)
    }

//endregion

    override fun onInit(status: Int) {}

    fun onExitAppClick(item: MenuItem) {

        APPLICATION_EXIT = 2

        finishAndRemoveTask()                           // native function very good!
    }


}





















