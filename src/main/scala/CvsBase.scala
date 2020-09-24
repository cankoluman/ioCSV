/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 23/09/2020, 18:11
 */

package ioCvs

import java.io.{BufferedReader, File, InputStream, InputStreamReader, OutputStream, OutputStreamWriter, Writer}
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.util.zip.{GZIPInputStream, GZIPOutputStream}
import scala.io.Codec

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

  /**
   * Constructor with defaults
   * @note The default separator is ';', the default encoding is UTF-8, the default
   *       line ending is '\n', and the header default is true
   */
  def this() = {
    this(';', StandardCharsets.UTF_8, true)
  }

  /*
  Utility Methods
   */

  /**
   *
   * @param hFile, InputStream, the stream to be read from file
   * @param gZip, Boolean, whether to output a compressed stream
   * @return BufferedReader, a (buffered) read stream
   * @note BufferedReader.readline() will break at \n, \r or its combination.
   *       This should pick up all standard line endings
   */
  protected def inputCharStream(hFile: InputStream, gZip: Boolean): BufferedReader = {
    if (gZip)
      return new BufferedReader(new InputStreamReader(new GZIPInputStream(hFile), encoding.charSet))
    new BufferedReader(new InputStreamReader(hFile, encoding.charSet))
  }

  /**
   * @param source, OutputStream, the stream to be written to file
   * @param gZip, Boolean, whether to output a compressed stream
   * @return Writer, a write stream
   */
  protected def outputCharStream(source: OutputStream, gZip: Boolean): Writer = {
    if (gZip)
      return new OutputStreamWriter(new GZIPOutputStream(source), encoding.charSet)
    new OutputStreamWriter(source, encoding.charSet)
  }

  /**
   * Creates the directory if it does not already exist
   * @param path, String, the directory path
   */
  protected def makeDir(path: String): Unit = {
    val resultDir = new File(path)
    if (resultDir.mkdirs())
      println(s"created $path")
    else
      println(s"$path already exists or could not be created")
  }

  /**
   * Extract path from path and filename
   * @param input, String, filename and path
   * @return String, the path component
   */
  protected def getPath(input: String): String = {
    val p = Paths.get(input)
    p.getParent.toString
  }

  /**
   * Stream chunk size
   */
  protected val CHUNK: Int = 2048
}
