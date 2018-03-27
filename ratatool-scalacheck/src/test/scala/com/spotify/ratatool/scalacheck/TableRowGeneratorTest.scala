/*
 * Copyright 2018 Spotify AB.
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

package com.spotify.ratatool.scalacheck

import com.google.api.services.bigquery.model.TableRow
import com.spotify.ratatool.Schemas
import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop.{all, forAll, BooleanOperators}

object TableRowGeneratorTest extends Properties("TableRowGenerator") {
  property("round trip") = forAll(tableRowOf(Schemas.tableSchema)) { m =>
    m.setF(m.getF) == m
  }

  val n = "nullable_fields"
  val richGen = tableRowOf(Schemas.tableSchema)
    .amend(Gen.choose(10L, 20L))(_.getRecord(n).set("int_field"))
    .amend(Gen.choose(10.0, 20.0))(_.getRecord(n).set("float_field"))
    .amend(Gen.const(true))(_.getRecord(n).set("boolean_field"))
    .amend(Gen.const("hello"))(_.getRecord(n).set("string_field"))

  property("support RichTableRowGen") = forAll (richGen) { r =>
    val fields = r.get(n).asInstanceOf[TableRow]
    val i = fields.get("int_field").asInstanceOf[Long]
    val f = fields.get("float_field").asInstanceOf[Double]
    val b = fields.get("boolean_field").asInstanceOf[Boolean]
    val s = fields.get("string_field").asInstanceOf[String]
    all(
      "Int"     |: i >= 10L && i <= 20L,
      "Float"   |: f >= 10.0 && f <= 20.0,
      "Boolean" |: b == true,
      "String"  |: s == "hello"
    )
  }

}