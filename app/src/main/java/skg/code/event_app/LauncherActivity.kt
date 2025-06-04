package skg.code.event_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val hasSeenWelcome = prefs.getBoolean("hasSeenWelcome", false)

        val intent = if (hasSeenWelcome) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, WelcomeActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}