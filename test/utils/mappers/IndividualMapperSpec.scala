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

package utils.mappers

import java.time.LocalDate

import base.SpecBase
import models._
import pages.individual._

class IndividualMapperSpec extends SpecBase {

  private val name = Name("John", None, "Doe")
  private val nino = "AA000000A"
  private val phone = "0987654321"
  private val date = LocalDate.parse("2019-03-09")
  private val ukAddress = UkAddress("line1", "line2", Some("line3"), Some("line4"), "POSTCODE")
  private val country: String = "country"
  private val number: String = "12345"
  private val nonUkAddress = NonUkAddress("line1", "line2", Some("line3"), country)
  private val passport: Passport = Passport(country, number, date)
  private val idCard: IdCard = IdCard(country, number, date)

  "Individual Mapper" when {

    val mapper = injector.instanceOf[IndividualMapper]

    "generate individual personal rep model with NINO and UK address" in {

      val userAnswers = emptyUserAnswers
        .set(NamePage, name).success.value
        .set(DateOfBirthPage, date).success.value
        .set(NinoYesNoPage, true).success.value
        .set(NinoPage, nino).success.value
        .set(LivesInTheUkYesNoPage, true).success.value
        .set(UkAddressPage, ukAddress).success.value
        .set(TelephoneNumberPage, phone).success.value

      val result = mapper(userAnswers).get

      result.name mustBe name
      result.dateOfBirth mustBe date
      result.identification mustBe NationalInsuranceNumber(nino)
      result.address mustBe ukAddress
      result.phoneNumber mustBe phone
    }

    "generate individual personal rep model with Passport and UK address" in {

      val userAnswers = emptyUserAnswers
        .set(NamePage, name).success.value
        .set(DateOfBirthPage, date).success.value
        .set(NinoYesNoPage, false).success.value
        .set(PassportOrIdCardPage, PassportOrIdCard.Passport).success.value
        .set(PassportPage, passport).success.value
        .set(LivesInTheUkYesNoPage, true).success.value
        .set(UkAddressPage, ukAddress).success.value
        .set(TelephoneNumberPage, phone).success.value

      val result = mapper(userAnswers).get

      result.name mustBe name
      result.dateOfBirth mustBe date
      result.identification mustBe Passport(country, number, date)
      result.address mustBe ukAddress
      result.phoneNumber mustBe phone
    }

    "generate individual personal rep model with ID card and non-UK address" in {

      val userAnswers = emptyUserAnswers
        .set(NamePage, name).success.value
        .set(DateOfBirthPage, date).success.value
        .set(NinoYesNoPage, false).success.value
        .set(PassportOrIdCardPage, PassportOrIdCard.IdCard).success.value
        .set(IdCardPage, idCard).success.value
        .set(LivesInTheUkYesNoPage, false).success.value
        .set(NonUkAddressPage, nonUkAddress).success.value
        .set(TelephoneNumberPage, phone).success.value

      val result = mapper(userAnswers).get

      result.name mustBe name
      result.dateOfBirth mustBe date
      result.identification mustBe IdCard(country, number, date)
      result.address mustBe nonUkAddress
      result.phoneNumber mustBe phone
    }
  }
}
