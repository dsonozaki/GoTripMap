package com.android.gotripmap.data.mappers

import com.android.gotripmap.data.db.RoutesAndEntryRelation
import com.android.gotripmap.domain.entities.CurrentEntryRoutes
/**
 * Маппер для текущего запроса и его маршрутов
 */
class RoutesAndEntriesMapper(val routeMapper: RouteMapper, val searchEntryMapper: SearchEntryMapper) {
  fun mapDbToDtModel(routesAndEntryRelation: RoutesAndEntryRelation) =
    CurrentEntryRoutes(
      searchEntryMapper.mapDbToDtModel(routesAndEntryRelation.entry),
      routeMapper.mapDbListToDtList(routesAndEntryRelation.searchEntries)
    )
}
