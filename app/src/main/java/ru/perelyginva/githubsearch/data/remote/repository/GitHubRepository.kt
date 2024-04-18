package ru.perelyginva.githubsearch.data.remote.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.perelyginva.githubsearch.data.model.Owner
import ru.perelyginva.githubsearch.data.model.RepositoryItem

interface GitHubRepository {

    fun searchRepositories(query: String): Flow<PagingData<RepositoryItem>>
    fun searchRepositoriesOffline(query: String): Flow<PagingData<RepositoryItem>>
    fun searchRepositoriesCombined(query: String): Flow<PagingData<RepositoryItem>>
}