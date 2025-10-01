package com.jcu.jc428992.booktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jcu.jc428992.booktracker.ui.BookTrackerApp
import com.jcu.jc428992.booktracker.ui.account.AccountViewModel
import com.jcu.jc428992.booktracker.ui.theme.BookTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val accountViewModel: AccountViewModel = hiltViewModel()
            val appTheme by accountViewModel.appTheme.collectAsStateWithLifecycle()
            BookTrackerTheme(appTheme = appTheme) {
                BookTrackerApp(accountViewModel = accountViewModel)
            }
        }
    }
}
