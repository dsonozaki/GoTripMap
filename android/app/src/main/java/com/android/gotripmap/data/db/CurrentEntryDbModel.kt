package com.android.gotripmap.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



/**
 * Указатель на текущий запрос, который будет отображаться на главном экране приложения
 */
@Entity
data class CurrentEntryDbModel(
  @PrimaryKey val id: Int = 0,
  @ColumnInfo val currentEntry: Int
)
