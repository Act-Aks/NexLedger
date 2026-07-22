package com.actaks.nexledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.actaks.nexledger.core.datastore.NexLedgerPreferences
import com.actaks.nexledger.core.designsystem.theme.NexLedgerTheme
import com.actaks.nexledger.core.domain.ThemeMode
import com.actaks.nexledger.core.navigation.NexledgerNavigationRoot
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : ComponentActivity() {

    private var keepSplash = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            keepSplash
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            delay(500.milliseconds) // 0.5 second
            keepSplash = false
        }

        val prefs: NexLedgerPreferences = GlobalContext.get().get()
        setContent {
            val themeMode by prefs.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val useDynamicColors by prefs.useDynamicColors.collectAsState(initial = false)
            NexLedgerTheme(themeMode = themeMode, useDynamicColors = useDynamicColors) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NexledgerNavigationRoot()
                }
            }
        }
    }
}