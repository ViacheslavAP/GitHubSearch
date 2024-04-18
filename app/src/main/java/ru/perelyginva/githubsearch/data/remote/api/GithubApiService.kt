package ru.perelyginva.githubsearch.data.remote.api


import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.perelyginva.githubsearch.data.model.ApiResponse
import ru.perelyginva.githubsearch.data.model.RepositoryItem

interface GithubApiService {
    @Headers("Accept: application/vnd.github+json", "Authorization: Bearer $YOUR_PERSONAL_ACCESS_TOKEN")
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ApiResponse<List<RepositoryItem>>

    companion object{
        const val YOUR_PERSONAL_ACCESS_TOKEN = "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"
    }
}