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

package com.cankoluman.ioCsv.test

import scala.annotation.tailrec
import java.io.File

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
    val candidate = new File(path)
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

    val candidate = new File(path)
    val parent = candidate.getParent
    candidate.delete()

    if (parent == null) return false
    dirDeleteRecursive(parent)
  }

}
