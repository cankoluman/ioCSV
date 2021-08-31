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

/**
 * Created on 24/09/2020.
 * Just a very light-weight Dataframe concept as a wrapper for csv data.
 * @author Can Koluman
 * @note With, or without header.
 *
 */
case class Frame[A](header: Option[Vector[String]], data: A) {
  def getHeader(col: Int): String =
    if (header.isDefined) header.get(col) else s"No Headers Defined."
}
