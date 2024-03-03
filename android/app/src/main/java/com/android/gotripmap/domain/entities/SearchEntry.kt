package com.android.gotripmap.domain.entities

data class SearchEntry(val id: Int, val entry: String, val dateTime: String, val transport: Transport, val startPointPlace: String? = null, val endPointPlace: String? = null, val length: String? = null)
