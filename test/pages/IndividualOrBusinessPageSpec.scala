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

package pages

import java.time.LocalDate

import models.IndividualOrBusiness.Business
import models.{IdCard, Name, NonUkAddress, Passport, PassportOrIdCard, UkAddress}
import pages.behaviours.PageBehaviours
import pages.individual._

class IndividualOrBusinessPageSpec extends PageBehaviours {

  private val date: LocalDate = LocalDate.parse("2019-02-03")

  "IndividualOrBusiness page" must {

    "implement cleanup logic when BUSINESS selected" in {

      val userAnswers = emptyUserAnswers
        .set(NamePage, Name("First", None, "Last"))
        .flatMap(_.set(DateOfBirthPage, date))
        .flatMap(_.set(NinoYesNoPage, true))
        .flatMap(_.set(NinoPage, "nino"))
        .flatMap(_.set(PassportOrIdCardPage, PassportOrIdCard.Passport))
        .flatMap(_.set(PassportPage, Passport("country", "number", date)))
        .flatMap(_.set(IdCardPage, IdCard("country", "number", date)))
        .flatMap(_.set(LivesInTheUkYesNoPage, true))
        .flatMap(_.set(UkAddressPage, UkAddress("Line 1", "Line 2", None, None, "postcode")))
        .flatMap(_.set(NonUkAddressPage, NonUkAddress("Line 1", "Line 2", None, "country")))
        .flatMap(_.set(IndividualOrBusinessPage, Business))

      userAnswers.get.get(NamePage) mustNot be(defined)
      userAnswers.get.get(DateOfBirthPage) mustNot be(defined)
      userAnswers.get.get(NinoYesNoPage) mustNot be(defined)
      userAnswers.get.get(NinoPage) mustNot be(defined)
      userAnswers.get.get(PassportOrIdCardPage) mustNot be(defined)
      userAnswers.get.get(PassportPage) mustNot be(defined)
      userAnswers.get.get(IdCardPage) mustNot be(defined)
      userAnswers.get.get(LivesInTheUkYesNoPage) mustNot be(defined)
      userAnswers.get.get(UkAddressPage) mustNot be(defined)
      userAnswers.get.get(NonUkAddressPage) mustNot be(defined)
    }
  }
}
