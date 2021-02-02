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

package pages.individual

import java.time.LocalDate

import models.{IdCard, Passport, PassportOrIdCard}
import pages.behaviours.PageBehaviours

class PassportOrIdCardPageSpec extends PageBehaviours {

  private val date: LocalDate = LocalDate.parse("2019-02-03")

  "PassportOrIdCard page" must {

    "implement cleanup logic when PASSPORT selected" in {

      val userAnswers = emptyUserAnswers
        .set(IdCardPage, IdCard("country", "number", date))
        .flatMap(_.set(PassportOrIdCardPage, PassportOrIdCard.Passport))

      userAnswers.get.get(IdCardPage) mustNot be(defined)
    }

    "implement cleanup logic when ID CARD selected" in {

      val userAnswers = emptyUserAnswers
        .set(PassportPage, Passport("country", "number", date))
        .flatMap(_.set(PassportOrIdCardPage, PassportOrIdCard.IdCard))

      userAnswers.get.get(PassportPage) mustNot be(defined)
    }
  }
}
