package com.itce.informants2.helper

class DataProfiles {

    companion
    object {

        /*
            data in file Anagrafica.txt in Documents
            _id|a_structure|b_address|c_city|d_time|e_phone|f_reference|g_flag1|h_flag2|i_lyTurnover|j_cyTurnover|k_group|l_NextVisit
        */
        // A – B – C – D – E – F – G – H – I – J – K – L – M – N – O – P – Q – R – S – T – U – V – W – X – Y – Z
        data class ProfileItem(                     //  NAME          INDEX  PREFIX
            var _id: String,                        //  _id,            0       -
            var a_structure: String,                //  a_structure,    1       a
            var b_address: String,                  //  b_address,      2       b
            var c_city: String,                     //  c_city,         3       c
            var d_time: String,                     //  d_time,         4       d
            var e_phone: String,                    //  e_phone,        5       e
            var f_reference: String,                //  f_reference,    6       f
            var g_flag1: String,                    //  g_flag1,        7       g
            var h_flag2: String,                    //  h_flag2,        8       h
            var i_lyTurnover: String,               //  i_lyTurnover,   9       i
            var j_cyTurnover: String,               //  j_cyTurnover,   10      j
            var k_group: String,                    //  k_group,        11      k
            var l_NextVisit: String,                //  l_NextVisit,    12      l
            var m_delivery: String,                 //                  13      m
            var n_email: String,                    //                  14      n
            var o_cupec: String,                    //                  15      o
            var p: String,                          //                  16      p
            var q: String,                          //                  17      q
            var r: String,                          //                  18      r
            var s: String,                          //                  19      s
            var t: String,                          //                  20      t
            var u: String,                          //                  21      u
            var v: String,                          //                  22      v
            var w: String,                          //                  23      w
            var x: String,                          //                  24      x
            var y: String,                          //                  25      y
            var z: String,                          //                  26      z
            var zz_remarks: MutableList<Note>,      //  zz_remarks      27      zz
            var zzz_sortKey: String                 //  zzz_sortKey     28      zzz
        ) {
            override fun toString(): String = a_structure
        }

        data class Note(
            var date: String,
            var note: String
        )

        data class Sentence(
            var speech: String,
            var command: String
        )


        val PROFILE_VOICE_COMMANDS: MutableList<Sentence> = mutableListOf(
            Sentence(speech = "nuovo profilo", command = "addNew"),
            Sentence(speech = "ordina per _id", command = "sortId"),
            Sentence(speech = "ordina per codice", command = "sortId"),
            Sentence(speech = "ordina per struttura", command = "sortStruct"),
            Sentence(speech = "ordina per nome", command = "sortStruct"),
            Sentence(speech = "ordina per gruppo", command = "sortGroup"),
            Sentence(speech = "apri", command = "detail_profile?"),
            Sentence(speech = "apri dettaglio", command = "detail_profile?"),
            Sentence(speech = "apri profilo", command = "detail_profile?"),
            Sentence(speech = "apri percorso", command = "map?"),
            Sentence(speech = "dettaglio", command = "detail_profile?"),
            Sentence(speech = "profilo", command = "detail_profile?"),
            Sentence(speech = "percorso", command = "map?"),
            Sentence(speech = "indicazioni", command = "map?"),
            Sentence(speech = "mappa", command = "map?"),

            Sentence(speech = "lista tutto", command = "listAll"),
            Sentence(speech = "lista tutti", command = "listAll"),
            Sentence(speech = "lista attivi", command = "listActive"),

            Sentence(speech = "annulla ricerca", command = "cancelSearch"),
            Sentence(speech = "cerca", command = "search-0?"),
            Sentence(speech = "cerca tutto", command = "search-0?"),
            Sentence(speech = "cerca struttura", command = "search-2?"),
            Sentence(speech = "cerca nome", command = "search-2?"),
            Sentence(speech = "cerca indirizzo", command = "search-3?"),
            Sentence(speech = "cerca citta", command = "search-4?"),
            Sentence(speech = "cerca orario", command = "search-5?"),

            Sentence(speech = "cerca da nome", command = "searchfrom-2?")
        )
        const val PROFILE_ASK_NAME = "dimmi il nome o il codice del profilo"
        const val PROFILE_ASK_SEARCH = "dimm il nome o il codice da cercare"
        const val SPEECH_COMMAND_CODE = 10
        const val SPEECH_ARGUMENT_CODE = 20
        const val POPUP_NOTE_CODE = 100

        const val FILLER_PROFILE =
            // _id|a_structure|b_address|c_city|d_time|e_phone|f_reference|g_flag1|h_flag2|i_lyTurnover|j_cyTurnover|k_group|l_NextVisit
            "1|demo_structure|demo_address|demo_city|08:00-20:00|+393356789876|demo_reference|X|A+++|25.000|9.500|2020-03-15|||||||||||||||££"

        const val ARG_ITEM_ID = "item_id"

        enum class ProfilesSortBy { ID, STRUCT, GROUP }

        lateinit var PROFILE_ITEM_SELECTED: ProfileItem
        val LIST_PROFILE_ITEMS: MutableList<ProfileItem> = ArrayList()
        val LIST_PROFILE_ITEM_MAP: MutableMap<String, ProfileItem> = HashMap()

        var PROFILE_PATH_FILE = ""

        var ADDING_PROFILE_ITEM = false

        var ARE_PROFILES_LOADED: Boolean = false

        var CURRENT_GROUP = "A"

        var GROUP_ACTIVE = false
        var IS_PROFILE_FIRST_TIME: Boolean = true


        var LAST_PROFILE_ID = 0
        var LAST_PROFILE_SEARCH = ""

        var NOTES: MutableList<Note> = ArrayList()
        var PROFILE_FILE_LINES = mutableListOf<String>()
        var PROFILE_MAPS_QUERY = ""
        var PROFILE_SCROLL_POSITION = 0
        var PROFILE_SORT_MODE = ProfilesSortBy.STRUCT
        var SETTINGS_SHOW_PROFILE_ACTIVE = true

    }

}