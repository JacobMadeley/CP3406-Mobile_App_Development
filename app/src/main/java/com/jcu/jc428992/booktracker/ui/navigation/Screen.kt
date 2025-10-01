package com.jcu.jc428992.booktracker.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Bookshelf : Screen("bookshelf")
    object Search : Screen("search")
    object Account : Screen("settings")

    object BookshelfDetail : Screen("bookshelf_detail/{bookshelfId}") {
        fun createRoute(bookshelfId: Long) = "bookshelf_detail/$bookshelfId"
    }

    object BookDetail : Screen("book_detail/{bookId}") {
        fun createRoute(bookId: Long) = "book_detail/$bookId"
    }

    object Stats : Screen("stats")
}
