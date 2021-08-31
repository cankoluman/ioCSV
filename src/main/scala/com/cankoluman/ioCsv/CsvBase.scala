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

import java.lang.{Class => JavaClass}
import java.io.{BufferedReader, BufferedWriter, File, InputStream, InputStreamReader, OutputStream, OutputStreamWriter}
import java.nio.file.Paths
import java.util.zip.{GZIPInputStream, GZIPOutputStream}
import scala.io.Codec
import scala.reflect.ClassTag
import scala.language.{existentials, implicitConversions}

/**
 * Created on 23/09/2020.
 * This class provides utility methods for file io handling
 * @author Can Koluman
 * @param separator Char, the cell (column) separator
 * @param encoding Codec, character set encoding
 * @param header Boolean, whether to read / write any column headers
 * @tparam A The full type of the repeated data. E.g. Vector[Vector[Double]]
 *
 *
 */
abstract class CsvBase[A](override val separator: Char, override implicit val encoding: Codec,
                          override val header: Boolean) extends TIOCsv[A] {

  /**
   * Additional derived types
   */
  val BIG_DECIMAL: String = classOf[BigDecimal].getTypeName
  val BIG_INT: String = classOf[BigInt].getTypeName
  val STRING: String = classOf[String].getTypeName

  /**
   * Stream chunk size
   */
  val CHUNK: Int = 2048

  /*
  Utility Methods
   */

  /**
   * This is to convert a string into other selected, 'Any' derived types.
   * @param t, JavaClass[T], the runtime class
   * @param in, String, the string to be converted to JavaClass[T]
   * @tparam B, the converted value
   * @throws java.lang.NumberFormatException, the operator must ensure that the input string is legal
   * @return B
   * @note If the type is not found in our conversion list, no conversion is made
   */
  @throws(classOf[NumberFormatException])
  def convert[B <: Any](t: JavaClass[B], in: String): B = {

    (t match {
      // primitive types
      case java.lang.Integer.TYPE   => in.toInt
      case java.lang.Double.TYPE    => in.toDouble
      case java.lang.Float.TYPE     => in.toFloat
      case java.lang.Short.TYPE     => in.toShort
      case java.lang.Long.TYPE      => in.toLong
      case java.lang.Byte.TYPE      => in.toByte
      case java.lang.Character.TYPE => in.head: Char
      case java.lang.Boolean.TYPE   => in.toBoolean
      case _                        =>
        t.getTypeName match {
          // derived types
          case BIG_DECIMAL          => BigDecimal(in)
          case BIG_INT              => BigInt(in)
          // String
          case STRING               => in
          // no conversion
          case _                    => in
        }
    }).asInstanceOf[B]
  }

  /**
   * This is to convert a string vector into other selected, 'Any' derived types.
   * @param in, Vector[String], the string vector to be converted
   * @param c, classTag of the target type, which is passed from the calling class
   * @tparam B, the target Type
   * @return Vector[B], the 'converted' vector
   * @note If the type is not found in our conversion list, no conversion is made
   */
  @throws(classOf[NumberFormatException])
  def convert[B <: Any](in: Vector[String])(implicit c: ClassTag[B]): Vector[B] = {
    val t = c.runtimeClass
    // no need to convert string
    if (t.getTypeName == STRING)
      return in.asInstanceOf[Vector[B]]
    in.map(convert(t, _).asInstanceOf[B])
  }

  /**
   * Utility method for creating a read stream
   * @param hFile, InputStream, the stream to be read from file
   * @param gZip, Boolean, whether to output a compressed stream
   * @return BufferedReader, a (buffered) read stream
   * @note BufferedReader.readline() will break at \n, \r or its combination.
   *       This should pick up all standard line endings
   */
  def inputCharStream(hFile: InputStream, gZip: Boolean): BufferedReader = {
    if (gZip)
      return new BufferedReader(new InputStreamReader(new GZIPInputStream(hFile), encoding.charSet))
    new BufferedReader(new InputStreamReader(hFile, encoding.charSet))
  }

  /**
   * Utility method for creating a write stream
   * @param source, OutputStream, the stream to be written to file
   * @param gZip, Boolean, whether to output a compressed stream
   * @return Writer, a write stream
   */
  def outputCharStream(source: OutputStream, gZip: Boolean): BufferedWriter = {
    if (gZip)
      return new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(source), encoding.charSet))
    new BufferedWriter(new OutputStreamWriter(source, encoding.charSet))
  }

  /**
   * Creates the directory if it does not already exist
   * @param path, String, the directory path
   */
  def makeDir(path: String): String = {
    val target = new File(path)
    try {
      if (target.mkdirs()) {
        println(s"created $path")
        return path
      }
      val msg = s"Warning: $path already exists."
      println(msg)
      msg
    }
    catch {
      case e: Exception => e.getStackTrace.mkString("\n")
    }
  }

  /**
   * Extract path from path and filename
   * @param input, String, filename and path
   * @return String, the path component
   */
  def getPath(input: String): String = {
    val p = Paths.get(input)
    p.getParent.toString
  }

}
