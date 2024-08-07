/*
 * Copyright 2024 HM Revenue & Customs
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

class NinoYesNoPageSpec extends PageBehaviours {

  private val date: LocalDate = LocalDate.parse("2019-02-03")

  "NinoYesNo page" must {

    beRetrievable[Boolean](NinoYesNoPage)

    beSettable[Boolean](NinoYesNoPage)

    beRemovable[Boolean](NinoYesNoPage)

    "implement cleanup logic when YES selected" in {

      val userAnswers = emptyUserAnswers
        .set(PassportOrIdCardPage, PassportOrIdCard.Passport)
        .flatMap(_.set(PassportPage, Passport("country", "number", date)))
        .flatMap(_.set(IdCardPage, IdCard("country", "number", date)))
        .flatMap(_.set(NinoYesNoPage, true))

      userAnswers.get.get(PassportOrIdCardPage) mustNot be(defined)
      userAnswers.get.get(PassportPage) mustNot be(defined)
      userAnswers.get.get(IdCardPage) mustNot be(defined)
    }

    "implement cleanup logic when NO selected" in {

      val userAnswers = emptyUserAnswers
        .set(NinoPage, "nino")
        .flatMap(_.set(NinoYesNoPage, false))

      userAnswers.get.get(NinoPage) mustNot be(defined)
    }
  }
}
