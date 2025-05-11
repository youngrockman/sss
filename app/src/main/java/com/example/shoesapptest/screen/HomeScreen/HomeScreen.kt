package com.example.pypypy.ui.screen.home

import ProductItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.shoesapp.ui.theme.MatuleTheme
import com.example.shoesapptest.R
import com.example.shoesapptest.Screen
import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.screen.ForSneakers.SneakersViewModel
import com.example.shoesapptest.screen.HomeScreen.component.BottomBar
import com.example.shoesapptest.screen.HomeScreen.component.TopPanel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenHast(navController: NavHostController) {
    val sneakersViewModel: SneakersViewModel = koinViewModel()

    Scaffold(
        topBar = {
            TopPanel(
                title = "Главная",
                leftImage = painterResource(R.drawable.menu),
                rightImage = painterResource(R.drawable.black_basket),
                textSize = 32
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        HomeScreenContent(
            paddingValues = paddingValues,
            viewModel = sneakersViewModel,
            navController = navController
        )
    }
}

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    viewModel: SneakersViewModel,
    navController: NavHostController
) {
    val sneakersState by viewModel.sneakersState.collectAsState()
    val searchQuery = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchSneakers()
    }

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SearchAndSortRow(searchQuery)
        }

        item {
            CategorySection(navController)
        }

        item {
            when (sneakersState) {
                is NetworkResponseSneakers.Success -> {
                    val sneakers = (sneakersState as NetworkResponseSneakers.Success<List<SneakersResponse>>).data
                    PopularSection(sneakers, navController)
                }
                is NetworkResponseSneakers.Error -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Ошибка загрузки",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                NetworkResponseSneakers.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

        item {
            SalesBanner()
        }
    }
}

@Composable
fun SearchAndSortRow(searchQuery: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            placeholder = { Text("Поиск") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.lupa),
                    contentDescription = "Поиск"
                )
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MatuleTheme.colors.block,
                unfocusedContainerColor = MatuleTheme.colors.background,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(onClick = {  }) {
            Image(
                painter = painterResource(R.drawable.sort),
                contentDescription = "Сортировка",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun CategorySection(navController: NavHostController) {
    Column {
        Text(
            text = "Категории",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listOf("All", "Outdoor", "Volleyball", "FootBall", "Hokkey")) { category ->
                Button(
                    onClick = {
                        if (category == "Outdoor") {
                            navController.navigate(Screen.Outdoor.route)
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = category, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun PopularSection(
    sneakers: List<SneakersResponse>,
    navController: NavHostController
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Популярное", fontSize = 16.sp)
            Text(
                text = "Все",
                color = MatuleTheme.colors.accent,
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Popular.route)
                }
            )
        }
        LazyRow(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sneakers) { sneaker ->
                ProductItem(
                    sneaker = sneaker,
                    onItemClick = { navController.navigate("details/${sneaker.id}") },
                    onFavoriteClick = { },
                    onAddToCart = { },
                    modifier = Modifier.width(160.dp)
                )
            }
        }
    }
}

@Composable
fun SalesBanner() {
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Акции", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text("Все", fontSize = 12.sp, color = MatuleTheme.colors.accent)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Image(
            painter = painterResource(R.drawable.activesale),
            contentDescription = "Акция",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}
