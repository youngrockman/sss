package com.example.shoesapptest

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.pypypy.ui.screen.home.HomeScreenHast
import com.example.shoesapp.ui.screen.SignInScreen
import com.example.shoesapp.ui.theme.MatuleTheme
import com.example.shoesapptest.data.local.DataStore
import com.example.shoesapptest.screen.*
import com.example.shoesapptest.screen.FavoriteScreen.FavoriteScreen
import com.example.shoesapptest.screen.ForgotScreen.ForgotPassScreen
import com.example.shoesapptest.screen.ListingScreen.OutdoorScreen
import com.example.shoesapptest.screen.OtrScreen.VerificationScreen
import com.example.shoesapptest.screen.PopularScreen.PopularScreen
import com.example.shoesapptest.screen.RegistrationScreen.RegisterAccountScreen
import com.example.shoesapptest.screen.SlideScreen.SlideScreen
import com.example.shoesapptest.screen.SlideScreen.TitleScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

sealed class Screen(val route: String) {
    object TitleScreen : Screen("title")
    object SlideScreen : Screen("onboarding")
    object SignInScreen : Screen("login")
    object ForgotPasswordScereem : Screen("forgot_password")
    object RegisteratonScreen : Screen("register")
    object Home : Screen("home")
    object Popular : Screen("popular")
    object Favorite : Screen("favorite")
    object Outdoor : Screen("listing")
    object OTPVerification : Screen("otp_verification")
}

class MainActivity : ComponentActivity() {

    @SuppressLint("ComposableDestinationInComposeScope")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dataStore = DataStore(applicationContext)

        setContent {
            MatuleTheme {
                val navController = rememberNavController()


                val token by produceState(initialValue = "") {
                    value = runBlocking { dataStore.tokenFlow.first() }
                }

                val startDestination = if (token.isNotBlank()) {
                    Screen.Home.route
                } else {
                    Screen.SignInScreen.route
                }

                NavHost(
                    navController = navController,
                    startDestination = startDestination
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
                                navController.navigate(Screen.SignInScreen.route) {
                                    popUpTo(Screen.SlideScreen.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.SignInScreen.route) {
                        SignInScreen(
                            onNavigationToRegScreen = {
                                navController.navigate(Screen.RegisteratonScreen.route)
                            },
                            onSignInSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.SignInScreen.route) { inclusive = true }
                                }
                            },
                            navController = navController
                        )
                    }

                    composable(Screen.ForgotPasswordScereem.route) {
                        ForgotPassScreen(
                            onNavigateToSignInScreen = { navController.popBackStack() },
                            navController = navController
                        )
                    }

                    composable(Screen.OTPVerification.route) {
                        VerificationScreen(navController = navController)
                    }

                    composable(Screen.RegisteratonScreen.route) {
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
