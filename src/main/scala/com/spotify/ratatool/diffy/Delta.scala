/*
 * Copyright 2016 Spotify AB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.spotify.ratatool.diffy

import scala.util.Try

/**
 * Delta of a single field between two records.
 *
 * @param field "." separated field identifier
 * @param left  left value
 * @param right right value
 * @param delta delta of numerical values
 */
case class Delta(field: String, left: Any, right: Any, delta: Option[Double])

private object DeltaUtil {
  def delta(x: Any, y: Any): Option[Double] =
    Try(y.toString.toDouble - x.toString.toDouble).toOption
}