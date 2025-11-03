/*
 * Copyright 2025 HM Revenue & Customs
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

package pages

import java.time.LocalDate

import models.IndividualOrBusiness._
import models.{IdCard, Name, NonUkAddress, Passport, PassportOrIdCard, UkAddress}
import pages.behaviours.PageBehaviours
import pages.business.UtrPage
import pages.{business => bus, individual => ind}

class IndividualOrBusinessPageSpec extends PageBehaviours {

  private val date: LocalDate = LocalDate.parse("2019-02-03")

  "IndividualOrBusiness page" must {

    "implement cleanup logic when INDIVIDUAL selected" in {

      val userAnswers = emptyUserAnswers
        .set(bus.UkRegisteredYesNoPage, true)
        .flatMap(_.set(bus.CompanyNamePage, "name"))
        .flatMap(_.set(UtrPage, "utr"))
        .flatMap(_.set(bus.AddressUkYesNoPage, true))
        .flatMap(_.set(bus.UkAddressPage, UkAddress("Line 1", "Line 2", None, None, "postcode")))
        .flatMap(_.set(bus.NonUkAddressPage, NonUkAddress("Line 1", "Line 2", None, "country")))
        .flatMap(_.set(IndividualOrBusinessPage, Individual))

      userAnswers.get.get(bus.UkRegisteredYesNoPage) mustNot be(defined)
      userAnswers.get.get(bus.CompanyNamePage) mustNot be(defined)
      userAnswers.get.get(bus.UtrPage) mustNot be(defined)
      userAnswers.get.get(bus.AddressUkYesNoPage) mustNot be(defined)
      userAnswers.get.get(bus.UkAddressPage) mustNot be(defined)
      userAnswers.get.get(bus.NonUkAddressPage) mustNot be(defined)
    }

    "implement cleanup logic when BUSINESS selected" in {

      val userAnswers = emptyUserAnswers
        .set(ind.NamePage, Name("First", None, "Last"))
        .flatMap(_.set(ind.DateOfBirthPage, date))
        .flatMap(_.set(ind.NinoYesNoPage, true))
        .flatMap(_.set(ind.NinoPage, "nino"))
        .flatMap(_.set(ind.PassportOrIdCardPage, PassportOrIdCard.Passport))
        .flatMap(_.set(ind.PassportPage, Passport("country", "number", date)))
        .flatMap(_.set(ind.IdCardPage, IdCard("country", "number", date)))
        .flatMap(_.set(ind.LivesInTheUkYesNoPage, true))
        .flatMap(_.set(ind.UkAddressPage, UkAddress("Line 1", "Line 2", None, None, "postcode")))
        .flatMap(_.set(ind.NonUkAddressPage, NonUkAddress("Line 1", "Line 2", None, "country")))
        .flatMap(_.set(IndividualOrBusinessPage, Business))

      userAnswers.get.get(ind.NamePage) mustNot be(defined)
      userAnswers.get.get(ind.DateOfBirthPage) mustNot be(defined)
      userAnswers.get.get(ind.NinoYesNoPage) mustNot be(defined)
      userAnswers.get.get(ind.NinoPage) mustNot be(defined)
      userAnswers.get.get(ind.PassportOrIdCardPage) mustNot be(defined)
      userAnswers.get.get(ind.PassportPage) mustNot be(defined)
      userAnswers.get.get(ind.IdCardPage) mustNot be(defined)
      userAnswers.get.get(ind.LivesInTheUkYesNoPage) mustNot be(defined)
      userAnswers.get.get(ind.UkAddressPage) mustNot be(defined)
      userAnswers.get.get(ind.NonUkAddressPage) mustNot be(defined)
    }
  }
}
