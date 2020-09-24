/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 23/09/2020, 18:11
 */

package ioCvs

import java.io.{File, FileInputStream, InputStream, InputStreamReader, OutputStream, OutputStreamWriter, Reader, Writer}
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Paths
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import scala.io.Codec

/**
 * Created on 23/09/2020.
 *
 * @author Can Koluman
 * @note
 *
 *
 */
abstract class CvsBase[A](override val separator: Char, override implicit val encoding: Codec,
                         override val ending: Char, override val header: Boolean) extends TIOCvs[A] {

  /**
   * Constructor with defaults
   * @note The default separator is ';', the default encoding is UTF-8, the default
   *       line ending is '\n', and the header default is true
   */
  def this(){
    this(';', StandardCharsets.UTF_8, '\n', true)
  }

  /*
  Utility Methods
   */

  /**
   *
   * @param hFile, InputStream, the stream to be read from file
   * @param gZip, Boolean, whether to output a compressed stream
   * @return Reader, a read stream
   */
  protected def inputCharStream(hFile: InputStream, gZip: Boolean): Reader = {
    if (gZip)
      return new InputStreamReader(new GZIPInputStream(hFile), encoding.charSet)
    new InputStreamReader(hFile, encoding.charSet)
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
