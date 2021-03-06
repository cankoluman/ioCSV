/*
 * Copyright (c) 2020. Can Koluman.
 * Authored components released under Apache 2.0.
 * For open source components / libraries, respective origin licenses apply.
 * Last modified 01/10/2020, 18:06
 */

package com.cankoluman.ioCsv

import scala.io.Codec

/**
 * Created on 23/09/2020.
 * Our 'common methods' footprint
 * @author Can Koluman
 *
 */
trait TIOCsv[A] {


  /**
   * The csv element separator
   * @return Char, the csv file separator
   */
  def separator: Char

  /**
   * The encoding of the Csv file
   * @return Codec, the encoding of the Csv file
   */
  def encoding: Codec

  /**
   * True if the first line is a header, false otherwise
   * @return Boolean, whether the first line is a header
   */
  def header: Boolean

  /**
   * Reads source csv file into data structure A.
   * @param source, String, path including filename and extension
   * @param gZip, Boolean, whether to read  a compressed (gZip) file
   * @return A, populated data structure
   */
  def csvRead(source: String, gZip: Boolean): Frame[A]

  /**
   * Outputs source data to the target file in csv format.
   * @param source, The data type determined in the implementation
   * @param target, String, path including filename and extension
   * @param gZip, Boolean, whether to compress (gZip) the output file
   * @param append, Boolean, whether to append (true) or to overwrite (false)
   * @note It is up to the operator to ensure correct filename extension
   */
  def csvWrite(source: Frame[A], target: String, gZip: Boolean, append: Boolean): Unit

}
