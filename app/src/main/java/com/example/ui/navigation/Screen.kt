package com.example.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object WorldMap : Screen("world_map")
    data object Game : Screen("game")
    data object AvatarShop : Screen("avatar_shop")
    data object ParentDashboard : Screen("parent_dashboard")
    data object ProfileEdit : Screen("profile_edit")
}
