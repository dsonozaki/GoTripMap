package com.android.gotripmap

import com.android.gotripmap.presentation.utils.EmailPhoneCorrectChecker
import org.junit.Test

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.random.Random

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */



class UtilsUnitTests {
  private val charPool = ('a'..'z').toList()

  private fun randomStringByKotlinRandom(length: Int) = (1..length)
    .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
    .joinToString("")

  @Test
  fun email_is_correct() {
    for (i in 10..15) {
      val string = randomStringByKotlinRandom(i)
      val digit = Random.nextInt(0, 999)
      val domain = randomStringByKotlinRandom(3)
      val domainPrefix = randomStringByKotlinRandom(i - 3)
      assert(EmailPhoneCorrectChecker("$string$digit@$domainPrefix.$domain").isCorrectEmail())
    }
  }

  @Test
  fun phone_is_correct() {
    assert(EmailPhoneCorrectChecker("+79215772516").isCorrectPhone())
  }

  fun stringToTimestamp(date: String): Long {
    val format = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale("ru"))
    return LocalDateTime.parse(date, format).toEpochSecond(
      ZoneId.systemDefault().rules.getOffset(
        Instant.now()
      )
    )
  }

  @Test
  fun dateTimeConverterIsCorrect() {
    stringToTimestamp("16 апреля 2024 08:58")
  }


}
