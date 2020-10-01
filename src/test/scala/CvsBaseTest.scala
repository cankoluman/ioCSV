/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 25/09/2020, 11:00
 */

package ioCvs.test

import java.nio.charset.StandardCharsets

import ioCvs.CvsBase
import org.mockito.Mockito.{CALLS_REAL_METHODS, withSettings}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import scala.io.Codec

/**
 * Created on 25/09/2020.
 *
 * @author Can Koluman
 * @note We only test utility methods
 *
 *
 */
class CvsBaseTest extends AnyFunSuite with MockitoSugar {

  val mockCvsBase: CvsBase[Double] = mock[CvsBase[Double]](withSettings()
    .defaultAnswer(CALLS_REAL_METHODS)
    .useConstructor(';', new Codec(StandardCharsets.UTF_8), false)
  )

  test("Path is returned as expected"){
    val expected = "/some/path/to"
    val fullPath = s"$expected/file.txt"
    val actual = mockCvsBase.getPath(fullPath)

    assert(actual === expected, s"path mismatch: actual $actual, expected $expected")
  }

  // The directory will be created in the current working directory
  // We assume it will remain empty
  test("non-existing directory is created as expected") {
    val dir = "./TEST/this/is/a/test"
    if (Utility.dirExists(dir))
      Utility.dirDeleteRecursive(path = dir)
    val actual = mockCvsBase.makeDir(dir)
    assert(Utility.dirExists(dir))
    assert (actual === dir, s"Error: created $actual, asked for $dir")
    Utility.dirDeleteRecursive(path = dir)
  }

  // The directory will be created in the current working directory
  // We assume it will remain empty
  test("When directory already exists a warning will be issues") {
    val dir = "./TEST/this/is/a/test"
    val expected = s"Warning: $dir already exists."
    if (Utility.dirExists(dir))
      Utility.dirDeleteRecursive(path = dir)
    mockCvsBase.makeDir(dir)

    assert(Utility.dirExists(dir))
    val actual = mockCvsBase.makeDir(dir)
    assert(actual === expected)
    Utility.dirDeleteRecursive(path = dir)
  }

  /**
   * Testing Boolean, Char, String, Byte, Short, Int, Long, Float, Double
   */
  test("Boolean types are converted correctly"){
    val test = Vector("true", "false", "false", "true")
    val expected = Vector(true, false, false, true)
    val actual = mockCvsBase.convert[Boolean](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Char types are converted correctly"){
    val test = Vector("t", "f", "a", "y")
    val expected = Vector('t', 'f', 'a', 'y')
    val actual = mockCvsBase.convert[Char](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("String types are converted correctly"){
    var test = Vector("t", "f", "a", "y")
    var expected = Vector("t", "f", "a", "y")
    var actual = mockCvsBase.convert[String](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))

    test = Vector("this", "is", "a", "test")
    expected = Vector("this", "is", "a", "test")
    actual = mockCvsBase.convert[String](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Byte types are converted correctly"){
    val test = Vector("-128", "10", "28", "127")
    val expected = Vector(-128.toByte, 10.toByte, 28.toByte, 127.toByte)
    val actual = mockCvsBase.convert[Byte](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Short types are converted correctly"){
    val test = Vector("-32768", "10", "28", "32767")
    val expected = Vector[Short](-32768, 10, 28, 32767)
    val actual = mockCvsBase.convert[Short](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Int types are converted correctly"){
    val test = Vector("-2147483648", "10", "28", "2147483647")
    val expected = Vector[Int](-2147483648, 10, 28, 2147483647)
    val actual = mockCvsBase.convert[Int](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Long types are converted correctly"){
    val test = Vector("-9223372036854775808", "10", "28", "9223372036854775807")
    val expected = Vector[Long](-9223372036854775808L, 10, 28, 9223372036854775807L)
    val actual = mockCvsBase.convert[Long](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Float types are converted correctly"){
    val test = Vector("-9223.3720", "10.1", "28.2", "9223.33")
    val expected = Vector[Float](-9223.3720f, 10.1f, 28.2f, 9223.33f)
    val actual = mockCvsBase.convert[Float](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("Double types are converted correctly"){
    val test = Vector("-9223.3720", "10.1", "28.2", "9223.33")
    val expected = Vector[Double](-9223.3720d, 10.1d, 28.2d, 9223.33d)
    val actual = mockCvsBase.convert[Double](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  /**
   * Additional type conversions
   */
  test("BigDecimal type is converted correctly"){
    val test = Vector("0.333333333333333333333333")
    val expected = Vector[BigDecimal](BigDecimal("0.333333333333333333333333"))
    val actual = mockCvsBase.convert[BigDecimal](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }

  test("BigInt type is converted correctly"){
    val test = Vector("92233720368547758071234567890")
    val expected = Vector[BigInt](BigInt("92233720368547758071234567890"))
    val actual = mockCvsBase.convert[BigInt](test)
    actual.indices.foreach(i => assert(actual(i) === expected(i)))
  }
}
