package com.itce.informants2.helper

import android.app.Activity
import android.content.SharedPreferences
import android.os.Environment
import android.text.format.DateFormat
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.itce.informants2.helper.Data.Companion.DATA_EDITED
import com.itce.informants2.helper.Data.Companion.LAST_BACKUP
import com.itce.informants2.helper.Data.Companion.LAST_EDIT
import com.itce.informants2.helper.DataProfiles.Companion.ARE_PROFILES_LOADED
import com.itce.informants2.helper.DataProfiles.Companion.LAST_PROFILE_ID
import com.itce.informants2.helper.DataProfiles.Companion.LIST_PROFILE_ITEMS
import com.itce.informants2.helper.DataProfiles.Companion.NOTES
import com.itce.informants2.helper.DataProfiles.Companion.Note
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_FILE_LINES
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_PATH_FILE
import com.itce.informants2.helper.DataProfiles.Companion.PROFILE_SORT_MODE
import com.itce.informants2.helper.DataProfiles.Companion.ProfileItem
import com.itce.informants2.helper.DataProfiles.Companion.SETTINGS_SHOW_PROFILE_ACTIVE
import com.itce.informants2.helper.Utility.Companion.checkBackup
import com.itce.informants2.helper.Utility.Companion.checkForDigitsOnly
import com.itce.informants2.helper.Utility.Companion.context
import com.itce.informants2.helper.Utility.Companion.getTodayDateTime
import com.itce.informants2.helper.Utility.Companion.showToast
import java.io.File
import java.util.*
import kotlin.math.abs

class UtilityProfiles {
    companion object {

        private const val PROFILES_FILE = "Profiles.txt"
        private var message = ""

        fun setApplicationSettings(activity: Activity) {
            context = activity
            PROFILE_PATH_FILE = getProfilesPathFile()

            val sharedPref: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            when (sharedPref.getString("sortMode", "ST")) {
                "ST" -> PROFILE_SORT_MODE = DataProfiles.Companion.ProfilesSortBy.STRUCT
                "ID" -> PROFILE_SORT_MODE = DataProfiles.Companion.ProfilesSortBy.ID
                "GR" -> PROFILE_SORT_MODE = DataProfiles.Companion.ProfilesSortBy.GROUP
            }

            SETTINGS_SHOW_PROFILE_ACTIVE = sharedPref.getBoolean("show", false)
            LAST_BACKUP = sharedPref.getString("backup", "2020-01-01 00:00:00").toString()
            DATA_EDITED = sharedPref.getString("dataEdited", "NO").toString()

            getTodayDateTime()
        }


        private fun getProfilesPathFile(): String {

            return (File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "")
                .absolutePath) + "/" + PROFILES_FILE
        }

        fun saveProfilesDataFile() {
            message = try {

                File(PROFILE_PATH_FILE).printWriter().use { out ->
                    PROFILE_FILE_LINES.forEach {
                        out.write(it + System.getProperty("line.separator"))
                    }
                }
                "Saved"
            } catch (exc: Exception) {
                "ERROR: on saveUpdatedProfile" + exc.message
            } finally {
                if (message.isNotEmpty())
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        /*
        interface Command {
            fun execute()

            companion object {
                val NO_OP: Command = object : Command {
                    override fun execute() {}
                }
            }
        }

        class CommandWrapper(private val command: Command) :
            DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
                command.execute()
            }
        }

        private val DISMISS = CommandWrapper(Command.NO_OP)
        fun UtilityProfiles.Companion.createDeletionDialog(

            name: String, deleteCommand: Command?
        ): AlertDialog? {
            val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
            with(builder) {
                this?.setCancelable(true)
                //  this.setIcon(R.drawable.dialog_question)
                this?.setTitle("Are you sure you want to delete \"$name\"?")
                //    this?.setInverseBackgroundForced(true)
                this?.setPositiveButton("Yes", CommandWrapper(deleteCommand!!))
                this?.setNegativeButton("No", DISMISS)
                return this?.create()
            }
        }

         */


        fun buildProfilesFillerData() {
            PROFILE_FILE_LINES.clear()
            File(PROFILE_PATH_FILE).printWriter().use {
                PROFILE_FILE_LINES.add(DataProfiles.FILLER_PROFILE)
                saveProfilesDataFile()
            }
        }

        /*
        fun Companion.buildDataLineFromDataItem(dataItem: DataProfiles.Companion.DataItem): String {

            val retLine: String

            /*
            startnow = android.os.SystemClock.uptimeMillis()

            val retL1 = buildDataLineFromItemStrigBuild(dataItem)

            endnow = android.os.SystemClock.uptimeMillis()
            Log.i("TAG inform", "Execution d_time (1): " + (endnow - startnow) + " ms")


 */
            //    val startnow: Long = android.os.SystemClock.uptimeMillis()
            val retL2 = buildDataLineFromItem_Reflection(dataItem)
            //    val endnow: Long = android.os.SystemClock.uptimeMillis()
            //    Log.i("TAG 'reflection''", "Execution d_time (2): " + (endnow - startnow) + " ms")

            retLine = retL2
            return retLine
        }

         */

/*
        private fun Companion.buildDataLineFromItemStrigBuild(d: DataProfiles.Companion.DataItem): String {
            val sb = StringBuilder()
            with(sb) {
                // A – B – C – D – E – F – G – H – I – J – K – L – M – N – O – P – Q – R – S – T – U – V – W – X – Y – Z
                //  0     1              2       3     4      5         6          7     8        9             10          11        12       13       14
                // _id|a_structure| b_address|c_city|d_time|e_phone|f_reference|g_flag1|h_flag2|i_lyTurnover|j_cyTurnover|k_group|l_NextVisit
                append(setFieldPiped(d._id))
                append(setFieldPiped(d.a_structure))
                append(setFieldPiped(d.b_address))
                append(setFieldPiped(d.c_city))
                append(setFieldPiped(d.d_time))
                append(setFieldPiped(d.e_phone))
                append(setFieldPiped(d.f_reference))
                append(setFieldPiped(d.g_flag1))
                append(setFieldPiped(d.h_flag2))
                append(setFieldPiped(d.i_lyTurnover))
                append(setFieldPiped(d.j_cyTurnover))
                append(setFieldPiped(d.k_group.capitalize()))
                append(setFieldPiped(d.l_NextVisit))
                append(setFieldPiped(d.m_delivery))
                append(setFieldPiped(d.n_email))
                append(setFieldPiped(d.o_cupec))

                NOTES.forEach { n ->
                    if (n.date != "==" && n.note.isNotEmpty() && !n.note.contains("ç"))
                        append((n.date + "°" + n.note + "§").replace("\n", " "))
                }
            }
            return sb.toString()
        }


 */

        fun buildProfileLineFromProfileItem(item: ProfileItem): String {

            // ------------  Build Fields Line from item object  @@@@@@@@@@@@@@@@@@@

            var fieldContent: String
            var dataPart1 = ""
            var dataPart2 = ""
            //  val list_profile: MutableList<String> = ArrayList()
            val fields = item.javaClass.declaredFields
            fields.forEach { field ->
                field.isAccessible = true

                when (field.name) {
                    "zz_remarks" -> {
                        val fieldContent = field[item] as MutableList<Note>
                        var remarks = ""
                        fieldContent.forEach { rem ->
                            if (rem.date != "==" && rem.note.isNotEmpty() && !rem.note.contains(
                                    "ç"
                                )
                            )
                                remarks += (rem.date.replace("|", "-") + "°"
                                        + rem.note.replace("|", "-") + "§")
                        }
                        dataPart2 = remarks
                    }
                    else -> {

                        if (field.name == "zzz_sortKey")
                            fieldContent = ""
                        else {
                            fieldContent = "" + field[item]
                            fieldContent = fieldContent.replace("|", "-")
                        }
                        dataPart1 += "$fieldContent|"
                    }
                }
            }
            return dataPart1 + "££" + dataPart2
        }

/*
                if (field.name != "zz_remarks") {
                    fieldContent = "" + field[item]
                    dataLine += fieldContent + "|"
                } else {
                    val remarks = field[item] as MutableList<Data.Companion.Note>
                    fieldContent = ""
                    remarks.forEach { rem ->
                        if (rem.date != "==" && rem.note.isNotEmpty() && !rem.note.contains("ç"))
                            fieldContent += (rem.date + "°" + rem.note + "§")
                    }
                    dataLine += "££" + fieldContent
                }


            }
            dataLine += "\n"
            return dataLine
            }
            */


        private var currLine: String = ""

        fun Companion.loadProfilesData(activity: Activity, searchRequest: String): Boolean {

            try {
                LIST_PROFILE_ITEMS.clear()
                NOTES.clear()
                LAST_PROFILE_ID = 0

                var searchWhere = ""
                val searchWhat: String

                val aSearch = searchRequest.split("#", ">")
                if (aSearch.size > 1) {
                    searchWhere = aSearch[0]
                    searchWhat = aSearch[1]
                } else
                    searchWhat = aSearch[0]

                if (searchRequest.isNotEmpty()) {
                    DataProfiles.LAST_PROFILE_SEARCH = searchRequest
                }

                // LOAD FILE on PROFILE_DATA_LINES ----------------------------------------------------
                val dataFile = File(PROFILE_PATH_FILE)
                LAST_EDIT =
                    DateFormat.format("yyyy-MM-dd HH:mm:ss", dataFile.lastModified())
                        .toString()

                checkBackup()

                PROFILE_FILE_LINES.clear()
                dataFile.useLines { lines -> PROFILE_FILE_LINES.addAll(lines) } // <--- READ File
                if (PROFILE_FILE_LINES.isEmpty()) {
                    showToast(
                        activity,
                        "ERROR! On Loading DATA,  the FILE is empty! - Unexpected situation!"
                    )
                    ARE_PROFILES_LOADED = false
                    return ARE_PROFILES_LOADED
                } else
                    ARE_PROFILES_LOADED = true

                checkAndNormalizeProfilesData()

                mutableListOf<DataProfiles.Companion.Note>().add(DataProfiles.Companion.Note("==", ""))
                PROFILE_FILE_LINES.forEachIndexed { index, line ->
                    currLine = "dataLine: $line"

                    // ignore headers
                    if (index == 0)
                        return@forEachIndexed
                    // ignore irregular line
                    if (line.count { line.contains("|") } < 26)
                        return@forEachIndexed

                    if (searchRequest.isNotEmpty()) {
                        if (!checkForDigitsOnly(searchWhere)) {
                            // full text search
                            if (!line.contains(searchRequest, true))
                                return@forEachIndexed
                        } else {
                            // search on field by position
                            if (checkForDigitsOnly(searchWhere)) {
                                val i = abs(searchWhere.toInt() - 1)
                                val aLine = line.split("|")
                                if (searchRequest.contains("#")) {
                                    // Search for contains when #
                                    if (!aLine[i].contains(
                                            searchWhat,
                                            ignoreCase = true
                                        )
                                    )
                                        return@forEachIndexed
                                } else {
                                    // Search for greater or equal >=
                                    val normal = normalizeStructure(aLine[i])
                                    val nline =
                                        normal.substring(
                                            0,
                                            searchWhat.length
                                        ) // set same length
                                    val res =
                                        nline.compareTo(searchWhat, ignoreCase = true)
                                    // negative when line < what
                                    if (res < 0)
                                        return@forEachIndexed
                                }
                            }
                        }
                    }


                    val aLineParts = line.split("££")  // part 0 Profile, part 1 Notes
                    val fields = aLineParts[0].split("|") as MutableList<String>

                    if (fields.size < 25) {
                        val count = "" + fields.size
                        message =
                            "ERROR on loadData: Data line irregular: fields count: $count\n$currLine"
                        showToast(activity, message)
                        return@forEachIndexed
                    }

                    if (fields[0].isNotEmpty() && fields[0] != "_id")
                        if ((!SETTINGS_SHOW_PROFILE_ACTIVE) ||
                            (SETTINGS_SHOW_PROFILE_ACTIVE && !fields[0].startsWith("--"))
                        ) {
                            // 0     1       2       3     4    5       6        7    8        9         10      11     12      13
                            // _id|a_structure|b_address|c_city|d_time|e_phone|f_reference|g_flag1|h_flag2|i_lyTurnover|j_cyTurnover|k_group|l_NextVisit|zz_remarks
                            val item = DataProfiles.Companion.ProfileItem(
                                _id = fields[0],
                                a_structure = normalizeStructure(fields[1]),
                                b_address = fields[2],
                                c_city = fields[3],
                                d_time = fields[4],
                                e_phone = fields[5],
                                f_reference = fields[6],
                                g_flag1 = fields[7],
                                h_flag2 = fields[8],
                                i_lyTurnover = fields[9],
                                j_cyTurnover = fields[10],
                                k_group = fields[11],
                                l_NextVisit = fields[12],
                                m_delivery = fields[13],
                                n_email = fields[14],
                                o_cupec = fields[15],
                                p = fields[16],
                                q = fields[17],
                                r = fields[18],
                                s = fields[19],
                                t = fields[20],
                                u = fields[21],
                                v = fields[22],
                                w = fields[23],
                                x = fields[24],
                                y = fields[25],
                                z = fields[26],

                                zz_remarks = mutableListOf(),
                                zzz_sortKey = ""
                            )
                            if (aLineParts[1].isNotEmpty()) {
                                val lNotes = aLineParts[1].split("§")
                                lNotes.sortedByDescending { lNotes[0] }
                                lNotes.forEach { element ->
                                    if (element.isNotEmpty()) {
                                        if (element.contains("°")) {
                                            val elem = element.split("°")
                                            item.zz_remarks.add(
                                                DataProfiles.Companion.Note(
                                                    date = if (elem[0] != "") elem[0] else "date?",
                                                    note = elem[1]
                                                )
                                            )
                                        } else
                                            item.zz_remarks.add(
                                                DataProfiles.Companion.Note(
                                                    date = "date?",
                                                    note = element
                                                )
                                            )
                                    }
                                }
                            } else item.zz_remarks = mutableListOf()

                            // EXPERIMENT to assign values iterating a class by name -----> OK!!!
                            // val property = DataProfiles.Companion.DataItem::class.memberProperties.find { it.name == "h_flag2" }
                            // if (property is KMutableProperty<*>) {
                            //    property.setter.call(item, "value")item.c_city
                            // }
                            // ------------------------------------

                            if (item.c_city.isEmpty())
                                item.c_city = "City?"

                            setProfilesSortMode(item)


                            // -------------------------------------------------------------

                            LIST_PROFILE_ITEMS.add(item)
                            DataProfiles.LIST_PROFILE_ITEM_MAP[item._id] = item
                            LAST_PROFILE_ID = item._id.replace("-", "").toInt()
                        }
                }

                LIST_PROFILE_ITEMS.sortBy { f -> f.zzz_sortKey }
                message = "" // "loaded " + LIST_PROFILE_ITEMS.size.toString()
            } catch (exc: Exception) {
                message = "ERROR on loadData: " + exc.message + "\n" + currLine
            } finally {
                if (message.isNotBlank())
                    showToast(activity, message)
            }

            return ARE_PROFILES_LOADED
        }

        // --------------------------------------------------------------------------
        //  @Suppress("UNCHECKED_CAST")
        //  fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
        //      val property = instance::class.memberProperties
        //          // don't cast here to <Any, R>, it would succeed silently
        //          .first { it.name == propertyName } as KProperty1<Any, *>
        //      // force a invalid cast exception if incorrect type here
        //      return property.get(instance) as R
        //  }

        // -------------------------------

        private fun normalizeStructure(structure: String): String {
            var name = structure
            var normal = ""
            name = name
                .replace("ambulatorio veterinario", "A.V.", ignoreCase = true)
                .replace("associato", "Ass.", ignoreCase = true)
                .replace("amb.vet.", "A.V.", ignoreCase = true)

            if (name.contains("A.V.")) {
                name = name
                    .replace("A.V.", "")
                    .replace("()", "")
                normal = "A.V."
            }
            if (name.contains("Ass.")) {
                name = name
                    .replace("Ass.", "")
                    .replace("( )", "")
                normal = "$normal Ass."
            }
            return if (normal.isNotEmpty())
                name.trim() + " ($normal)"
            else
                structure
        }


        private fun setProfilesSortMode(item: DataProfiles.Companion.ProfileItem) {
            when (PROFILE_SORT_MODE) {
                DataProfiles.Companion.ProfilesSortBy.ID -> {
                    val str = "00000" + item._id.replace("-", "")
                    item.zzz_sortKey = str.substring(str.length - 4)
                }
                DataProfiles.Companion.ProfilesSortBy.STRUCT -> {
                    item.zzz_sortKey = item.a_structure.toLowerCase(Locale.ROOT)
                }
                DataProfiles.Companion.ProfilesSortBy.GROUP -> {
                    item.zzz_sortKey =
                        " " + item.k_group + " " + item.c_city
                }
            }
        }


        private var flagNormalization = 0
        private fun checkAndNormalizeProfilesData() {
            // 0     1       2       3     4    5       6        7    8        9         10      11          12        13      14
            // _id|a_structure|b_address|c_city|d_time|e_phone|f_reference|g_flag1|h_flag2|i_lyTurnover|j_cyTurnover|k_group|l_NextVisit
            val newDataLines = mutableListOf<String>()
            var lParts: Array<String>

            PROFILE_FILE_LINES.forEach { line ->
                try {

                    lParts = line.split("££").toTypedArray()

                    val fields = lParts[0].split("|").toMutableList()

                    var newDataLine = UtilityProfiles.composeProfileLine(fields, lParts.size)


                    //   var stop = ""
                    //   if (newLine.contains("VALLETTE"))
                    //           stop = "?"

                    if (!newDataLine.contains("££"))

                        newDataLine += "££"         // <---  VERY IMPORTANT !!! The NOTE part MUST begin with '££'

                    if (lParts.size == 2)
                        newDataLine += lParts[1]

                    newDataLines.add(newDataLine)
                } catch (exc: Exception) {
                    throw exc
                }
            }
            if (flagNormalization != 0) {
                PROFILE_FILE_LINES = newDataLines
                UtilityProfiles.saveProfilesDataFile()
            }

        }

        fun composeProfileLine(fields: MutableList<String>, lParts: Int): String {
            var line = ""
            var field: String
            var separator: String

            if (lParts < 2) {   // ££ separator not present, then normalize fields

                while (fields.size < 27) {
                    fields.add(fields.size - 1, "")
                    flagNormalization = 1
                }

                fields.add(fields.size - 1, "££")

            }
            // specific Normalization
            fields.forEachIndexed { i: Int, fld ->
                field = if (i == 1) normalizeStructure(fld) else fld        // Structure
                if (field != fld) flagNormalization = 2

                if (i == 11)
                    field = if (fld.trim() == "") "_" else fld              // Group
                if (field != fld) flagNormalization = 3

                separator = "|"

                if (field.contains("££") || fields.size - 1 == i)
                    separator = ""

                line += field + separator
            }

            return line
        }
    }
}









