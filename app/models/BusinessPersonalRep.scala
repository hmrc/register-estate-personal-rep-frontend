/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

final case class BusinessPersonalRep(name: String,
                                     phoneNumber: String,
                                     utr: Option[String],
                                     address: Address)

object BusinessPersonalRep {

  implicit val reads: Reads[BusinessPersonalRep] =
    ((__ \ 'orgName).read[String] and
      (__ \ 'phoneNumber).read[String] and
      __.lazyRead(readNullableAtSubPath[String](__ \ 'identification \ 'utr)) and
      __.lazyRead(readAtSubPath[Address](__ \ 'identification \ 'address))).tupled.map {

      case (name, phoneNumber, utr, address) =>
        BusinessPersonalRep(name, phoneNumber, utr, address)
    }

  implicit val writes: Writes[BusinessPersonalRep] =
    ((__ \ 'orgName).write[String] and
      (__ \ 'phoneNumber).write[String] and
      (__ \ 'identification \ 'utr).writeNullable[String] and
      (__ \ 'identification \ 'address).write[Address]
      ).apply(unlift(BusinessPersonalRep.unapply))

  def readNullableAtSubPath[T:Reads](subPath : JsPath) : Reads[Option[T]] = Reads (
    _.transform(subPath.json.pick)
      .flatMap(_.validate[T])
      .map(Some(_))
      .recoverWith(_ => JsSuccess(None))
  )

  def readAtSubPath[T:Reads](subPath: JsPath): Reads[T] = Reads (
    _.transform(subPath.json.pick)
      .flatMap(_.validate[T])
  )
}