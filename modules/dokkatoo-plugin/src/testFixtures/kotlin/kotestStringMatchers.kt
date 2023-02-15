package dev.adamko.dokkatoo.utils

import io.kotest.assertions.print.print
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.neverNullMatcher
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot


infix fun String?.shouldContainAll(substrings: Iterable<String>): String? {
  this should containAll(substrings)
  return this
}

infix fun String?.shouldNotContainAll(substrings: Iterable<String>): String? {
  this shouldNot containAll(substrings)
  return this
}

fun String?.shouldContainAll(vararg substrings: String): String? {
  this should containAll(substrings.asList())
  return this
}

fun String?.shouldNotContainAll(vararg substrings: String): String? {
  this shouldNot containAll(substrings.asList())
  return this
}

private fun containAll(substrings: Iterable<String>) =
  neverNullMatcher<String> { value ->
    MatcherResult(
      substrings.all { it in value },
      { "${value.print().value} should include substrings ${substrings.print().value}" },
      { "${value.print().value} should not include substrings ${substrings.print().value}" })
  }
