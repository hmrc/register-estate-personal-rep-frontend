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

import java.time.LocalDate

import base.SpecBase
import controllers.individual.routes._
import models.{IdCard, IndividualOrBusiness, Name, NonUkAddress, NormalMode, Passport, PassportOrIdCard, UkAddress}
import pages.IndividualOrBusinessPage
import pages.individual._
import play.twirl.api.Html
import viewmodels.{AnswerRow, AnswerSection}

class IndividualPrintHelperSpec extends SpecBase {

  val name: Name = Name("First", Some("Middle"), "Last")
  val ukAddress: UkAddress = UkAddress("value 1", "value 2", None, None, "AB1 1AB")
  val nonUkAddress: NonUkAddress = NonUkAddress("value 1", "value 2", None, "DE")
  val date: LocalDate = LocalDate.parse("2019-02-03")
  val passport: Passport = Passport("GB", "12345", date)
  val idCard: IdCard = IdCard("GB", "12345", date)

  "Individual print helper" must {

    val helper = injector.instanceOf[IndividualPrintHelper]

    val userAnswers = emptyUserAnswers
      .set(IndividualOrBusinessPage, IndividualOrBusiness.Individual).success.value
      .set(NamePage, name).success.value
      .set(DateOfBirthPage, date).success.value
      .set(NinoYesNoPage, true).success.value
      .set(NinoPage, "AA000000A").success.value
      .set(PassportOrIdCardPage, PassportOrIdCard.Passport).success.value
      .set(PassportPage, passport).success.value
      .set(IdCardPage, idCard).success.value
      .set(LivesInTheUkYesNoPage, true).success.value
      .set(UkAddressPage, ukAddress).success.value
      .set(NonUkAddressPage, nonUkAddress).success.value
      .set(TelephoneNumberPage, "0123456789").success.value

    "generate answer section" in {

      val result = helper(userAnswers, name.displayName)
      result mustBe AnswerSection(
        headingKey = None,
        rows = Seq(
          AnswerRow(label = Html(messages("individualOrBusiness.checkYourAnswersLabel")), answer = Html("Individual"), changeUrl = controllers.routes.IndividualOrBusinessController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.name.checkYourAnswersLabel")), answer = Html("First Middle Last"), changeUrl = NameController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.dateOfBirth.checkYourAnswersLabel", name.displayName)), answer = Html("3 February 2019"), changeUrl = DateOfBirthController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.ninoYesNo.checkYourAnswersLabel", name.displayName)), answer = Html("Yes"), changeUrl = NinoYesNoController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.nino.checkYourAnswersLabel", name.displayName)), answer = Html("AA 00 00 00 A"), changeUrl = NinoController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.passportOrIdCard.checkYourAnswersLabel", name.displayName)), answer = Html("Passport"), changeUrl = PassportOrIdCardController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.passport.checkYourAnswersLabel", name.displayName)), answer = Html("United Kingdom<br />12345<br />3 February 2019"), changeUrl = PassportController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.idCard.checkYourAnswersLabel", name.displayName)), answer = Html("United Kingdom<br />12345<br />3 February 2019"), changeUrl = IdCardController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.livesInTheUkYesNo.checkYourAnswersLabel", name.displayName)), answer = Html("Yes"), changeUrl = LivesInTheUkYesNoController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.ukAddress.checkYourAnswersLabel", name.displayName)), answer = Html("value 1<br />value 2<br />AB1 1AB"), changeUrl = UkAddressController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.nonUkAddress.checkYourAnswersLabel", name.displayName)), answer = Html("value 1<br />value 2<br />Germany"), changeUrl = NonUkAddressController.onPageLoad(NormalMode).url),
          AnswerRow(label = Html(messages("individual.telephoneNumber.checkYourAnswersLabel", name.displayName)), answer = Html("0123456789"), changeUrl = TelephoneNumberController.onPageLoad(NormalMode).url)
        )
      )
    }
  }
}
