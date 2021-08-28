/*
 * Copyright (c) 2020. Can Koluman.
 * Authored components released under Apache 2.0.
 * For open source components / libraries, respective origin licenses apply.
 * Last modified 01/10/2020, 18:06
 */

package com.cankoluman.ioCsv.test

import java.nio.charset.StandardCharsets
import com.cankoluman.ioCsv.{Frame, VectorToCsv}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import scala.io.Codec
import java.io.File

/**
 * Created on 25/09/2020.
 *
 * @author Can Koluman
 * @note We only test concrete instances
 *
 *
 */
class VectorToCsvTest extends AnyFunSuite with MockitoSugar with BeforeAndAfterEach with BeforeAndAfterAll{

  val testDir = "./TEST/VectorToCsvTest"
  val file = s"$testDir/data.csv"
  val fileGz = s"$testDir/data.csv.gz"

  /*
  Test Data Structures
   */
  val testHeader: Vector[String] = Vector("A", "B", "C", "D", "E", "F", "G", "H", "I")
  /**
   * Row i, Column j => i.j
   */
  val testData: Vector[Vector[Double]] = Vector(
    Vector(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9),
    Vector(1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9),
    Vector(2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9),
    Vector(3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9),
    Vector(4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9),
    Vector(5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, 5.9)
  )
  /**
   * Transposed Data, Row i, Column j => j.i
   * Reserved for the future
   */
  val testDataT: Vector[Vector[Double]] = Vector(
    Vector(0.1, 1.1, 2.1, 3.1, 4.1, 5.1),
    Vector(0.2, 1.2, 2.2, 3.2, 4.2, 5.2),
    Vector(0.3, 1.3, 2.3, 3.3, 4.3, 5.3),
    Vector(0.4, 1.4, 2.4, 3.4, 4.4, 5.4),
    Vector(0.5, 1.5, 2.5, 3.5, 4.5, 5.5),
    Vector(0.6, 1.6, 2.6, 3.6, 4.6, 5.6),
    Vector(0.7, 1.7, 2.7, 3.7, 4.7, 5.7),
    Vector(0.8, 1.8, 2.8, 3.8, 4.8, 5.8),
    Vector(0.9, 1.9, 2.9, 3.9, 4.9, 5.9)
  )

  val default = new VectorToCsv[Double]()
  val custom = new VectorToCsv[Double](',', Codec(StandardCharsets.ISO_8859_1), false)

  override def beforeEach(): Unit = {
    val hFile = File(file)
    val hFileGz = File(fileGz)
    if (hFile.exists) hFile.delete()
    if (hFileGz.exists) hFileGz.delete()
  }

  override def afterEach(): Unit = beforeEach()

  override def beforeAll(): Unit = {}

  override def afterAll(): Unit = {
    Utility.dirDeleteRecursive(testDir)
  }

  test("Default constructor initializes as expected") {
    val aSep = default.separator
    val eSep = ';'
    assert(aSep === eSep, s"Incorrect Separator: actual $aSep, expected $eSep")

    val aEnc = default.encoding.charSet
    val eEnc = Codec(StandardCharsets.UTF_8)
    assert(aEnc === eEnc.charSet, s"Incorrect Codec: actual $aEnc, expected $eEnc")

    val aHdr = default.header
    val eHdr = true
    assert(aHdr === eHdr, s"Incorrect Header: actual $aHdr, expected $eHdr")
  }

  test("Custom constructor initializes as expected") {
    val aSep = custom.separator
    val eSep = ','
    assert(aSep === eSep, s"Incorrect Separator: actual $aSep, expected $eSep")

    val aEnc = custom.encoding.charSet
    val eEnc = Codec(StandardCharsets.ISO_8859_1)
    assert(aEnc === eEnc.charSet, s"Incorrect Codec: actual $aEnc, expected $eEnc")

    val aHdr = custom.header
    val eHdr = false
    assert(aHdr === eHdr, s"Incorrect Header: actual $aHdr, expected $eHdr")
  }

  test("Not transposed data without header is written / read to CSV file as expected") {
    val expected = Frame(header = None, data = testData)
    custom.csvWrite(expected, file, gZip = false)

    val hFile = File(file)
    assert(hFile.exists, s"File $file could not be written")

    val actual = custom.csvRead(file, gZip = false)
    assert(actual.header.isEmpty, s"Error: $file read, header should be empty but is not")

    val rows = actual.data.length
    val cols = actual.data.head.length

    (0 until rows).foreach(i =>
      (0 until cols).foreach(j => {
        val a = actual.data(i)(j)
        val e = testData(i)(j)
        assert(a === e,
          s"Error: mismatch at row $i, col $j; actual $a expected $e")
      })
    )
  }

  test("Not transposed data with header is written / read to compressed CSV file as expected") {
    val expected = Frame(header = Some(testHeader), data = testData)
    default.csvWrite(expected, fileGz, gZip = true)

    val hFile = File(fileGz)
    assert(hFile.exists, s"File $file could not be written")

    val actual = default.csvRead(fileGz, gZip = true)
    assert(actual.header.isDefined, s"Error: $fileGz read, header should be full but is not")

    val rows = actual.data.length
    val cols = actual.data.head.length

    (0 until rows).foreach(i =>
      (0 until cols).foreach(j => {
        val a = actual.data(i)(j)
        val e = testData(i)(j)
        assert(a === e,
          s"Error: mismatch at row $i, col $j; actual $a expected $e")
      })
    )
  }

  test("Not transposed data with header is written, appended to, and read from compressed CSV file as expected") {
    val write1 = Frame(header = Some(testHeader), data = testData)
    val write2 = Frame(header = Some(testHeader), data = testData.take(5))
    val expected = Frame(header = Some(testHeader), data = testData ++ testData.take(5))
    default.csvWrite(write1, fileGz, gZip = true)
    default.csvWrite(write2, fileGz, gZip = true, append = true)

    val hFileGz = File(fileGz)
    assert(hFileGz.exists, s"File $fileGz could not be written")

    val actual = default.csvRead(fileGz, gZip = true)
    assert(actual.header.isDefined, s"Error: $fileGz read, header should be full but is not")

    val rows = actual.data.length
    val cols = actual.data.head.length

    assert(rows === expected.data.length, s"Got $rows rows, expected ${expected.data.length}")
    assert(cols === expected.data.head.length,
      s"Got $cols cols, expected ${expected.data.head.length}")

    (0 until rows).foreach(i =>
      (0 until cols).foreach(j => {
        val a = actual.data(i)(j)
        val e = expected.data(i)(j)
        assert(a === e,
          s"Error: mismatch at row $i, col $j; actual $a expected $e")
      })
    )

    if (hFileGz.exists) hFileGz.delete()
  }

  test("Empty cell at end is read correctly"){
    val csv = new VectorToCsv[String]()
    val dExpected = Vector[Vector[String]](Vector("1", "2", "3", "4", "5", ""))
    val hExpected = dExpected.indices.map(_.toString).toVector
    val fExpected = Frame(Some(hExpected), dExpected)
    csv.csvWrite(fExpected, file, gZip = false)

    val fActual = csv.csvRead(file, gZip = false)
    val hActual = fActual.header.get
    val dActual = fActual.data

    assert(hActual.length === hExpected.length, s"Header length is mismatched")
    hExpected.indices.foreach(i => {
      val a = hActual(i)
      val e = hExpected(i)
      assert(a === e, s"Error: Header mismatch at $i, actual $a, expected $e")
    })

    assert(dActual.length === 1, s"Error: expected 1 row, got ${dActual.length}")
    assert(dActual.head.length === dExpected.head.length, s"Header length is mismatched")
    dExpected.head.indices.foreach(i => {
      val a = dActual.head(i)
      val e = dExpected.head(i)
      assert(a === e, s"Error: Data mismatch at $i, actual $a, expected $e")
    })

  }
}
