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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import ru.perelyginva.githubsearch.data.model.RepositoryItem
import ru.perelyginva.githubsearch.navigation.MainDestinations
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun RepositoryDetailsScreen(repositoryItem: RepositoryItem, navHostController: NavHostController) {
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
            text = "Repository : ${repositoryItem.name.toString()}",
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = repositoryItem.description ?: "",
            style = MaterialTheme.typography.body1,
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = repositoryItem.description ?: "",
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
