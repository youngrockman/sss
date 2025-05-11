package com.example.shoesapptest.screen.ListingScreen

import ProductItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    categories: List<String> = listOf("Всё", "Outdoor", "Tennis", "Basketball", "Running")
) {
    var selectedCategory by remember { mutableStateOf("Outdoor") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = selectedCategory,
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
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category

                }
            )

            OutdoorContent(
                navController = navController,
                category = selectedCategory
            )
        }
    }
}

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            Box(
                modifier = Modifier
                    .clickable { onCategorySelected(category) }
                    .background(
                        color = if (category == selectedCategory) MatuleTheme.colors.accent
                        else Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = category,
                    color = if (category == selectedCategory) Color.White else Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun OutdoorContent(
    navController: NavController,
    category: String,
    viewModel: SneakersViewModel = koinViewModel()
) {
    val sneakersState by viewModel.sneakersState.collectAsState()
    val favoritesState by viewModel.favoritesState.collectAsState()

    LaunchedEffect(category) {
        viewModel.fetchSneakersByCategory(category)
    }

    when (val state = sneakersState) {
        is NetworkResponseSneakers.Success -> {
            val sneakersWithFavorites = state.data.map { sneaker ->
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
                items(sneakersWithFavorites, key = { it.id }) { sneaker ->
                    ProductItem(
                        sneaker = sneaker,
                        onItemClick = { navController.navigate("details/${sneaker.id}") },
                        onFavoriteClick = {},
                        onAddToCart = {

                        },
                        modifier = Modifier.aspectRatio(0.85f)
                    )
                }
            }
        }
        is NetworkResponseSneakers.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: ${state.errorMessage}")
            }
        }

        NetworkResponseSneakers.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
    }
}
