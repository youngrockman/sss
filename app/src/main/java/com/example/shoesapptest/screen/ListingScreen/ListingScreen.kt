package com.example.shoesapptest.screen.ListingScreen

import ProductItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.shoesapp.ui.theme.MatuleTheme
import com.example.shoesapptest.R
import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.screen.ForSneakers.SneakersViewModel
import com.example.shoesapptest.screen.HomeScreen.component.BottomBar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutdoorScreen(
    navController: NavController,
    viewModel: SneakersViewModel = koinViewModel(),
    initialCategory: String = "Outdoor"
) {
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    val categories = listOf("Всё", "Outdoor", "Tennis", "Basketball", "Running")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = selectedCategory,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.back_arrow),
                            contentDescription = "Назад",
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            CategorySelector(
                categories = categories,
                selected = selectedCategory,
                onSelect = {
                    selectedCategory = it
                    viewModel.fetchSneakersByCategory(it)
                }
            )

            SneakersGrid(
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchSneakersByCategory(initialCategory)
    }
}

@Composable
fun CategorySelector(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            Button(
                onClick = { onSelect(category) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (category == selected) MatuleTheme.colors.accent else Color.LightGray,
                    contentColor = if (category == selected) Color.White else Color.Black
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = category, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SneakersGrid(
    navController: NavController,
    viewModel: SneakersViewModel
) {
    val sneakersState by viewModel.sneakersState.collectAsState()
    val favoritesState by viewModel.favoritesState.collectAsState()

    when (val state = sneakersState) {
        is NetworkResponseSneakers.Success -> {
            val sneakers = state.data.map { sneaker ->
                sneaker.copy(
                    isFavorite = (favoritesState as? NetworkResponseSneakers.Success)?.data?.any { it.id == sneaker.id } == true
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sneakers, key = { it.id }) { sneaker ->
                    ProductItem(
                        sneaker = sneaker,
                        onItemClick = { navController.navigate("details/${sneaker.id}") },
                        onFavoriteClick = { /* TODO */ },
                        onAddToCart = { /* TODO */ },
                        modifier = Modifier.aspectRatio(0.85f)
                    )
                }
            }
        }

        is NetworkResponseSneakers.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка загрузки: ${state.errorMessage}", color = Color.Red)
            }
        }

        NetworkResponseSneakers.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
