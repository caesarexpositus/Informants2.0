package com.itce.informants2.helper

import android.app.Activity
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.preference.PreferenceManager
import com.itce.informants2.R
import kotlinx.android.synthetic.main.body_for_popup_with_edit.view.*

class Utility {
    companion object {

        lateinit var context: Activity

        enum class Answer {
            YES, NO                          // may be also -> , ERROR
        }
        private var choise: Answer = Answer.NO

        fun infoDialog(
            activity: Activity,
            inMessage: String,
            mode: String = "yes"
        ) {

            var message = "ATTENTION!\n\n$inMessage\n\n"
            message += if (mode == "yesno")
                "Tap 'Yes or No'"
            else
                "Tap '$mode'"
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(message)
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton(if (mode == "yes" || mode == "yesno") "Yes" else "") { dialog, _ ->
                    choise = Answer.YES
                    userChoise(Answer.YES)
                    dialog.dismiss()
                }
                .setNegativeButton(if (mode == "no" || mode == "yesno") "No" else "") { dialog, _ ->
                    userChoise(Answer.NO)
                    dialog.dismiss()
                }

            val alert = builder.create()
            alert.show()
        }


        private fun userChoise(choosen: Answer) {
            choise = choosen
            if (choosen === Answer.YES)
                Toast.makeText(context, "YOUR CHOISE YES", Toast.LENGTH_LONG)
                    .show()
            else
                if (choosen === Answer.NO)
                    Toast.makeText(context, "YOUR CHOISE NO", Toast.LENGTH_LONG)
                        .show()
        }


        var EDIT_TEXT_RETURN = ""

        fun popUpWithEdit(
            activity: Activity,
            title: String,
            subTitle: String,
            leftButton: String,
            rightButton: String,
            function: Runnable
        ) {
            EDIT_TEXT_RETURN = ""

            val bodyView: View =
                LayoutInflater.from(context).inflate(R.layout.body_for_popup_with_edit, null)

            with(bodyView)
            {
                tvTitle.text = title
                tvLabel.text = subTitle
                etText.setText("")

            }

            val builder = AlertDialog.Builder(activity)
            with(builder)
            {
                setView(bodyView)
                setPositiveButton(rightButton) { dialog, which ->
                    EDIT_TEXT_RETURN = bodyView.etText.text.toString()
                    function.run()
                    dialog.cancel()

                }
                setNegativeButton(leftButton) { dialog, which ->
                    dialog.cancel()
                }
                show()
            }
        }

        fun setFieldPiped(field: String): String {
            // remove unwanted char and complete with pipe
            val newField = field.replace("\n", " ").replace("|", " ").trim()
            return "$newField|"
        }

        fun showToast(
            activity: Activity,
            message: String,
            stopExecution: Boolean = false,
            mode: String = "yes"
        ): Answer {
            if (message.isNotEmpty() && message.isNotBlank()) {

                if ((message.startsWith("error", ignoreCase = true))
                    or (message.startsWith("info", ignoreCase = true))
                    or (message.startsWith("question", ignoreCase = true))
                    or stopExecution
                )
                    infoDialog(activity, message, mode)
                else
                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
            }

            return choise

        }
        fun checkBackup(): Boolean {

            var backupDate = ""
            val editDate: String
            Data.IS_TO_BACKUP = false
            if (Data.LAST_BACKUP.isNotEmpty())
                backupDate = Data.LAST_BACKUP.substring(0, Data.LAST_BACKUP.indexOf(" "))


            if (Data.LAST_EDIT.isNotEmpty() && Data.COUNT_EDITS > Data.MAX_EDITS) {
                editDate = Data.LAST_EDIT.substring(0, Data.LAST_EDIT.indexOf(" "))
                Data.IS_TO_BACKUP = editDate > backupDate && Data.DATA_EDITED.contains("YES")
            }

            return Data.IS_TO_BACKUP
        }

        fun storeToPreferences(key: String, value: String) {
            val sharedPref: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPref.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun checkForDigitsOnly(string: String): Boolean {
            return if (string.isEmpty() || string.isBlank()) {
                false
            } else {
                string.isDigitsOnly()
            }

        }

    }
}