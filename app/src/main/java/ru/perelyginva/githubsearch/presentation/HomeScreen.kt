package ru.perelyginva.githubsearch.presentation


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import kotlinx.coroutines.delay
import ru.perelyginva.githubsearch.R
import ru.perelyginva.githubsearch.data.model.Owner
import ru.perelyginva.githubsearch.data.model.RepositoryItem
import ru.perelyginva.githubsearch.navigation.MainDestinations
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(navHostController: NavHostController) {

    val viewModel: MainViewModel = hiltViewModel()
    var query by rememberSaveable { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        if (query.length > 2) {
            isSearching = true
            delay(200)
            viewModel.searchRepositories(query)
            isSearching = false
        }
    }

    val pagingData: LazyPagingItems<RepositoryItem> =
        viewModel.getRepositories().collectAsLazyPagingItems()

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            isRefreshing = true
            viewModel.searchRepositories(query)
            isRefreshing = false
        },
        modifier = Modifier.fillMaxSize(),
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    ) {
        val loadState = pagingData.loadState

        when {
            // Если идет загрузка данных, показываем индикатор загрузки
            loadState.refresh is LoadState.Loading -> {
                if (isSearching) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Загрузка...", modifier = Modifier)
                    }
                }
            }

            // Если данные не загружены и список пуст, показываем сообщение о том, что данных нет
            loadState.refresh is LoadState.NotLoading && pagingData.itemCount == 0 -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Данных нет", modifier = Modifier)
                }
            }

            // показываем ошибку
            loadState.refresh is LoadState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ошибка загрузки данных:  ${(loadState.refresh as LoadState.Error).error.localizedMessage}. Попробуйте позже.")
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { newQuery ->
                    query = newQuery
                },
                label = {
                    Text(
                        stringResource(R.string.search_repositories),
                        color = Color.Unspecified
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = Color.Blue,
                    textColor = Color.Black
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            RepositoryList(pagingData, navHostController)
        }
    }
}

@Composable
fun RepositoryList(
    pagingData: LazyPagingItems<RepositoryItem>,
    navController: NavHostController,
) {
    LazyColumn {
        items(pagingData.itemCount) { index ->
            val repository = pagingData[index]
            if (repository != null) {
                RepositoryItem(repository, navController)
            }
        }
    }
}

@Composable
fun RepositoryItem(repository: RepositoryItem, navController: NavHostController) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                val jsonString = Gson().toJson(repository)
                val encode = URLEncoder.encode(jsonString, StandardCharsets.UTF_8.toString())
                val encodedUrl = encode.replace("+", "%20")
                navController.navigate("${MainDestinations.REPOSITORY_DETAILS_ROUTE}/$encodedUrl")
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
                .background(Color.White),
            backgroundColor = Color.White,
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(repository.owner.avatar_url),
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .padding(3.dp)
                            .clip(CircleShape),
                    )

                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier,
                            fontSize = 12.sp,
                            text = stringResource(
                                R.string.repository,
                                repository.name.toString()
                            ),
                            color = Color.Black,
                        )
                        Text(
                            text = stringResource(
                                R.string.owner,
                                repository.owner.login.toString()
                            ),
                            color = Color.Black,
                            style = MaterialTheme.typography.body2
                        )

                        Text(
                            text = stringResource(
                                R.string.updatedAt,
                                repository.updatedAt.toString()
                            ),
                            color = Color.Black,
                        )
                        Row() {
                            Icon(
                                Icons.Default.Star, contentDescription = "Star",
                                modifier = Modifier
                                    .background(Color.Magenta)
                            )
                            Text(
                                text = stringResource(
                                    R.string.stars,
                                    repository.stargazers_count
                                ),
                                color = Color.Black,
                            )
                        }

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Show() {

}