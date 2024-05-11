package com.android.gotripmap.data.pojo

import com.android.gotripmap.domain.entities.SearchEntry

data class EntriesUpdate(val entry: List<SearchEntry>, private val id: Int, private val hash: String)
