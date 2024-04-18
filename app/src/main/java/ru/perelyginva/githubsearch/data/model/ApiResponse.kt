package ru.perelyginva.githubsearch.data.model

data class ApiResponse<T>(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: T
)
