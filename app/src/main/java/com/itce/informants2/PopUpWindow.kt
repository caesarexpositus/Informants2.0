package com.itce.informants2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_popup_window.*


class PopUpWindow : AppCompatActivity() {

    private var popupTitle = ""
    private var popupLabel = ""
    private var popupText = ""
    private var popupbtnLeft = ""
    private var popupbtnRight = ""
    private var darkStatusBar = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)      // stop any open/close animation

        setContentView(R.layout.activity_popup_window)


        // Get the data passed by the caller of this activity
        val bundle = intent.extras
        popupTitle = bundle?.getString("popupTitle", "Title") ?: ""
        popupLabel = bundle?.getString("popupLabel", "Label") ?: ""
        popupText = bundle?.getString("popupText", "Text") ?: ""
        popupbtnLeft = bundle?.getString("popupBtnLeft", "Button") ?: ""
        popupbtnRight = bundle?.getString("popupBtnRight", "Button") ?: ""
        darkStatusBar = bundle?.getBoolean("darkstatusbar", false) ?: false

        // Set the data

        popup_window_title.text = popupTitle
        popup_window_label.text = popupLabel
        popup_window_text.setText(popupText)
        popup_window_btnLeft.text = popupbtnLeft
        popup_window_btnRight.text = popupbtnRight

        // Set the Status bar appearance for different API levels
        /*
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // If you want dark status bar, set darkStatusBar to true
                if (darkStatusBar) {
                    this.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                this.window.statusBarColor = Color.TRANSPARENT
                setWindowFlag(this, false)
            }
        }


         */

        /*
         // Fade animation for the background of Popup Window
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popup_window_background.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        // Fade animation for the Popup Window
        popup_window_view_with_border.alpha = 0f
        popup_window_view_with_border.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()
        */

        // Close the Popup Window when you press the button


        popup_window_btnRight.setOnClickListener {
            onBtnRightPressed()
        }

        popup_window_btnLeft.setOnClickListener {
            onBtnLeftPressed()
        }

    }


/*
  private fun setWindowFlag(activity: Activity, on: Boolean) {
    val win = activity.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    } else {
        winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
    }
    win.attributes = winParams
}
 */

    fun onBtnRightPressed() {

        val intent = Intent()
        intent.putExtra("note", popup_window_text.text)
        setResult(100, intent)

        //  navigateUpTo(Intent(this, ListProfileActivity::class.java))

        finish()
        overridePendingTransition(0, 0)
    }


    fun onBtnLeftPressed() {
        /*
          // Fade animation for the background of Popup Window when you press the back button
          val alpha = 100 // between 0-255
          val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
          val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
          colorAnimation.duration = 500 // milliseconds
          colorAnimation.addUpdateListener { animator ->
              popup_window_background.setBackgroundColor(
                  animator.animatedValue as Int
              )
          }

          // Fade animation for the Popup Window when you press the back button
          popup_window_view_with_border.animate().alpha(0f).setDuration(500).setInterpolator(
              DecelerateInterpolator()
          ).start()

          // After animation finish, close the Activity
          colorAnimation.addListener(object : AnimatorListenerAdapter() {
              override fun onAnimationEnd(animation: Animator) {

         */
        finish()
        overridePendingTransition(0, 0)
    }
// })
// colorAnimation.start()
}


