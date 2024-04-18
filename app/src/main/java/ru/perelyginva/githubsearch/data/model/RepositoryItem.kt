package ru.perelyginva.githubsearch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class RepositoryItem(
    @PrimaryKey val id: Long,
    val name: String? = "",
    val fullName: String? = "",
    val owner: Owner,
    val html_url: String? = "",
    val description: String? = "",
    val language: String? = "",
    val watchersCount: Int,
    val forksCount: Int,
    val topics: List<String>,
    val createdAt: String? = "",
    val updatedAt: String? = "",
    val stargazers_count: Int = 0,
    val bio: String? = "",
    val followers: Int = 0,
    val lastUpdateTime: String? = "",
)

data class Owner(
    val login: String? = "",
    val avatar_url: String? = "",
    val followers_url: String? = "",
    val followers: Int = 0,
    val bio: String?
)


