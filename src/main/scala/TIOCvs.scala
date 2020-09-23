/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 23/09/2020, 17:57
 */

package ioCvs

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
trait TIOCvs {


  /**
   * @return Char, the csv file separator
   */
  def separator: Char

  /**
   * @return Codec, the encoding of the CVS file
   */
  def encoding: Codec




}
