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
