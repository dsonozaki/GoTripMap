package com.android.gotripmap

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.gotripmap.data.network.SearchAPIFactory
import com.android.gotripmap.data.pojo.SearchRequest
import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.Transport
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WordsRecognitionTest {
  @Test
  fun testRecogniseOnePlace() = runTest {
    val searchApiService = SearchAPIFactory.apiService
    val result = searchApiService.getRoutesForEntry(SearchRequest("хочу заехать в поликлинику",Transport.PUBLIC, MyAddress()))
    assert(result.entries[0].destpoint.category=="поликлинику")
    assert(result.entries[0].time==0)
    assert(result.entries.size==1)
  }

  @Test
  fun testRecogniseTwoPlaces() = runTest {
    val searchApiService = SearchAPIFactory.apiService
    val result = searchApiService.getRoutesForEntry(SearchRequest("Я хочу в парк, а потом в церковь",Transport.PUBLIC, MyAddress()))
    assert(result.entries[0].destpoint.category=="парк")
    assert(result.entries[0].time==0)
    assert(result.entries[1].destpoint.category=="церковь")
    assert(result.entries[1].time==0)
    assert(result.entries.size==2)
  }

  @Test
  fun testRecogniseThreePlaces() = runTest {
    val searchApiService = SearchAPIFactory.apiService
    val result = searchApiService.getRoutesForEntry(SearchRequest("Я хочу проехаться на велосипеде по парку, заехать на мост и приехать в торговый центр",Transport.PUBLIC, MyAddress()))
    assert(result.entries[0].destpoint.category=="парку")
    assert(result.entries[0].time==0)
    assert(result.entries[1].destpoint.category=="мост")
    assert(result.entries[1].time==0)
    assert(result.entries[2].destpoint.category=="торговый центр")
    assert(result.entries[2].time==0)
    assert(result.entries.size==3)
  }

  @Test
  fun testRecogniseThreePlacesOneTimeConstraint() = runTest {
    val searchApiService = SearchAPIFactory.apiService
    val result = searchApiService.getRoutesForEntry(SearchRequest("Я хочу за час дойти до парка, потом посетить кафе, после чего зайти в метро",Transport.PUBLIC, MyAddress()))
    assert(result.entries[0].destpoint.category=="парка")
    assert(result.entries[0].time==60)
    assert(result.entries[1].destpoint.category=="кафе")
    assert(result.entries[1].time==0)
    assert(result.entries[2].destpoint.category=="метро")
    assert(result.entries[2].time==0)
    assert(result.entries.size==3)
  }

  @Test
  fun testRecogniseThreePlacesTwoTimeConstraints() = runTest {
    val searchApiService = SearchAPIFactory.apiService
    val result = searchApiService.getRoutesForEntry(SearchRequest("Я хочу за час дойти до парка, через полчаса посетить кафе, после чего зайти в метро",Transport.PUBLIC, MyAddress()))
    assert(result.entries[0].destpoint.category=="парка")
    assert(result.entries[0].time==60)
    assert(result.entries[1].destpoint.category=="кафе")
    assert(result.entries[1].time==30)
    assert(result.entries[2].destpoint.category=="метро")
    assert(result.entries[2].time==0)
    assert(result.entries.size==3)
  }

  @Test
  fun testRecogniseToponym() = runTest {
    val searchApiService = SearchAPIFactory.apiService
    val result = searchApiService.getRoutesForEntry(SearchRequest("Я хочу зайти в Эрмитаж",Transport.PUBLIC, MyAddress()))
    assert(result.entries[0].destpoint.category=="Эрмитаж")
    assert(result.entries[0].time==0)
    assert(result.entries.size==1)
  }

  @Test
  fun testRecogniseToponymAndPlace() = runTest {
    val searchApiService = SearchAPIFactory.apiService
    val result = searchApiService.getRoutesForEntry(SearchRequest("Я хочу зайти в Эрмитаж, а потом в парк",Transport.PUBLIC, MyAddress()))
    assert(result.entries[0].destpoint.category=="Эрмитаж")
    assert(result.entries[0].time==0)
    assert(result.entries[1].destpoint.category=="парк")
    assert(result.entries[1].time==0)
    assert(result.entries.size==2)
  }

  @Test
  fun testRecogniseToponymAndPlaceAndConstraint() = runTest {
    val searchApiService = SearchAPIFactory.apiService
    val result = searchApiService.getRoutesForEntry(SearchRequest("Я хочу дойти до парка за полчаса, потом посетить Эрмитаж",Transport.PUBLIC, MyAddress()))
    assert(result.entries[0].destpoint.category=="парка")
    assert(result.entries[0].time==30)
    assert(result.entries[1].destpoint.category=="Эрмитаж")
    assert(result.entries[1].time==0)
    assert(result.entries.size==2)
  }

  @Test
  fun testRecogniseLongToponym() = runTest {
    val searchApiService = SearchAPIFactory.apiService
    val result = searchApiService.getRoutesForEntry(SearchRequest("Я хочу посетить Летний сад",Transport.PUBLIC, MyAddress()))
    assert(result.entries[0].destpoint.category=="Летний сад")
    assert(result.entries[0].time==0)
    assert(result.entries.size==1)
  }
}
