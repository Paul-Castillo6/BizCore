package com.example.bizcore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bizcore.screen.AuthenScreen
import com.example.bizcore.screen.LoginScreen
import com.example.bizcore.screen.MainScreen


@Composable
fun Navigation(modifier: Modifier, navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = "login"){
        composable("login") {
            LoginScreen(modifier, navHostController)
        }
        composable("otp"){
            AuthenScreen(modifier, navHostController)
        }
        composable("principal") {
            MainScreen(modifier, rootNavController = navHostController)
        }
    }

}