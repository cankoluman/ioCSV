/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 25/09/2020, 11:00
 */

package ioCvs.test

import ioCvs.CvsBase
import org.mockito.Mockito.{CALLS_REAL_METHODS, when, withSettings}
import org.scalatest.Assertions.convertToEqualizer
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

import scala.reflect.ClassTag

/**
 * Created on 25/09/2020.
 *
 * @author Can Koluman
 * @note We only test utility methods
 *
 *
 */
class CvsBaseTest extends AnyFunSuite with MockitoSugar {

  val mockCvsBase: CvsBase[Double] = mock[CvsBase[Double]](withSettings().defaultAnswer(CALLS_REAL_METHODS))

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
   * Testing Boolean, Char, String, Byte, Short, Int, Long, Float
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

}
