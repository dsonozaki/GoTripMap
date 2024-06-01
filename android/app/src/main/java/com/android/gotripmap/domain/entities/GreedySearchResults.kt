package com.android.gotripmap.domain.entities

data class GreedySearchResults(val resultPoints: MutableList<List<MyPoint>>, val route: List<MyPoint>,val distance: Double)
