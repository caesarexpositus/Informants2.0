package com.itce.informants2.profiles

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itce.informants2.R
import com.itce.informants2.helper.DataProfiles.Companion.ADDING_PROFILE_ITEM
import com.itce.informants2.helper.DataProfiles.Companion.ARG_ITEM_ID
import com.itce.informants2.helper.Data.Companion.COUNT_EDITS
import com.itce.informants2.helper.DataProfiles.Companion.LAST_PROFILE_ID
import com.itce.informants2.helper.DataProfiles.Companion.NOTES
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_FILE_LINES
import com.itce.informants2.helper.Utility.Companion.setFieldPiped
import com.itce.informants2.helper.Utility.Companion.storeToPreferences
import com.itce.informants2.helper.UtilityProfiles
import kotlinx.android.synthetic.main.activity_detail_profile.*
import kotlinx.android.synthetic.main.detail_profile.*
import kotlinx.android.synthetic.main.detail_profile_note_row.view.*
import kotlinx.android.synthetic.main.detail_profile_note_row_new.*
import java.util.*

class DetailProfileActivity : AppCompatActivity() {

    //   var bEnabledEdit: Boolean = false
    private val fieldsList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_profile)
        detail_toolbar?.title = title
        setSupportActionBar(detail_toolbar)


        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            // Create the detail_profile fragment and add it to the activity
            // using a fragment transaction.
            val fragment = DetailProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        ARG_ITEM_ID,
                        intent.getStringExtra(ARG_ITEM_ID)
                    )
                }
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.item_detail_container, fragment)
                .commit()

        }

    }

    fun clickDataPicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, yearOfDate, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                this.etNoteDate.setText("$yearOfDate-${monthOfYear + 1}-$dayOfMonth")

                // Toast.makeText(
                //       this, """$dayOfMonth - ${monthOfYear + 1} - $year""",
                //       Toast.LENGTH_LONG
                //   ).show()

            },
            year,
            month,
            day
        )
        dpd.show()

    }

    private lateinit var thisMenu: Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_profile, menu)
        if (menu != null) {
            thisMenu = menu
        }
        thisMenu.forEach {
            it.isVisible = false
        }

        // 0     1       2       3     4    5       6        7    8        9         10      11     12
        // _id|a_structure|b_address|c_city|d_time|e_phone|f_reference|g_flag1|h_flag2|i_lyTurnover|j_cyTurnover|k_group|l_NextVisit

        if (ADDING_PROFILE_ITEM) {
            tvId.text = (LAST_PROFILE_ID + 1).toString()
            etStructure.setText("")
            etAddress.setText("")
            etCity.setText("")
            etTime.setText("")
            etPhone.setText("")
            etReference.setText("")
            ckFlag1.isChecked = false
            etFlag2.setText("")
            etLyTurn.setText("")
            etCyTurn.setText("")
            etGroup.setText("")
            etEmail.setText("")
            etDelivery.setText("")
            etCuPec.setText("")
            // TODO etNextVisit
            rowNoteNew.etNoteDate.setText("")
            rowNoteNew.etNote.setText("")
            enableDetails()
            //fab.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun revertDetails() {
        // 0     1       2       3     4    5       6        7    8        9         10      11     12
        // _id|a_structure|b_address|c_city|d_time|e_phone|f_reference|g_flag1|h_flag2|i_lyTurnover|j_cyTurnover|k_group|l_NextVisit
        // tvId.setText(fieldsList[0])
        etStructure.setText(fieldsList[1])
        etAddress.setText(fieldsList[2])
        etCity.setText(fieldsList[3])
        etTime.setText(fieldsList[4])
        etPhone.setText(fieldsList[5])
        etReference.setText(fieldsList[6])
        ckFlag1.isEnabled = fieldsList[7] == "true"
        etFlag2.setText(fieldsList[8])
        etLyTurn.setText(fieldsList[9])
        etCyTurn.setText(fieldsList[10])
        etGroup.setText(fieldsList[11])
        // TODO: etNextVisit.setText(fieldsList[12])
        etDelivery.setText(fieldsList[13])
        etEmail.setText(fieldsList[14])
        etCuPec.setText(fieldsList[15])

        rowNoteNew.etNoteDate.setText(fieldsList[16])
        rowNoteNew.etNote.setText(fieldsList[17])
    }

    private fun enableDetails() {

        toggleEdit(etStructure, true)
        toggleEdit(etAddress, true)
        toggleEdit(etCity, true)
        toggleEdit(etTime, true)
        toggleEdit(etPhone, true)
        toggleEdit(etReference, true)
        ckFlag1.isEnabled = true
        toggleEdit(etFlag2, true)
        toggleEdit(etLyTurn, true)
        toggleEdit(etCyTurn, true)
        toggleEdit(etGroup, true)
        // TODO: toggleEdit(etNextVisit, true)
        toggleEdit(etDelivery, true)
        toggleEdit(etEmail, true)
        toggleEdit(etCuPec, true)

        toggleEdit(rowNoteNew.etNoteDate, true)
        toggleEdit(rowNoteNew.etNote, true)
        ivCalendar.isEnabled = true
        rowNote1.ivDelete.isEnabled = true
        rowNote2.ivDelete.isEnabled = true
        rowNote3.ivDelete.isEnabled = true
        rowNote4.ivDelete.isEnabled = true

        val navView: BottomNavigationView = findViewById(R.id.navigation)
        with(navView) {
            menu.findItem(R.id.action_delete).isEnabled = true
            menu.findItem(R.id.action_restore).isEnabled = true
            menu.findItem(R.id.action_save).isEnabled = true
        }

        // backup data for revert
        fieldsList.clear()
        with(fieldsList, {
            // 0     1       2       3     4    5       6        7    8        9         10      11     12
            // _id|a_structure|b_address|c_city|d_time|e_phone|f_reference|g_flag1|h_flag2|i_lyTurnover|j_cyTurnover|k_group|l_NextVisit
            add(0, tvId.text.toString())
            add(1, etStructure.text.toString())
            add(2, etAddress.text.toString())
            add(3, etCity.text.toString())
            add(4, etTime.text.toString())
            add(5, etPhone.text.toString())
            add(6, etReference.text.toString())
            add(7, ckFlag1.isEnabled.toString())
            add(8, etFlag2.text.toString())
            add(9, etLyTurn.text.toString())
            add(10, etCyTurn.text.toString())
            add(11, etGroup.text.toString())
            add(12, "")

            add(13, etDelivery.text.toString())
            add(14, etEmail.text.toString())
            add(15, etCuPec.text.toString())

            add(16, rowNoteNew.etNoteDate.text.toString())
            add(17, rowNoteNew.etNote.text.toString())
        })


        // Menu operation on ActionBar  --------------------------------
        //  thisMenu.forEach { it.isVisible = true }

        // Menu operation on Detail row --------------------------------
        // operationsMenu.isVisible= true
    }

    private fun toggleEdit(item: EditText, flag: Boolean) {
        with(item, {
            isEnabled = flag
            //  isFocusable = flag
            //  isCursorVisible = flag
        })
    }

    override fun onOptionsItemSelected(menuItem: MenuItem) =
        when (menuItem.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                detailClose("")
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }

    private fun detailClose(message: String) {
        //fab.isVisible = true
        if (message.isNotEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        // IMPORTANT !! use finish() to return to main NOT navigateUpTo
        //              with finish tha main is exactly how and where it was
        // finish()
        navigateUpTo(Intent(this, ListProfileActivity::class.java))
    }

    private fun executeUpdateProfile(setDeleted: String = "") {

        val editedId = tvId.text.toString()
        var idToSave: String
        var newNote: String
        var newDate: String
        idToSave = tvId.text.toString()
        val sb = StringBuilder()
        with(sb) {
            when (setDeleted) {
                "D" -> {
                    idToSave = "--$idToSave" // mark _id as deleted with leading '--'
                }
                "U" -> {
                    idToSave =
                        idToSave.removePrefix("--")  // mark _id as undeleted with leading '--'
                }
                "R" -> {
                    idToSave =
                        idToSave.replace("--", "XX")  // mark _id as undeleted with leading '--'
                }
            }

            if (!idToSave.startsWith("XX")) {
                // A – B – C – D – E – F – G – H – I – J – K – L – M – N – O – P – Q – R – S – T – U – V – W – X – Y – Z
                // 0     1       2       3     4    5       6        7    8        9         10      11     12
                // _id|a_structure|b_address|c_city|d_time|e_phone|f_reference|g_flag1|h_flag2|i_lyTurnover|j_cyTurnover|k_group|l_NextVisit

                append(setFieldPiped(idToSave))        // -
                val etArray = arrayOf<String>(
                    "etStructure",                             // a
                    "etAddress",                               // b
                    "etCity",                                  // c
                    "etTime",                                  // d
                    "etPhone",                                 // e
                    "etReference",                             // f
                    "ckFlag1",                                 // g
                    "etFlag2",                                 // h
                    "etLyTurn",                                // i
                    "etCyTurn",                                // j
                    "etGroup",                                 // k
                    "",                                        // l
                    "etDelivery",                              // m
                    "etEmail",                                 // n
                    "etCuPec",                                 // o
                    "",                                        // p
                    "",                                        // q
                    "",                                        // r
                    "",                                        // s
                    "",                                        // t
                    "",                                        // u
                    "",                                        // v
                    "",                                        // w
                    "",                                        // x
                    "",                                        // y
                    ""
                )
                var text: String
                etArray.forEach {

                    if (it.startsWith("et")) {
                        val etId = resources.getIdentifier(it, "id", packageName)
                        text = (findViewById<EditText>(etId)).text.toString()
                    } else {
                        text = if (it == "ckFlag1")
                            if (ckFlag1.isChecked) "X" else ""
                        else
                            it
                    }

                    append(setFieldPiped(text))
                }

/*
                append(UtilityProfiles.setFieldPiped(idToSave))                                     // -
                append(UtilityProfiles.setFieldPiped(etStructure.text.toString()))                  // a
                append(UtilityProfiles.setFieldPiped(etAddress.text.toString()))                    // b
                append(UtilityProfiles.setFieldPiped(etCity.text.toString()))                       // c
                append(UtilityProfiles.setFieldPiped(etTime.text.toString()))                       // d
                append(UtilityProfiles.setFieldPiped(etPhone.text.toString()))                      // e
                append(UtilityProfiles.setFieldPiped(etReference.text.toString()))                  // f
                append(UtilityProfiles.setFieldPiped((if (ckFlag1.isChecked) "X" else "")))         // g
                append(UtilityProfiles.setFieldPiped(etFlag2.text.toString()))                      // h
                append(UtilityProfiles.setFieldPiped(etLyTurn.text.toString()))                     // i
                append(UtilityProfiles.setFieldPiped(etCyTurn.text.toString()))                     // j
                append(UtilityProfiles.setFieldPiped(etGroup.text.toString().capitalize()))         // k
                append(UtilityProfiles.setFieldPiped(""))   // TODI etNextVisit))                     // l
                append(UtilityProfiles.setFieldPiped(etDelivery.text.toString()))                   // m
                append(UtilityProfiles.setFieldPiped(etEmail.text.toString()))                      // n
                append(UtilityProfiles.setFieldPiped(etCuPec.text.toString()))                      // o
                append(UtilityProfiles.setFieldPiped(""))   //                                      // p
                append(UtilityProfiles.setFieldPiped(""))   //                                      // q
                append(UtilityProfiles.setFieldPiped(""))   //                                      // r
                append(UtilityProfiles.setFieldPiped(""))   //                                      // s
                append(UtilityProfiles.setFieldPiped(""))   //                                      // t
                append(UtilityProfiles.setFieldPiped(""))   //                                      // u
                append(UtilityProfiles.setFieldPiped(""))   //                                      // v
                append(UtilityProfiles.setFieldPiped(""))   //                                      // w
                append(UtilityProfiles.setFieldPiped(""))   //                                      // x
                append(UtilityProfiles.setFieldPiped(""))   //                                      // y
                append(UtilityProfiles.setFieldPiped(""))   //                                      // z
*/
                append("££") //   <--- REMARKS SEPARATOR


                if (!rowNoteNew.etNote.text.toString()
                        .isBlank()
                ) {     // Does note-text contains something?
                    newDate = rowNoteNew.etNoteDate.text.toString()
                    if (newDate.isBlank())   // insert cuttent date if empty
                        newDate =
                            DateFormat.format("yyyy-MM-dd", Calendar.getInstance().time).toString()

                    newNote = newDate.trim() + "°" + rowNoteNew.etNote.text.toString().trim() + "§"
                    append(newNote)
                }

                // --- Manage NOTES
                NOTES.forEach { n ->
                    if (n.date != "==" && n.note.isNotEmpty() && !n.note.contains("ç"))
                        append(n.date + "°" + n.note + "§")
                }
            }

            val dataLine = sb.toString().replace("\n", " ")
            if (ADDING_PROFILE_ITEM)
                PROFILE_FILE_LINES.add(dataLine)
            else {
                replaceLine(editedId, idToSave, dataLine)
            }
        }

        UtilityProfiles.saveProfilesDataFile()
        COUNT_EDITS += 1
        storeToPreferences("dataEdited", "YES ($COUNT_EDITS) - execute backup ASAP")
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

    fun onDeleteClick(item: MenuItem) {
        val builder = AlertDialog.Builder(this@DetailProfileActivity)
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                // Mark as deleted
                executeUpdateProfile("D")
                detailClose("Deleted")
            }
            .setNegativeButton("No") { dialog, _ ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    fun onUnDeleteClick(view: View) {

        if (!tvId.text.startsWith("--"))
            return

        val builder = AlertDialog.Builder(this@DetailProfileActivity)
        builder
            .setTitle("Profile info")
            .setIcon(R.drawable.ic_info_outline_black_24dp)
            .setMessage(
                "This profile is not active because of 'deletion'. " +
                        "You may choose to reactivate it tapping on UN-DELETE or completely remove it tapping on REMOVE FOR EVER. "
            )
            .setCancelable(false)
            .setPositiveButton("Remove forever") { _, _ ->
                // Mark as deleted
                executeUpdateProfile("R")
                detailClose("Removed")
            }
            .setNegativeButton("UN-DELETE") { _, _ ->
                // Mark as deleted
                executeUpdateProfile("U")
                detailClose("Deleted")
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                // Dismiss the dialog
                dialog.dismiss()
            }

        val alert = builder.create()
        alert.show()
    }

    fun onRevertClick(item: MenuItem) {
        revertDetails()
        Toast.makeText(this, "Initial data restored.", Toast.LENGTH_SHORT).show()
    }

    fun onSaveClick(item: MenuItem) {
        executeUpdateProfile("")
        detailClose("Saved")
    }

    fun onEditClick(item: MenuItem) {
        enableDetails()
    }


}


