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

  /**
   * Check if directory at path exists
   * @param path, String
   * @return
   */
  def dirExists(path: String): Boolean = {
    val candidate = Directory(path)
    candidate.exists && candidate.isDirectory
  }

  /**
   * Walk through target path and recursively delete directories
   * @param path, String
   * @return
   */
  @tailrec
  def dirDeleteRecursive(path: String): Boolean = {
    if (!dirExists(path)) return false

    val candidate = Directory(path)
    candidate.delete()

    val parent = Path(path).parent.toString()
    dirDeleteRecursive(parent)
  }

}
