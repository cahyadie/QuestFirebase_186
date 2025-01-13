package com.example.mobile.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobile.ui.pages.DetailView
import com.example.mobile.ui.pages.HomeScreen
import com.example.mobile.ui.pages.InsertMhsView

@Composable
fun PengelolaHalaman (
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier
    ) {
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToItemEntry = {
                    navController.navigate(DestinasiInsert.route)
                },
            )
        }
        composable(DestinasiInsert.route) {
            InsertMhsView(
                onBack = {navController.popBackStack()},
                onNavigate = {
                    navController.navigate(DestinasiInsert.route)
                }
            )
        }
        composable(
            DestinasiDetail.routeWithArg,
            arguments = listOf(
                navArgument(DestinasiDetail.NIM){
                    type = NavType.StringType
                }
            )
        ){
            val nim = it.arguments?.getString(DestinasiDetail.NIM)
            nim?.let {nim ->
                DetailView(
                    onBack = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        navController.navigate("${DestinasiUpdate.route}/$it")
                    },
                    modifier = Modifier,
                    onDeleteClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}