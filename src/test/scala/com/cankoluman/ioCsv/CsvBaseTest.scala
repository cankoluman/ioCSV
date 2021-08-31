/*
 *    Copyright $time.year Can Koluman
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *    
 *    For  open sourced components / libraries, respective origin licenses apply.
 */

package com.cankoluman.ioCsv.test

import java.nio.charset.StandardCharsets
import com.cankoluman.ioCsv.CsvBase
import org.mockito.Mockito.{CALLS_REAL_METHODS, withSettings}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.mockito.MockitoSugar.mock
import scala.io.Codec

/**
 * Created on 25/09/2020.
 *
 * @author Can Koluman
 * @note We only test utility methods
 *
 *
 */
class CsvBaseTest extends AnyFunSuite {

  val mockCsvBase: CsvBase[Double] = mock[CsvBase[Double]](withSettings()
    .defaultAnswer(CALLS_REAL_METHODS)
    .useConstructor(';', Codec(StandardCharsets.UTF_8), false)
  )

  test("Path is returned as expected"){
    val expected = "/some/path/to"
    val fullPath = s"$expected/file.txt"
    val actual = mockCsvBase.getPath(fullPath)

    assert(actual === expected, s"path mismatch: actual $actual, expected $expected")
  }

  // The directory will be created in the current working directory
  // We assume it will remain empty
  test("non-existing directory is created as expected") {
    val dir = "./TEST/this/is/a/test"
    if (Utility.dirExists(dir))
      Utility.dirDeleteRecursive(path = dir)
    val actual = mockCsvBase.makeDir(dir)
    assert(Utility.dirExists(dir))
    assert (actual === dir, s"Error: created $actual, asked for $dir")
    Utility.dirDeleteRecursive(path = dir)
  }

  // The directory will be created in the current working directory
  // We assume it will remain empty
  test("When directory already exists a warning will be issued") {
    val dir = "./TEST/this/is/a/test"
    val expected = s"Warning: $dir already exists."
    if (Utility.dirExists(dir))
      Utility.dirDeleteRecursive(path = dir)
    mockCsvBase.makeDir(dir)

    assert(Utility.dirExists(dir))
    val actual = mockCsvBase.makeDir(dir)
    assert(actual === expected)
    Utility.dirDeleteRecursive(path = dir)
  }

  /**
   * Testing Boolean, Char, String, Byte, Short, Int, Long, Float, Double
   */
  test("Boolean types are converted correctly"){
    val test = Vector("true", "false", "false", "true")
    val expected = Vector(true, false, false, true)
    val actual = mockCsvBase.convert[Boolean](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Char types are converted correctly"){
    val test = Vector("t", "f", "a", "y")
    val expected = Vector('t', 'f', 'a', 'y')
    val actual = mockCsvBase.convert[Char](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("String types are converted correctly"){
    var test = Vector("t", "f", "a", "y")
    var expected = Vector("t", "f", "a", "y")
    var actual = mockCsvBase.convert[String](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))

    test = Vector("this", "is", "a", "test")
    expected = Vector("this", "is", "a", "test")
    actual = mockCsvBase.convert[String](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Byte types are converted correctly"){
    val test = Vector("-128", "10", "28", "127")
    val expected = Vector(-128.toByte, 10.toByte, 28.toByte, 127.toByte)
    val actual = mockCsvBase.convert[Byte](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Short types are converted correctly"){
    val test = Vector("-32768", "10", "28", "32767")
    val expected = Vector[Short](-32768, 10, 28, 32767)
    val actual = mockCsvBase.convert[Short](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Int types are converted correctly"){
    val test = Vector("-2147483648", "10", "28", "2147483647")
    val expected = Vector[Int](-2147483648, 10, 28, 2147483647)
    val actual = mockCsvBase.convert[Int](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Long types are converted correctly"){
    val test = Vector("-9223372036854775808", "10", "28", "9223372036854775807")
    val expected = Vector[Long](-9223372036854775808L, 10, 28, 9223372036854775807L)
    val actual = mockCsvBase.convert[Long](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Float types are converted correctly"){
    val test = Vector("-9223.3720", "10.1", "28.2", "9223.33")
    val expected = Vector[Float](-9223.3720f, 10.1f, 28.2f, 9223.33f)
    val actual = mockCsvBase.convert[Float](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Double types are converted correctly"){
    val test = Vector("-9223.3720", "10.1", "28.2", "9223.33")
    val expected = Vector[Double](-9223.3720d, 10.1d, 28.2d, 9223.33d)
    val actual = mockCsvBase.convert[Double](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  /**
   * Additional type conversions
   */
  test("BigDecimal type is converted correctly"){
    val test = Vector("0.333333333333333333333333")
    val expected = Vector[BigDecimal](BigDecimal("0.333333333333333333333333"))
    val actual = mockCsvBase.convert[BigDecimal](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("BigInt type is converted correctly"){
    val test = Vector("92233720368547758071234567890")
    val expected = Vector[BigInt](BigInt("92233720368547758071234567890"))
    val actual = mockCsvBase.convert[BigInt](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }
}
