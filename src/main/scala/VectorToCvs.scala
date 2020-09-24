/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 23/09/2020, 17:56
 */

package ioCvs

import java.io.{BufferedReader, BufferedWriter, FileInputStream, FileOutputStream}
import java.util.zip.GZIPInputStream

import scala.collection.mutable.ArrayBuffer
import scala.io.Codec

/**
 * Created on 23/09/2020.
 *
 * @author Can Koluman
 * @note
 *
 *
 */
class VectorToCvs[A](override val separator: Char, override val encoding: Codec)
  extends CvsBase[Vector[Vector[A]]] {

  /**
   * Inputs source csv file to data structure A
   *
   * @param source , String, path including filename and extension
   * @param gZip   , Boolean, whether to read  a compressed (gZip) file
   * @return A, populated data structure
   */
  override def csvRead(source: String, gZip: Boolean): Vector[Vector[A]] = {
    val hFile = new FileInputStream(source)
    val hInput = inputCharStream(hFile, gZip)
    val data = ArrayBuffer[Vector[A]]()

//    hInput.


    data.toVector
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
  override def csvWrite(source: Vector[Vector[A]], target: String, gZip: Boolean, append: Boolean = false): Unit = {
    makeDir(getPath(target))
    val hFile = new FileOutputStream(target, append)
    val hOutput = outputCharStream(hFile, gZip)

    source.foreach(r => {
      val line = r.mkString(separator.toString)
      var start = 0
      var size = line.length
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
