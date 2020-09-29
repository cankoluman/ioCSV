/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 23/09/2020, 18:11
 */

package ioCvs

import java.io.{BufferedReader, BufferedWriter, File, InputStream, InputStreamReader, OutputStream, OutputStreamWriter}
import java.nio.file.Paths
import java.util.zip.{GZIPInputStream, GZIPOutputStream}
import scala.io.Codec
import scala.reflect.ClassTag

/**
 * Created on 23/09/2020.
 *
 * @author Can Koluman
 * @param separator Char, the cell (column) separator
 * @param encoding Codec, character set encoding
 * @param header Boolean, whether to read / write any column headers
 * @tparam A The full type of the repeated data. E.g. Vector[Vector[Double]]
 * @note This class provides default constructors, and utility methods for file io handling
 *
 *
 */
abstract class CvsBase[A](override val separator: Char, override implicit val encoding: Codec,
                          override val header: Boolean) extends TIOCvs[A] {

  /*
  Utility Methods
   */

  /**
   * @param in, Vector[String], the string vector to be cast
   * @param c, classTag of the target type, which is passed from the calling class
   * @tparam B, the target Type
   * @return Vector[B], the 'converted' vector
   * @note This is to convert a string vector in to other primitive AnyVal types.
   *       If the type is not found in our conversion list, an exception is triggered
   */
  @throws(classOf[NumberFormatException])
  def convert[B <: Any](in: Vector[String])(implicit c: ClassTag[B]): Vector[B] = {
    val t = c.runtimeClass.getTypeName

    // no need to convert string
    if (t == "java.lang.String")
      return in.asInstanceOf[Vector[B]]

    in.map(elt => (t match {
      case t if (t == "integer")  => elt.toInt
      case t if (t == "double") => elt.toDouble
//      case t if (t == "double") => elt.toDouble
//      case t if (t == "double") => elt.toDouble
//      case t if (t == "double") => elt.toDouble
//      case t if (t == "java.lang.string") => elt.toDouble
      case t if (t == "char") => elt.head: Char
      case t if (t == "boolean") => elt.toBoolean
    }).asInstanceOf[B])
  }

  /**
   *
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

  /**
   * Stream chunk size
   */
  val CHUNK: Int = 2048
}
