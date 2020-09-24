/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 24/09/2020, 20:20
 */

package ioCvs

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
