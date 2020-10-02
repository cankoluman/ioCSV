/*
 * Copyright (c) 2020. Can Koluman.
 * Authored components released under Apache 2.0.
 * For open source components / libraries, respective origin licenses apply.
 * Last modified 01/10/2020, 18:06
 */

package com.cankoluman.ioCvs.test

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
