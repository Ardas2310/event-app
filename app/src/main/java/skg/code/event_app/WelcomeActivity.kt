package skg.code.event_app

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WelcomeActivity : AppCompatActivity() {

    private lateinit var welcomeContent: View
    private var downX = 0f
    private var isSwiping = false
    private val swipeThreshold = 200f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        welcomeContent = findViewById(R.id.welcome_content)
        welcomeContent.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.rawX
                    isSwiping = true
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isSwiping) {
                        val deltaX = event.rawX - downX

                        // Only allow left swipe (negative direction)
                        if (deltaX < 0) {
                            welcomeContent.translationX = deltaX
                        } else {
                            welcomeContent.translationX = 0f // block right swipe
                        }
                    }
                    true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val deltaX = event.rawX - downX

                    if (deltaX < -swipeThreshold) {
                        // Successful left swipe
                        completeWelcome()
                    } else {
                        // Snap back to original position
                        welcomeContent.animate()
                            .translationX(0f)
                            .setDuration(200)
                            .start()
                    }

                    isSwiping = false
                    welcomeContent.performClick()
                    true
                }

                else -> false
            }
        }


        findViewById<Button>(R.id.continue_button).setOnClickListener {
            getSharedPreferences("prefs", MODE_PRIVATE).edit { putBoolean("hasSeenWelcome", true) }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun completeWelcome() {
        getSharedPreferences("prefs", MODE_PRIVATE).edit { putBoolean("hasSeenWelcome", true) }
        welcomeContent.animate()
            .translationX(-welcomeContent.width.toFloat())
            .setDuration(300)
            .withEndAction {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(0, 0)
                finish()
            }
            .start()
    }

}