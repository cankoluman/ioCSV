/*
 * Copyright (c) 2020. Can Koluman.
 * Authored components released under Apache 2.0.
 * For open source components / libraries, respective origin licenses apply.
 * Last modified 01/10/2020, 18:06
 */

package com.cankoluman.ioCvs

/**
 * Created on 24/09/2020.
 *
 * @author Can Koluman
 * @note Just a very light-weight Dataframe concept as a wrapper for the
 *       with, or without header, csv data
 *
 *
 */
case class Frame[A](header: Option[Vector[String]], data: A) {
  def getHeader(col: Int): String =
    if (header.isDefined) header.get(col) else s"No Headers Defined."
}
