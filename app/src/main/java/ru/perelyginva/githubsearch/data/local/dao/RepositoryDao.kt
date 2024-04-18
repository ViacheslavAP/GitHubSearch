package ru.perelyginva.githubsearch.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.perelyginva.githubsearch.data.model.RepositoryItem

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<RepositoryItem>)

    @Query("SELECT * FROM repositories")
    fun getRepositories(): PagingSource<Int, RepositoryItem>

    @Query("SELECT * FROM repositories WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchRepositories(query: String): PagingSource<Int, RepositoryItem>

    @Query("DELETE FROM repositories")
    suspend fun deleteAllRepositories()

    @Query("SELECT * FROM repositories ORDER BY id ASC LIMIT :pageSize OFFSET :offset")
    fun getRepositoriesByPageQuery(pageSize: Int, offset: Int): List<RepositoryItem>

    fun getRepositoriesByPage(page: Int, pageSize: Int): List<RepositoryItem> {
        // Расчет смещения (offset) в зависимости от номера страницы (page) и размера страницы (pageSize)
        val offset = (page - 1) * pageSize
        // Выполнение запроса и возврат данных
        return getRepositoriesByPageQuery(pageSize, offset)
    }


}