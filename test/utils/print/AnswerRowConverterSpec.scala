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

package utils.print

import base.SpecBase
import models.IndividualOrBusiness._
import models.PassportOrIdCard._
import models.{Name, NormalMode}
import pages.IndividualOrBusinessPage
import pages.individual.PassportOrIdCardPage
import play.twirl.api.Html
import utils.countryOptions.CountryOptions

class AnswerRowConverterSpec extends SpecBase {

  val name: Name = Name("First", Some("Middle"), "Last")
  val countryOptions: CountryOptions = injector.instanceOf[CountryOptions]

  "Answer row converter" must {

    "convert individual or business answer" when {

      "individual" in {

        val userAnswers = emptyUserAnswers.set(IndividualOrBusinessPage, Individual).success.value

        val answerRowConverter = AnswerRowConverter(userAnswers, name.displayName, countryOptions)

        val result = answerRowConverter.enumQuestion(
          IndividualOrBusinessPage,
          "individualOrBusiness",
          controllers.routes.IndividualOrBusinessController.onPageLoad(NormalMode).url,
          "individualOrBusiness"
        )

        result.get.answer mustEqual Html("Individual")
      }

      "business" in {

        val userAnswers = emptyUserAnswers.set(IndividualOrBusinessPage, Business).success.value

        val answerRowConverter = AnswerRowConverter(userAnswers, name.displayName, countryOptions)

        val result = answerRowConverter.enumQuestion(
          IndividualOrBusinessPage,
          "individualOrBusiness",
          controllers.routes.IndividualOrBusinessController.onPageLoad(NormalMode).url,
          "individualOrBusiness"
        )

        result.get.answer mustEqual Html("Business")
      }
    }

    "convert passport or ID card answer" when {

      "passport" in {

        val userAnswers = emptyUserAnswers.set(PassportOrIdCardPage, Passport).success.value

        val answerRowConverter = AnswerRowConverter(userAnswers, name.displayName, countryOptions)

        val result = answerRowConverter.enumQuestion(
          PassportOrIdCardPage,
          "individual.passportOrIdCard",
          controllers.individual.routes.PassportOrIdCardController.onPageLoad(NormalMode).url,
          "passportOrIdCard"
        )

        result.get.answer mustEqual Html("Passport")
      }

      "ID card" in {

        val userAnswers = emptyUserAnswers.set(PassportOrIdCardPage, IdCard).success.value

        val answerRowConverter = AnswerRowConverter(userAnswers, name.displayName, countryOptions)

        val result = answerRowConverter.enumQuestion(
          PassportOrIdCardPage,
          "individual.passportOrIdCard",
          controllers.individual.routes.PassportOrIdCardController.onPageLoad(NormalMode).url,
          "passportOrIdCard"
        )

        result.get.answer mustEqual Html("ID card")
      }
    }
  }
}
