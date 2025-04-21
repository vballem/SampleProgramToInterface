package com.example.unittestsdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.amiiboapp.R
import com.example.unittestsdemo.comments.CommentsScreen
import com.example.unittestsdemo.amiibo.Amiibo
import com.example.unittestsdemo.amiibo.AmiiboIntent
import com.example.unittestsdemo.amiibo.AmiiboState
import com.example.unittestsdemo.amiibo.AmiiboViewModel
import com.example.unittestsdemo.ui.theme.AmiiboAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AmiiboViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmiiboAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "amiibos",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("amiibos") {
                            val state by viewModel.state.collectAsState()

                            LaunchedEffect(Unit) {
                                viewModel.sendIntent(AmiiboIntent.FetchAmiibos("Mario"))
                            }

                            AmiiboScreen(state = state)
                        }
                        composable("comments") {
                            CommentsScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AmiiboScreen(
    state: AmiiboState,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is AmiiboState.Loading -> LoadingState(modifier)
        is AmiiboState.Success -> AmiiboList(state.data, modifier)

        is AmiiboState.Error -> Text("Error: ", modifier = modifier)
    }
}

@Composable
private fun AmiiboList(
    amiibos: List<Amiibo>,
    modifier: Modifier,
) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = modifier,
    ) {
        items(amiibos) { amiibo ->
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                AsyncImage(
                    model = amiibo.image,
                    contentDescription = amiibo.name,
                    modifier = Modifier.size(100.dp),
                    error = painterResource(id = R.drawable.placeholder),
                    placeholder = painterResource(id = R.drawable.placeholder),
                )
                Text(
                    text = amiibo.name,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(androidx.compose.ui.Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center,
    ) {
        Text(
            "Loading...",
            modifier = modifier,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp
            )
        )
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = navController.currentDestination?.route == "amiibos",
            onClick = { navController.navigate("amiibos") },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Amiibos"
                )
            },
            label = { Text("Amiibos") }
        )
        NavigationBarItem(
            selected = navController.currentDestination?.route == "comments",
            onClick = { navController.navigate("comments") },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Comment,
                    contentDescription = "Comments"
                )
            },
            label = { Text("Comments") }
        )
    }
}
