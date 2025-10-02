package com.example.pawcarecontrol.Helpers

import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationHelper(
    bottomNavigationView: BottomNavigationView,
    selectedItem: Int
) {
    init {
        bottomNavigationView.selectedItemId = selectedItem

    }
}