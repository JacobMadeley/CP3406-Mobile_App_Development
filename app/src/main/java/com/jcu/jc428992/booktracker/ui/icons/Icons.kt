package com.jcu.jc428992.booktracker.ui.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.room.Delete
import com.jcu.jc428992.booktracker.R

object  AppIcons {
    val Book: Painter
        @Composable
        get() = painterResource(id = R.drawable.book)

    val BookOutline: Painter
        @Composable
        get() = painterResource(id = R.drawable.book_outline)

    val Bookmark: Painter
        @Composable
        get() = painterResource(id = R.drawable.bookmark)

    val BookmarkOutline: Painter
        @Composable
        get() = painterResource(id = R.drawable.bookmark_outline)

    val LibraryAdd: Painter
        @Composable
        get() = painterResource(id = R.drawable.library_add)

    val HomeIcon: Painter
        @Composable
        get() = painterResource(id = R.drawable.local_library)

    val MenuIcon: Painter
        @Composable
        get() = painterResource(id = R.drawable.lists)

    val SearchIcon: Painter
        @Composable
        get() = painterResource(id = R.drawable.search)

    val AccountIcon: Painter
        @Composable
        get() = painterResource(id = R.drawable.account_circle)

    val DeleteIcon: Painter
        @Composable
        get() = painterResource(id = R.drawable.delete)

    val StarIcon: Painter
        @Composable
        get() = painterResource(id = R.drawable.star)

    val HalfStarIcon: Painter
        @Composable
        get() = painterResource(id = R.drawable.star_half)

    val StarIconFilled: Painter
        @Composable
        get() = painterResource(id = R.drawable.star_filled)
}
