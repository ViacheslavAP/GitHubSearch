package ru.perelyginva.githubsearch.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.perelyginva.githubsearch.data.local.database.AppDatabase
import ru.perelyginva.githubsearch.data.model.RepositoryItem
import ru.perelyginva.githubsearch.data.remote.api.GithubApiService

class GitHubPagingSource (
    private val githubApiService: GithubApiService,
    private val appDatabase: AppDatabase,
    private val query: String
) : PagingSource<Int, RepositoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryItem> {
        val page = params.key ?: 1
        return try {
            // Загрузка данных из Api
            val response = githubApiService.searchRepositories(query, page, params.loadSize)
            val item = response.items


            appDatabase.repositoryDao().deleteAllRepositories()

            val limitedItems = response.items.take(15)
            appDatabase.repositoryDao().insertRepositories(limitedItems)

            LoadResult.Page(
                data = item,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (item.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RepositoryItem>): Int? {
        return state.anchorPosition
    }
}