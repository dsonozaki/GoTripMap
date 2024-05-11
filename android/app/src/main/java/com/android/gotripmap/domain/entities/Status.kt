package com.android.gotripmap.domain.entities

import android.content.Context

class Status(val resId: Int, vararg val args: String) {
  fun toString(context: Context): String {
    return context.getString(resId,*args)
  }
}
