package ru.perelyginva.githubsearch.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.perelyginva.githubsearch.data.local.database.AppDatabase
import ru.perelyginva.githubsearch.data.model.RepositoryItem
import ru.perelyginva.githubsearch.data.paging.GitHubPagingSource
import ru.perelyginva.githubsearch.data.remote.api.GithubApiService
import ru.perelyginva.githubsearch.utils.NetworkUtils
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val apiService: GithubApiService,
    private val appDatabase: AppDatabase,
    private val networkUtils: NetworkUtils
) : GitHubRepository {

    override fun searchRepositories(query: String): Flow<PagingData<RepositoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { GitHubPagingSource(apiService, appDatabase, query) }
        ).flow
    }

    override fun searchRepositoriesOffline(query: String): Flow<PagingData<RepositoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { appDatabase.repositoryDao().searchRepositories(query) }
        ).flow
    }

    override fun searchRepositoriesCombined(query: String): Flow<PagingData<RepositoryItem>> {
        return if (networkUtils.isNetworkAvailable()) {
            searchRepositories(query)
        } else {
            searchRepositoriesOffline(query)
        }
    }
}