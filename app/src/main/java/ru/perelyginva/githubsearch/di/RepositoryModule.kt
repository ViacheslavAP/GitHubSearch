package ru.perelyginva.githubsearch.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.perelyginva.githubsearch.data.remote.repository.GitHubRepository
import ru.perelyginva.githubsearch.data.remote.repository.GitHubRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindGitHubRepository(impl: GitHubRepositoryImpl): GitHubRepository
}