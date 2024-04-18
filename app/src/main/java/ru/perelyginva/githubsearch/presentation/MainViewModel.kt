package ru.perelyginva.githubsearch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import ru.perelyginva.githubsearch.data.model.RepositoryItem
import ru.perelyginva.githubsearch.data.remote.repository.GitHubRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val gitHubRepository: GitHubRepository) :
    ViewModel() {

    private val queryFlow = MutableStateFlow("")
    private val repositoryFlow = queryFlow
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isNotEmpty()) {
                gitHubRepository.searchRepositoriesCombined(query).cachedIn(viewModelScope)
            } else {
                flowOf(PagingData.empty())
            }
        }

    fun searchRepositories(query: String) {
        queryFlow.value = query
    }

    fun getRepositories(): Flow<PagingData<RepositoryItem>> {
        return repositoryFlow
    }
}