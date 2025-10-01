package com.jcu.jc428992.booktracker.data.remote

import retrofit2.http.Query
import retrofit2.http.GET

interface OpenLibraryApiService {
    @GET("search.json")
    suspend fun searchBooksByTitle(
        @Query("title") query: String
    ): BookSearchResponse

    @GET("search.json")
    suspend fun searchBooksByAuthor(
        @Query("author") query: String
    ): BookSearchResponse

    @GET("search.json")
    suspend fun searchBooksGeneral(
        @Query("q") query: String
    ): BookSearchResponse
}
