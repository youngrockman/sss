


package com.example.pypypy.ui.screen.home

import ProductItem
import androidx.compose.material3.IconButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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


@Composable
fun HomeScreenHast(navController: NavHostController) {
    val sneakersViewModel: SneakersViewModel = koinViewModel<SneakersViewModel>()

    Scaffold(
        topBar = {
            TopPanel(title = "Главная",
                leftImage = painterResource(R.drawable.menu),
                rightImage = painterResource(R.drawable.black_basket),
                textSize = 32
            )
        },

        bottomBar = { BottomBar(navController) }
    ) {
            paddingValues ->
        HomeScreenContent(paddingValues = paddingValues,
            sneakersViewModel,
            navController = navController);
    }
}

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    viewModel: SneakersViewModel,
    navController: NavHostController
) {
    val sneakersState by viewModel.sneakersState.collectAsState()
    val message = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchSneakers()
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 22.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp, bottom = 24.dp),
                value = message.value,
                onValueChange = { message.value = it },
                placeholder = { Text("Поиск") },
                leadingIcon = { Icon(
                    painter = painterResource(R.drawable.lupa),
                    contentDescription = "Поиск"
                ) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MatuleTheme.colors.block,
                    unfocusedContainerColor = MatuleTheme.colors.background,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { }) {
                Image(
                    painter = painterResource(R.drawable.sort),
                    contentDescription = "Сортировка",
                    modifier = Modifier.size(48.dp)
                )
            }
        }


        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Категории",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyRow {
                items(listOf("All", "Outdoor", "Volleyball", "FootBall", "Hokkey")) { category ->
                    Button(
                        onClick = {
                            when (category) {
                                "Outdoor" -> navController.navigate(Screen.Outdoor.route)
                            }
                        },
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .height(40.dp),
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


        when (sneakersState) {
            is NetworkResponseSneakers.Success -> {
                val sneakers = (sneakersState as NetworkResponseSneakers.Success<List<SneakersResponse>>).data
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Популярное",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "Все",
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.Popular.route)
                            },
                            fontSize = 12.sp,
                            color = MatuleTheme.colors.accent
                        )
                    }

                    LazyRow(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(sneakers) { sneaker ->
                            ProductItem(
                                sneaker = sneaker,
                                onItemClick = {
                                    navController.navigate("details/${sneaker.id}")
                                },
                                onFavoriteClick = {},
                                onAddToCart = {

                                },
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }
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


        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Акции",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Все",
                    fontSize = 12.sp,
                    color = MatuleTheme.colors.accent
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.activesale),
                    contentDescription = "Акция",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

