package com.example.todo.airbnb.presentation.main.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import com.example.todo.airbnb.R
import com.example.todo.airbnb.presentation.main.components.Destinations.searchResult
import com.example.todo.airbnb.presentation.reservation.ReservationViewModel
import com.example.todo.airbnb.presentation.reservation.components.ReservationScreen
import com.example.todo.airbnb.presentation.search.SearchViewModel
import com.example.todo.airbnb.presentation.search.date.components.DateScreen
import com.example.todo.airbnb.presentation.search.detail.components.DetailScreen
import com.example.todo.airbnb.presentation.search.fare.components.FareScreen
import com.example.todo.airbnb.presentation.search.main.SearchScreen
import com.example.todo.airbnb.presentation.search.personnel.components.PersonnelScreen
import com.example.todo.airbnb.presentation.search.searchmap.components.SearchMapScreen
import com.example.todo.airbnb.presentation.search.searchresult.ResultViewModel
import com.example.todo.airbnb.presentation.search.searchresult.components.SearchResultScreen
import com.example.todo.airbnb.presentation.search.serachcondition.SearchConditionScreen
import com.example.todo.airbnb.presentation.wishlist.components.WishListScreen
import com.example.todo.airbnb.ui.theme.Gray
import com.google.accompanist.pager.ExperimentalPagerApi

object Destinations {
    const val search = "search"
    const val calendar = "calendar"
    const val fare = "fare"
    const val personnel = "personnel"
    const val searchResult = "result"
    const val searchMap = "map"
    const val searchCondition = "condition"
    const val detail = "detail"
}

sealed class HomeSections(
    var route: String,
    var icon: Int,
    var title: String,
) {
    object Search : HomeSections("검색", R.drawable.ic_search, "검색")
    object WishList : HomeSections("위시리스트", R.drawable.ic_favorite, "위시리스트")
    object Reservation : HomeSections("내 예약", R.drawable.ic_reservation, "내 예약")
}

@Composable
fun BottomBar(
    navController: NavController,
    bottomBarTabs: List<HomeSections>,
) {
    BottomNavigation(backgroundColor = Gray) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomBarTabs.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = item.title)
                },
                label = { Text(text = item.title) },
                selectedContentColor = Color.Red,
                unselectedContentColor = Color.Black,
                alwaysShowLabel = true,
                selected = (currentRoute == item.route) || selectNavigation(currentRoute, item),
                onClick = {
                    val backStackEntry = navController.previousBackStackEntry
                    val route = backStackEntry?.destination?.route
                    if (item.route == "검색" && route == searchResult) {
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    reservationViewModel: ReservationViewModel,
    searchResultViewModel: ResultViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.search
    ) {
        airbnbNavGraph(
            navController = navController,
            searchViewModel = searchViewModel,
            reservationViewModel = reservationViewModel,
            searchResultViewModel = searchResultViewModel
        )
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
private fun NavGraphBuilder.airbnbNavGraph(
    navController: NavController,
    searchViewModel: SearchViewModel,
    reservationViewModel: ReservationViewModel,
    searchResultViewModel: ResultViewModel,
) {
    navigation(
        route = Destinations.search,
        startDestination = HomeSections.Search.route
    ) {
        composable(HomeSections.Search.route) { SearchScreen(searchViewModel, navController) }
        composable(HomeSections.WishList.route) {
            WishListScreen(
                searchResultViewModel,
                navController
            )
        }
        composable(HomeSections.Reservation.route) {
            ReservationScreen(
                navController,
                reservationViewModel
            )
        }
    }

    composable(route = Destinations.calendar) {
        DateScreen(navController = navController, searchViewModel)
    }
    composable(route = Destinations.fare) {
        FareScreen(
            navController = navController,
            searchViewModel
        )
    }
    composable(route = Destinations.personnel) {
        PersonnelScreen(navController = navController, searchViewModel)
    }
    composable(route = Destinations.searchResult) {
        SearchResultScreen(navController = navController, searchViewModel, searchResultViewModel)
    }
    composable(route = Destinations.searchMap) { SearchMapScreen() }
    composable(route = Destinations.searchCondition) {
        SearchConditionScreen(navController = navController, searchViewModel)
    }
    composable(
        route = "${Destinations.detail}/{id}",
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) { entry ->
        val id = entry.arguments?.getInt("id")
        id?.let {
            DetailScreen(
                navController = navController,
                searchViewModel = searchViewModel,
                searchResultViewModel = searchResultViewModel,
                reservationViewModel = reservationViewModel,
                id = id
            )
        }
    }
}

private fun selectNavigation(
    currentRoute: String?,
    item: HomeSections,
): Boolean {
    return when (item.route) {
        "검색" -> currentRoute == searchResult
        else -> false
    }
}