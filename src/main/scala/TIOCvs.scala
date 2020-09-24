/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 23/09/2020, 17:57
 */

package ioCvs

import java.io.{BufferedReader, BufferedWriter}
import java.nio.charset.StandardCharsets

import scala.io.Codec

/**
 * Created on 23/09/2020.
 *
 * @author Can Koluman
 * @note
 *
 *
 */
trait TIOCvs[A] {


  /**
   * @return Char, the csv file separator
   */
  def separator: Char

  /**
   * @return Codec, the encoding of the CVS file
   */
  def encoding: Codec

  /**
   * @return Char, the line ending delimiter
   */
  def ending: Char

  /**
   * @return Boolean, whether the first line is a header
   */
  def header: Boolean

  /**
   * Inputs source csv file to data structure A
   * @param source, String, path including filename and extension
   * @param gZip, Boolean, whether to read  a compressed (gZip) file
   * @return A, populated data structure
   */
  def csvRead(source: String, gZip: Boolean): A

  /**
   * Outputs source data to the 'target' file in csv format
   * @param source, The data type determined in the implementation
   * @param target, String, path including filename and extension
   * @param gZip, Boolean, whether to compress (gZip) the output file
   * @param append, Boolean, whether to append (true) or to overwrite (false)
   * @note It is up to the operator to ensure correct filename extension
   */
  def csvWrite(source: A, target: String, gZip: Boolean, append: Boolean): Unit

}
