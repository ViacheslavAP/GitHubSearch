package ru.perelyginva.githubsearch.presentation


import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import ru.perelyginva.githubsearch.data.model.Owner
import ru.perelyginva.githubsearch.data.model.RepositoryItem
import ru.perelyginva.githubsearch.navigation.MainDestinations
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun RepositoryDetailsScreen(repositoryItem: RepositoryItem, navHostController: NavHostController) {

    val followersCountState = remember { mutableStateOf(0) }

    LaunchedEffect(repositoryItem.owner.followers_url) {
        val followersCount = repositoryItem.owner.followers_url?.let { fetchFollowersCount(it) }
        if (followersCount != null) {
            followersCountState.value = followersCount
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(repositoryItem.owner.avatar_url),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Owner: ${repositoryItem.owner.login}",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Bio : ${repositoryItem.owner.bio.toString()}",
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Folowers: ${followersCountState.value}",
            style = MaterialTheme.typography.body1,
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        ClickableText(
            text = AnnotatedString(repositoryItem.html_url.toString()),
            onClick = {
                val encode = URLEncoder.encode(
                    repositoryItem.html_url.toString(),
                    StandardCharsets.UTF_8.toString()
                )
                navHostController.navigate("${MainDestinations.WEB_VIEW_ROUTE}/${encode}")
            },
            style = TextStyle(
                color = Color.Blue,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun ComposeWebView(url: String) {
    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    })
}

suspend fun fetchFollowersCount(followersUrl: String): Int {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(followersUrl)
            .build()
        val response: Response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val jsonResponse = response.body?.string()
            val jsonArray = JSONArray(jsonResponse)
            jsonArray.length()
        } else {
            0
        }
    }
}
