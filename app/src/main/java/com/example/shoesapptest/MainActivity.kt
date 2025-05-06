package com.example.shoesapptest


import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.core.bundle.Bundle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pypypy.ui.screen.home.HomeScreenHast
import com.example.shoesapp.ui.screen.SignInScreen
import com.example.shoesapp.ui.theme.MatuleTheme
import com.example.shoesapptest.data.local.DataStoreForSlides
import com.example.shoesapptest.screen.FavoriteScreen.FavoriteScreen
import com.example.shoesapptest.screen.ForgotScreen.ForgotPassScreen
import com.example.shoesapptest.screen.ListingScreen.OutdoorScreen
import com.example.shoesapptest.screen.OtrScreen.VerificationScreen
import com.example.shoesapptest.screen.PopularScreen.PopularScreen
import com.example.shoesapptest.screen.RegistrationScreen.RegisterAccountScreen
import com.example.shoesapptest.screen.SlideScreen.SlideScreen
import com.example.shoesapptest.screen.SlideScreen.TitleScreen


sealed class Screen(val route: String) {
    object TitleScreen : Screen("first")
    object SlideScreen : Screen("slide")
    object SignIn : Screen("signin")
    object ForgotPass : Screen("forgotpass")
    object Registration : Screen("registration")
    object Home : Screen("home")
    object Popular : Screen("popular")
    object Favorite : Screen("favorite")
    object Outdoor : Screen("listing")
    object Verification : Screen("verification")
}


class MainActivity : ComponentActivity() {
    @SuppressLint("ComposableDestinationInComposeScope")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            MatuleTheme {
                val navController = rememberNavController()
                val dataStore = DataStoreForSlides(LocalContext.current)




                NavHost(
                    navController = navController,
                    startDestination = Screen.TitleScreen.route
                ) {
                    composable(Screen.TitleScreen.route) {
                        TitleScreen {
                            navController.navigate(Screen.SlideScreen.route) {
                                popUpTo(Screen.TitleScreen.route) { inclusive = true }
                            }
                        }
                    }

                    composable(Screen.SlideScreen.route) {
                        SlideScreen(
                            onNavigateToAuthScreen = {
                                navController.navigate(Screen.SignIn.route) {
                                    popUpTo(Screen.SlideScreen.route) { inclusive = true }
                                }
                            },
                            dataStore = dataStore
                        )
                    }

                    composable(Screen.SignIn.route) {
                        SignInScreen(
                            onNavigationToRegScreen = {
                                navController.navigate(Screen.Registration.route)
                            },
                            onSignInSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.SignIn.route) { inclusive = true }
                                }
                            },
                            navController = navController
                        )
                    }



                    composable(Screen.ForgotPass.route) {
                        ForgotPassScreen(
                            onNavigateToSignInScreen = { navController.popBackStack() },
                            navController = navController
                        )
                    }

                    composable(Screen.Verification.route) {
                        VerificationScreen(navController = navController)
                    }

                    composable(Screen.Registration.route) {
                        RegisterAccountScreen(
                            onNavigationToSigninScreen = {
                                navController.popBackStack()
                            }
                        )
                    }


                    composable(Screen.Home.route) {
                        HomeScreenHast(navController)
                    }

                    composable(Screen.Popular.route) {
                        PopularScreen(navController)
                    }

                    composable(Screen.Favorite.route) {
                        FavoriteScreen(navController)
                    }
                    composable(Screen.Outdoor.route) {
                        OutdoorScreen(navController)

                    }

                }
            }
        }
    }
}