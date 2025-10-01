package com.jcu.jc428992.booktracker.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcu.jc428992.booktracker.data.local.ReadingStatus
import com.jcu.jc428992.booktracker.data.repository.AppTheme
import com.jcu.jc428992.booktracker.data.repository.BookRepository
import com.jcu.jc428992.booktracker.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountUiState(
    val isLogoutDialogVisible: Boolean = false
)

data class ReadingStats(
    val completedCount: Int = 0,
    val inProgressCount: Int = 0,
    val abandonedCount: Int = 0,
    val willReadCount: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    val appTheme: StateFlow<AppTheme> = settingsRepository.appTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AppTheme.SYSTEM
        )

    val stats: StateFlow<ReadingStats> = bookRepository.getAllBooks()
        .map { books ->
            ReadingStats(
                completedCount = books.count { it.readingStatus == ReadingStatus.READ },
                inProgressCount = books.count { it.readingStatus == ReadingStatus.READING },
                abandonedCount = books.count { it.readingStatus == ReadingStatus.ABANDONED },
                willReadCount = books.count { it.readingStatus == ReadingStatus.WILL_READ },
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ReadingStats()
        )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            settingsRepository.setAppTheme(theme)
        }
    }

    fun onLogOutClicked() {
        _uiState.update { it.copy(isLogoutDialogVisible = true) }
    }

    fun onDismissLogOutDialog() {
        _uiState.update { it.copy(isLogoutDialogVisible = false) }
    }

    fun onConfirmLogOut() {
        onDismissLogOutDialog()
    }
}
