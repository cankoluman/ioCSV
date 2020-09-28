/*
 * Copyright (c) 2020. Can Koluman. All rights reserved save for  open
 * sourced components for which respective licenses apply.
 * Last modified 25/09/2020, 12:07
 */

package ioCvs.test

import scala.annotation.tailrec
import scala.reflect.io.{Directory, Path}

/**
 * Created on 25/09/2020.
 *
 * @author Can Koluman
 * @note Test Utilities
 *
 *
 */

object Utility {

  def dirExists(path: String): Boolean = {
    val candidate = Directory(path)
    candidate.exists && candidate.isDirectory
  }

  @tailrec
  def dirDeleteRecursive(path: String): Boolean = {
    if (!dirExists(path)) return false

    val candidate = Directory(path)
    candidate.delete()

    val parent = Path(path).parent.toString()
    dirDeleteRecursive(parent)
  }

}
