package com.itce.informants2.profiles


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.itce.informants2.R
import com.itce.informants2.helper.DataProfiles
import com.itce.informants2.helper.DataProfiles.Companion.ARG_ITEM_ID
import com.itce.informants2.helper.DataProfiles.Companion.LIST_PROFILE_ITEM_MAP
import com.itce.informants2.helper.DataProfiles.Companion.NOTES
import com.itce.informants2.helper.Utility.Companion.checkForDigitsOnly
import kotlinx.android.synthetic.main.activity_detail_profile.*
import kotlinx.android.synthetic.main.detail_profile.view.*
import kotlinx.android.synthetic.main.detail_profile_note_row.view.*
import java.time.LocalDate
import java.util.*


class DetailProfileFragment : Fragment() {

    var activityRef = this
    var item: DataProfiles.Companion.ProfileItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { it ->
            if (it.containsKey(ARG_ITEM_ID)) {

                item = LIST_PROFILE_ITEM_MAP[it.getString(ARG_ITEM_ID)]

                val header: String? =
                    if (item?.a_structure == null) "New PROFILE" else item?.a_structure
                activity?.detail_toolbar?.title = header
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.detail_profile, container, false)
        item?.let {
            with(rootView) {
                if (it._id.startsWith("--")) {
                    ivInfoId.isVisible = true
                }
                tvId.text = it._id
                setTextDisabled(et = etStructure, source = it.a_structure)
                setTextDisabled(etAddress, it.b_address)
                setTextDisabled(etCity, it.c_city)
                setTextDisabled(etTime, it.d_time)
                setTextDisabled(etPhone, it.e_phone)
                setTextDisabled(etReference, it.f_reference)
                setTextDisabled(etLyTurn, it.i_lyTurnover)
                setTextDisabled(etCyTurn, it.j_cyTurnover)
                setTextDisabled(etGroup, it.k_group)

                // setTextDisabled(etNextVisit, it.l_NextVisit)


                setTextDisabled(etDelivery, it.m_delivery)
                setTextDisabled(etEmail, it.n_email)
                setTextDisabled(etCuPec, it.o_cupec)

                ckFlag1.isChecked = it.g_flag1.toUpperCase(Locale.ROOT) == "X"
                ckFlag1.isEnabled = false
                setTextDisabled(etFlag2, it.h_flag2)

                NOTES = it.zz_remarks

                // Set Row for new Note
                ivCalendar.isEnabled = false

                var sVisit = ""
                var sNote = ""
                if (checkForDigitsOnly(it.k_group)) {
                    sVisit = LocalDate.ofYearDay(2020, it.k_group.toInt()).toString()
                    sNote = "planned to visit"
                }
                setTextDisabled(et = rowNoteNew.etNoteDate, source = sVisit)
                setTextDisabled(et = rowNoteNew.etNote, source = sNote)


                var note: DataProfiles.Companion.Note
                for (i in 0 until it.zz_remarks.size) {
                    note = it.zz_remarks[i]
                    when (i + 1) {
                        0 -> {

                        }
                        1 -> {
                            with(rowNote1) {
                                isVisible = true
                                etNoteDate.setTextColor(
                                    resources.getColor(
                                        R.color.colorAccent,
                                        null
                                    )
                                )
                                etNote.setTextColor(
                                    resources.getColor(
                                        R.color.colorAccent,
                                        null
                                    )
                                )
                                setTextDisabled(et = etNoteDate, source = note.date)
                                setTextDisabled(et = etNote, source = note.note)
                                ivDelete.setOnClickListener {
                                    setNoteOperation(rowNote1, 1)
                                }
                                ivDelete.isEnabled = false
                            }
                        }
                        2 -> {
                            with(rowNote2) {
                                isVisible = true
                                setTextDisabled(et = etNoteDate, source = note.date)
                                setTextDisabled(et = etNote, source = note.note)
                                ivDelete.setOnClickListener {
                                    setNoteOperation(rowNote2, 2)
                                    ivDelete.isEnabled = false
                                }
                            }
                        }
                        3 -> {
                            with(rowNote3) {
                                isVisible = true
                                setTextDisabled(et = etNoteDate, source = note.date)
                                setTextDisabled(et = etNote, source = note.note)
                                ivDelete.setOnClickListener {
                                    setNoteOperation(rowNote3, 3)
                                    ivDelete.isEnabled = false
                                }
                            }
                        }
                        4 -> {
                            with(rowNote4) {
                                isVisible = true
                                setTextDisabled(et = etNoteDate, source = note.date)
                                setTextDisabled(et = etNote, source = note.note)
                                ivDelete.setOnClickListener {
                                    setNoteOperation(rowNote4, 4)
                                    ivDelete.isEnabled = false
                                }
                            }
                        }
                    }
                }
            }
        }

        //   startTimer()                                       // <--   Suspended here  @@@@@@@

        return rootView
    }


//  private fun startTimer() {                                  // <--   Suspended here  @@@@@@@
//      val updateHandler = Handler()
//      val runnable = Runnable {
//          updateDisplay() // some action(s)
//      }
//      updateHandler.postDelayed(runnable, 3000)
//  }

//  private fun updateDisplay() {                               // <--   Suspended here  @@@@@@@
//      // --- Simulate KEY PRESS  and Show virtual Keyboard
//      rowNoteNew.etNote.requestFocus()

//      //in case need initialize the edittext
//      //rowNoteNew.etNote.setSelection(0)
//      //val inputConnection = BaseInputConnection(rowNoteNew.etNote, true)
//      //inputConnection.sendKeyEvent(
//      //    KeyEvent(
//      //        KeyEvent.ACTION_DOWN,
//      //        KeyEvent.KEYCODE_POUND
//      //    )
//      //)

//      val imm: InputMethodManager = rowNoteNew.etNote.getContext()
//          .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//      imm.showSoftInput(rowNoteNew.etNote, InputMethodManager.SHOW_IMPLICIT)
//  }


    private fun setNoteOperation(view: View, num: Int) {
        val noteN = num - 1
        with(view) {
            if (tag == null) {
                tag = 1
                setBackgroundResource(R.drawable.strike_through)
                NOTES[noteN].note += "ç"
            } else {
                setBackgroundResource(R.color.white)
                NOTES[noteN].note = NOTES[noteN].note.replace("ç", "")
                tag = null
            }
        }
    }

    private fun setTextDisabled(et: EditText, source: String) {
        et.setText(source)
        // et.isFocusable = false
        et.isEnabled = false
        // et.isCursorVisible = false
    }


}
