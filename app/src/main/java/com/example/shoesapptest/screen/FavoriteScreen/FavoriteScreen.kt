package com.example.shoesapptest.screen.FavoriteScreen

import ProductItem
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shoesapptest.R
import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.screen.ForSneakers.SneakersViewModel
import com.example.shoesapptest.screen.HomeScreen.component.BottomBar
import com.google.accompanist.flowlayout.FlowRow
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: SneakersViewModel = koinViewModel()
) {
    val favoritesState by viewModel.favoritesState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Избранное",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_arrow),
                            contentDescription = "Назад",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(64.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.red_heart),
                            contentDescription = "Домой",
                            modifier = Modifier.size(48.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (favoritesState) {
                is NetworkResponseSneakers.Success -> {
                    val favorites = (favoritesState as NetworkResponseSneakers.Success<List<SneakersResponse>>).data

                    if (favorites.isEmpty()) {
                        Text(
                            text = "Нет избранных товаров",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        FavoriteList(
                            favorites = favorites,
                            onItemClick = { id ->  },
                            onFavoriteClick = { },
                            navController = navController
                        )
                    }
                }

                is NetworkResponseSneakers.Error -> {
                    Text(
                        text = "Ошибка загрузки",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                NetworkResponseSneakers.Loading -> {
                    Text(
                        text = "Загрузка...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteList(
    favorites: List<SneakersResponse>,
    onItemClick: (Int) -> Unit,
    onFavoriteClick: () -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        FlowRow(
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 16.dp
        ) {
            favorites.forEach { sneaker ->
                ProductItem(
                    sneaker = sneaker,
                    onItemClick = { onItemClick(sneaker.id) },
                    onFavoriteClick = { onFavoriteClick() },
                    onAddToCart = { },
                    modifier = Modifier
                        .width(160.dp)
                        .aspectRatio(0.85f)
                )
            }
        }
    }
}
