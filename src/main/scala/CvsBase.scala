/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 23/09/2020, 18:11
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
abstract class CvsBase(override val separator: Char, override val encoding: Codec) extends TIOCvs {

  /**
   * Constructor with defaults
   * @note The default separator is ';', while the default encoding is UTF-8
   */
  def this(){
    this(';', StandardCharsets.UTF_8)
  }

}
