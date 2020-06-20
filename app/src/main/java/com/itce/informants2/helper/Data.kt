package com.itce.informants2.helper

import java.time.LocalDate

class Data {
    companion
    object {

        lateinit var TODAY_DATE: LocalDate
        var TODAY_year = 0
        var TODAY_month = 0
        var TODAY_day = 0

        var APPLICATION_EXIT = 0
        var COUNT_EDITS = 0
        var DATA_EDITED = ""
        var IS_TO_BACKUP = false
        var LAST_BACKUP = ""
        var LAST_EDIT = ""
        var MAX_EDITS = 0
        var TRY_BACKUP = ""

    }
}
