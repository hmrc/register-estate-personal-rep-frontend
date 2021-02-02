/*
 * Copyright 2021 HM Revenue & Customs
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

package utils.extractors

import java.time.LocalDate

import base.SpecBase
import models.IndividualOrBusiness.Individual
import models._
import pages.IndividualOrBusinessPage
import pages.individual._

class IndividualExtractorSpec extends SpecBase {

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
  private val email: String = "email@example.com"

  "Individual Extractor" when {

    val extractor = injector.instanceOf[IndividualExtractor]

    "extract answers for personal rep with NINO, UK address and email" in {

      val personalRep = IndividualPersonalRep(
        name = name,
        dateOfBirth = date,
        identification = NationalInsuranceNumber(nino),
        address = ukAddress,
        phoneNumber = phone,
        email = Some(email)
      )

      val result = extractor(personalRep, emptyUserAnswers).success.value

      result.get(IndividualOrBusinessPage).get mustBe Individual
      result.get(NamePage).get mustBe name
      result.get(DateOfBirthPage).get mustBe date
      result.get(NinoYesNoPage).get mustBe true
      result.get(NinoPage).get mustBe nino
      result.get(PassportOrIdCardPage) mustNot be(defined)
      result.get(PassportPage) mustNot be(defined)
      result.get(IdCardPage) mustNot be(defined)
      result.get(LivesInTheUkYesNoPage).get mustBe true
      result.get(UkAddressPage).get mustBe ukAddress
      result.get(NonUkAddressPage) mustNot be(defined)
      result.get(EmailAddressYesNoPage).get mustBe true
      result.get(EmailAddressPage).get mustBe email
      result.get(TelephoneNumberPage).get mustBe phone
    }

    "extract answers for personal rep with Passport and UK address" in {

      val personalRep = IndividualPersonalRep(
        name = name,
        dateOfBirth = date,
        identification = passport,
        address = ukAddress,
        phoneNumber = phone,
        email = None
      )

      val result = extractor(personalRep, emptyUserAnswers).success.value

      result.get(IndividualOrBusinessPage).get mustBe Individual
      result.get(NamePage).get mustBe name
      result.get(DateOfBirthPage).get mustBe date
      result.get(NinoYesNoPage).get mustBe false
      result.get(NinoPage) mustNot be(defined)
      result.get(PassportOrIdCardPage).get mustBe PassportOrIdCard.Passport
      result.get(PassportPage).get mustBe passport
      result.get(IdCardPage) mustNot be(defined)
      result.get(LivesInTheUkYesNoPage).get mustBe true
      result.get(UkAddressPage).get mustBe ukAddress
      result.get(NonUkAddressPage) mustNot be(defined)
      result.get(EmailAddressYesNoPage).get mustBe false
      result.get(EmailAddressPage) mustNot be(defined)
      result.get(TelephoneNumberPage).get mustBe phone
    }

    "extract answers for personal rep with ID card and non-UK address" in {

      val personalRep = IndividualPersonalRep(
        name = name,
        dateOfBirth = date,
        identification = idCard,
        address = nonUkAddress,
        phoneNumber = phone,
        email = None
      )

      val result = extractor(personalRep, emptyUserAnswers).success.value

      result.get(IndividualOrBusinessPage).get mustBe Individual
      result.get(NamePage).get mustBe name
      result.get(DateOfBirthPage).get mustBe date
      result.get(NinoYesNoPage).get mustBe false
      result.get(NinoPage) mustNot be(defined)
      result.get(PassportOrIdCardPage).get mustBe PassportOrIdCard.IdCard
      result.get(PassportPage) mustNot be(defined)
      result.get(IdCardPage).get mustBe idCard
      result.get(LivesInTheUkYesNoPage).get mustBe false
      result.get(UkAddressPage) mustNot be(defined)
      result.get(NonUkAddressPage).get mustBe nonUkAddress
      result.get(EmailAddressYesNoPage).get mustBe false
      result.get(EmailAddressPage) mustNot be(defined)
      result.get(TelephoneNumberPage).get mustBe phone
    }
  }
}
