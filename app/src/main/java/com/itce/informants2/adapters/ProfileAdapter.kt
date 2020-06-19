package com.itce.informants2.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.BuildConfig
import com.itce.informants2.MapsActivity
import com.itce.informants2.R
import com.itce.informants2.helper.DataProfiles
import com.itce.informants2.helper.DataProfiles.Companion.ARG_ITEM_ID
import com.itce.informants2.helper.Utility.Companion.infoDialog

import com.itce.informants2.helper.UtilityProfiles
import com.itce.informants2.products.ListProductActivity
import com.itce.informants2.products.ListProductActivityUc
import com.itce.informants2.profiles.DetailProfileActivity
import com.itce.informants2.profiles.ListProfileActivity
import kotlinx.android.synthetic.main.list_profile_content.view.*
import kotlinx.android.synthetic.main.list_profile_flags_row.view.*

class ProfileAdapter(
    private val parentProfileActivity: ListProfileActivity,
    private val dataList: List<DataProfiles.Companion.ProfileItem>
) :
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_profile_content, parent, false)
        return ViewHolder(view)
    }

    private var sDash = " - "

    //@RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        val note = if (item.zz_remarks.size > 0) item.zz_remarks[0] else
            DataProfiles.Companion.Note(" == ", "")
        with(holder)
        {

            if (item._id.startsWith("--"))
                tlProfileView.setBackgroundResource(R.color.colorLightGray)
            else
                tlProfileView.setBackgroundResource(R.color.design_default_color_background)

            tvGroupView.text = item.k_group
            if (DataProfiles.GROUP_ACTIVE) {
                tvGroupView.setBackgroundResource(R.color.colorSecondary)
                tvGroupView.setTextColor(Color.WHITE)
            } else {
                tvGroupView.setBackgroundResource(R.color.white)
                tvGroupView.setTextColor(Color.BLACK)
            }
            idView.text = item._id
            structureView.text = item.a_structure

            sDash = if (item.b_address.isEmpty() or item.c_city.isEmpty())
                "" else " - "

            //sTel = if (item.e_phone.isEmpty())
            //    "" else "     tel "

            addressView.text = (item.b_address.trimEnd() + sDash + item.c_city).trim()
            timeView.text =
                (item.d_time).trim()                           // + sTel + item.e_phone).trim()
            referenceView.text = item.f_reference.trim()

            // remove empty lines
            if (addressView.text.isEmpty()) addressView.isVisible = false
            if (timeView.text.isEmpty()) timeView.isVisible = false
            if (referenceView.text.isEmpty()) referenceView.isVisible = false

            if (item.g_flag1 == "x" || item.g_flag1 == "X")
                flag1View.ivFlag1?.setImageResource(R.drawable.ic_check_box_black_on) else {
                flag1View.ivFlag1?.setImageResource(R.drawable.ic_check_box_black_off)
            }
            flag2View.text = item.h_flag2
            lyTurnView.text = item.i_lyTurnover
            cyTurnView.text = item.j_cyTurnover


            emailView.text = item.n_email
            deliveryView.text = item.m_delivery
            cu_pecView.text = item.o_cupec
            phoneView.text = item.e_phone


            if (item.i_lyTurnover.isEmpty() and item.j_cyTurnover.isEmpty()) {
                turnoverView.isVisible = false
            }
            noteView.text = note.note
            noteDateView.text = note.date

            if (noteView.text.isEmpty()) {
                noteView.isVisible = false
            } else {
                noteView.isVisible = true
                if (noteView.text.contains("!!!")) {
                    noteView.setTextColor(
                        (ContextCompat.getColor(
                            parentProfileActivity,
                            R.color.red
                        ))
                    )
                    // noteDateView.setTextColor((ContextCompat.getColor(parentProfileActivity, R.color.red)))
                }
            }


            btnContextMenuView.setOnClickListener {
                // V is View variable and tv is name of textView
                val popup = PopupMenu(it.context, it)
                with(popup) {
                    inflate(R.menu.context_menu_profile)
                    setOnMenuItemClickListener { item ->


                        when (item.itemId) {
                            R.id.action_detail -> {
                                DataProfiles.PROFILE_SCROLL_POSITION = adapterPosition
                                DataProfiles.ADDING_PROFILE_ITEM = false
                                val intent =
                                    Intent(it.context, DetailProfileActivity::class.java).apply {
                                        putExtra(ARG_ITEM_ID, idView.text.toString())
                                    }
                                it.context.startActivity(intent)

                            }
                            R.id.action_map -> {
                                DataProfiles.PROFILE_MAPS_QUERY = addressView.text as String
                                val intent = Intent(it.context, MapsActivity::class.java).apply {
                                }
                                it.context.startActivity(intent)

                            }
                            R.id.action_quick_note -> {
                                DataProfiles.PROFILE_SCROLL_POSITION = adapterPosition
                                DataProfiles.ADDING_PROFILE_ITEM = false

                                parentProfileActivity.onQuickNoteClicked(
                                    it, idView.text as String, structureView.text as String
                                )
                            }
                            R.id.order -> {
                                DataProfiles.PROFILE_MAPS_QUERY = addressView.text as String
                                if (BuildConfig.DEBUG) {
                                    val intent = Intent(it.context, ListProductActivity::class.java).apply { }
                                    it.context.startActivity(intent)
                                } else {
                                    val intent = Intent(it.context, ListProductActivityUc::class.java).apply { }
                                    it.context.startActivity(intent)
                                }
                            }
                        }
                        true
                    }
                    show()
                }
                true
            }


            //    --------------------- Buttons in single profile

            btnGoDetailView.setOnClickListener { view ->
                DataProfiles.PROFILE_SCROLL_POSITION = adapterPosition
                DataProfiles.ADDING_PROFILE_ITEM = false
                val intent = Intent(view.context, DetailProfileActivity::class.java).apply {
                    putExtra(ARG_ITEM_ID, item._id)
                }
                view.context.startActivity(intent)
            }

            btnGoMapsView.setOnClickListener { view ->
                DataProfiles.PROFILE_MAPS_QUERY = item.b_address + " " + item.c_city
                val intent = Intent(view.context, MapsActivity::class.java).apply {
                }
                view.context.startActivity(intent)
            }


            btnPopNoteView.setOnClickListener { view ->
                DataProfiles.PROFILE_SCROLL_POSITION = adapterPosition
                DataProfiles.ADDING_PROFILE_ITEM = false

                parentProfileActivity.onQuickNoteClicked(
                    view, item._id, item.a_structure
                )
            }


            /*  ---------------------  QUICK NOTE by Activity
                  val intent = Intent(it.context, PopUpWindow::class.java)
                intent.putExtra(
                    "popupTitle", "Quick NOTE"
                )
                intent.putExtra(
                    "popupLabel", structureView.text.toString() +
                            " #" + item._id
                )
                intent.putExtra("popupText", "")
                intent.putExtra("popupBtnLeft", "Cancel")
                intent.putExtra("popupBtnRight", "Save")
                intent.putExtra("darkstatusbar", false)
                //   it.context.startActivity(intent)

               PROFILE_SCROLL_POSITION = adapterPosition
               ADDING_PROFILE_ITEM = false
               PROFILE_ITEM_SELECTED = item
                  val POPUP_NOTE_TO_DO = 100
                 startActivityForResult(parentProfileActivity, intent, POPUP_NOTE_TO_DO, null)

            }
            */


            btnExpandView.setOnClickListener { v ->
                tlExpandCollapse.visibility = View.VISIBLE
                btnExpandView.visibility = View.GONE
                btnCollapseView.visibility = View.VISIBLE
            }

            btnCollapseView.setOnClickListener { v ->
                tlExpandCollapse.visibility = View.GONE
                btnExpandView.visibility = View.VISIBLE
                btnCollapseView.visibility = View.GONE
            }

            llGroupView.setOnClickListener {
                if (DataProfiles.GROUP_ACTIVE) {
                    if (tvGroupView.text != DataProfiles.CURRENT_GROUP) {
                        tvGroupView.text =
                            DataProfiles.CURRENT_GROUP
                        item.k_group =
                            DataProfiles.CURRENT_GROUP
                    } else {
                        tvGroupView.text = "_"
                        item.k_group = "_"
                    }
                    this@ProfileAdapter.notifyItemChanged(position)

                    // replace line of this _id in PROFILE_FILE_LINES
                    val dataLine = UtilityProfiles.buildProfileLineFromProfileItem(item)
                    val index = DataProfiles.PROFILE_FILE_LINES.indexOfFirst { ln ->
                        ln.startsWith(item._id + "|")
                    }
                    if (index >= 0) {
                        DataProfiles.PROFILE_FILE_LINES[index] = dataLine
                        //    UtilityProfiles.saveDataFile(parentProfileActivity)
                    } else {
                        infoDialog(parentProfileActivity,"ID NOT FOUND")

                    }
                }
            }

            /* ---- NOT for  full item click
            with(itemView) { tag = item
                             setOnClickListener(onClickListener)
            }
            */
        }

    }


    override fun getItemCount() = dataList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tlProfileView: TableLayout = view.tlProfile
        val llGroupView: LinearLayout = view.llGroup
        val tvGroupView: TextView = view.tvGroup
        val idView: TextView = view.tvId
        val structureView: TextView = view.tvStructure
        val addressView: TextView = view.tvAddress
        val timeView: TextView = view.tvTime
        val referenceView: TextView = view.tvReference
        val flag1View: ImageView = view.ivFlag1
        val flag2View: TextView = view.tvFlag2
        val noteDateView: TextView = view.tvNoteDate
        val noteView: TextView = view.tvNote
        val turnoverView: TextView = view.tvTurnover
        val lyTurnView: TextView = view.tvLyTurn
        val cyTurnView: TextView = view.tvCyTurn
        val btnGoDetailView: ImageView = view.imgGoDetails
        val btnGoMapsView: ImageView = view.imgMap
        val btnPopNoteView: ImageView = view.imgNotes
        val btnExpandView: ImageView = view.imgExpand
        val btnCollapseView: ImageView = view.imgCollapse
        val btnContextMenuView: ImageView = view.imgContextMenu
        val tlExpandCollapse: LinearLayout = view.tlExpand
        val phoneView: TextView = view.tvPhone
        val emailView: TextView = view.tvEmail
        val deliveryView: TextView = view.tvDelivery
        val cu_pecView: TextView = view.tvCuPec
    }
}