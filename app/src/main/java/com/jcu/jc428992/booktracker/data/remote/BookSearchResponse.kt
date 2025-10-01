package com.jcu.jc428992.booktracker.data.remote

import com.squareup.moshi.Json

data class BookSearchResponse(
    val numFound: Int,
    val start: Int,
    @field:Json(name = "docs")
    val bookDocs: List<BookSearchResultDoc>
)

data class BookSearchResultDoc(
    val title: String,
    @field:Json(name = "author_name")
    val authorName: List<String>?,
    @field:Json(name = "isbn")
    val isbn: List<String>?,
    @field:Json(name = "cover_i")
    val coverId: Int?
)
