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

package com.cankoluman.ioCsv

import java.io.{FileInputStream, FileOutputStream}
import java.nio.charset.StandardCharsets
import scala.collection.mutable.ArrayBuffer
import scala.io.Codec
import scala.reflect.ClassTag

/**
 * Created on 23/09/2020.
 * Output to csv of source data in Vector[Vector[A]] format.
 * @author Can Koluman
 * @param separator Char, the cell (column) separator
 * @param encoding Codec, character set encoding
 * @param header Boolean, whether to read / write any column headers
 * @tparam A The cell type of the repeated data. E.g. Double
 *
 */
class VectorToCsv[A <: Any](override val separator: Char, override implicit val encoding: Codec,
                            override val header: Boolean)(implicit val c: ClassTag[A])
  extends CsvBase[Vector[Vector[A]]](separator, encoding, header) {

  /**
   * Constructor with defaults
   * @note The default separator is ';', the default encoding is UTF-8, the default
   *       line ending is '\n', and the header default is true
   */
  def this()(implicit c: ClassTag[A]) = {
    this(';', Codec(StandardCharsets.UTF_8), true)(c)
  }

  /**
   * Reads source csv file into data structure Vector[Vector[A]]
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
    if (line != null && header & !line.isEmpty) {
      headers = Some(line.split(separator.toString, -1).toVector)
      line = hInput.readLine()
    }

    while(line!= null && !line.isEmpty){
      val row = line.split(separator.toString, -1)
      data += convert(row.toVector)
      line = hInput.readLine()
    }

    Frame(headers, data.toVector)
  }

  /**
   * Outputs source data to the 'target' file in csv format.
   * It is up to the operator to ensure correct filename extension,
   * use of appropriate separator etc.
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
      hOutput.newLine()
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
      hOutput.newLine()
    })

    hOutput.flush()
    hOutput.close()
    hFile.close()
  }

}
