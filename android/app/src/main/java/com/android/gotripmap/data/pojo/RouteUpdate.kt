package com.android.gotripmap.data.pojo

import com.android.gotripmap.data.db.RouteDbModel

data class RouteUpdate(private val routes: List<RouteDbModel>, private val id: Int, private val hash: String)
