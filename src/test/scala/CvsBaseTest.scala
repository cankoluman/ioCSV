/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 25/09/2020, 11:00
 */

package ioCvs.test

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets

import ioCvs.CvsBase
import org.mockito.Mockito.{CALLS_REAL_METHODS, when, withSettings}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

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
}
