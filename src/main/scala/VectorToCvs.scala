/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 23/09/2020, 17:56
 */

package ioCvs

import java.io.{FileInputStream, FileOutputStream}
import scala.collection.mutable.ArrayBuffer
import scala.io.Codec
import scala.reflect.ClassTag

/**
 * Created on 23/09/2020.
 *
 * @author Can Koluman
 * @param separator Char, the cell (column) separator
 * @param encoding Codec, character set encoding
 * @param header Boolean, whether to read / write any column headers
 * @tparam A The cell type of the repeated data. E.g. Double
 * @note
 *
 *
 */
class VectorToCvs[A](override val separator: Char, override implicit val encoding: Codec,
                     override val header: Boolean)(implicit val c: ClassTag[A])
  extends CvsBase[Vector[Vector[A]]](separator, encoding, header) {

  /**
   * Inputs source csv file to data structure A
   *
   * @param source , String, path including filename and extension
   * @param gZip   , Boolean, whether to read  a compressed (gZip) file
   * @return A, populated data structure
   */
  override def csvRead(source: String, gZip: Boolean): Frame[Vector[Vector[A]]] = {
    val hFile = new FileInputStream(source)
    val hInput = inputCharStream(hFile, gZip)
    val data = ArrayBuffer[Vector[A]]()
    var headers: Option[Vector[String]] = None

    var line = hInput.readLine()
    if (header & !line.isEmpty) {
      headers = Some(line.split(separator.toString).toVector)
      line = hInput.readLine()
    }

    while(!line.isEmpty){
      val row = line.split(separator.toString).map(_.asInstanceOf[A])
      data += row.toVector
      line = hInput.readLine()
    }

    Frame(headers, data.toVector)
  }

  /**
   * Outputs source data to the 'target' file in csv format
   * @param source, The data type determined in the implementation
   * @param target, String, path including filename and extension
   * @param gZip, Boolean, whether to compress (gZip) the output file
   * @param append, Boolean, whether to append (true) or to overwrite (false, default)
   * @note It is up to the operator to ensure correct filename extension, use of appropriate separator etc.
   *       Vector[Vector[A]] constitutes
   *       - Outer vector: lines (rows)
   *       - Inner Vector: separator delimited elements (columns)
   */
  override def csvWrite(source: Frame[Vector[Vector[A]]], target: String, gZip: Boolean, append: Boolean = false): Unit = {
    makeDir(getPath(target))
    val hFile = new FileOutputStream(target, append)
    val hOutput = outputCharStream(hFile, gZip)

    var start = 0
    var size = 0
    // cannot inject a header into a file being appended
    if (header & !append) {
      val hdr = source.header.get.mkString(separator.toString)
      size = hdr.length
      while (size > 0) {
        hOutput.write(hdr, start, scala.math.min(CHUNK, size))
        start += CHUNK
        size -= CHUNK
      }
    }

    source.data.foreach(r => {
      val line = r.mkString(separator.toString)
      start = 0
      size = line.length
      while (size > 0) {
        hOutput.write(line, start, scala.math.min(CHUNK, size))
        start += CHUNK
        size -= CHUNK
      }
    })

    hOutput.flush()
    hOutput.close()
    hFile.close()
  }

}
