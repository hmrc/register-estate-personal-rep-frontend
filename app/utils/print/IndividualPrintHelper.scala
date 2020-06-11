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

import com.google.inject.Inject
import controllers.individual.routes._
import models.{NormalMode, UserAnswers}
import pages.IndividualOrBusinessPage
import pages.individual._
import play.api.i18n.Messages
import utils.countryOptions.AllCountryOptions
import viewmodels.{AnswerRow, AnswerSection}

class IndividualPrintHelper @Inject()(answerRowConverter: AnswerRowConverter,
                                      countryOptions: AllCountryOptions
                                     ) {

  def apply(userAnswers: UserAnswers, name: String)(implicit messages: Messages): AnswerSection = {

    val bound: answerRowConverter.Bound = answerRowConverter.bind(userAnswers, name, countryOptions)

    val rows: Seq[AnswerRow] = Seq(
      bound.enumQuestion(
        IndividualOrBusinessPage,
        "individualOrBusiness",
        controllers.routes.IndividualOrBusinessController.onPageLoad(NormalMode).url,
        "individualOrBusiness"
      ),
      bound.nameQuestion(NamePage, "individual.name", NameController.onPageLoad(NormalMode).url),
      bound.dateQuestion(DateOfBirthPage, "individual.dateOfBirth", DateOfBirthController.onPageLoad(NormalMode).url),
      bound.yesNoQuestion(NinoYesNoPage, "individual.ninoYesNo", NinoYesNoController.onPageLoad(NormalMode).url),
      bound.ninoQuestion(NinoPage, "individual.nino", NinoController.onPageLoad(NormalMode).url),
      bound.enumQuestion(
        PassportOrIdCardPage,
        "individual.passportOrIdCard",
        PassportOrIdCardController.onPageLoad(NormalMode).url,
        "passportOrIdCard"
      ),
      bound.passportOrIdCardDetailsQuestion(PassportPage, "individual.passport", PassportController.onPageLoad(NormalMode).url),
      bound.passportOrIdCardDetailsQuestion(IdCardPage, "individual.idCard", IdCardController.onPageLoad(NormalMode).url),
      bound.yesNoQuestion(LivesInTheUkYesNoPage, "individual.livesInTheUkYesNo", LivesInTheUkYesNoController.onPageLoad(NormalMode).url),
      bound.addressQuestion(UkAddressPage, "individual.ukAddress", UkAddressController.onPageLoad(NormalMode).url),
      bound.addressQuestion(NonUkAddressPage, "individual.nonUkAddress", NonUkAddressController.onPageLoad(NormalMode).url),
      bound.stringQuestion(TelephoneNumberPage, "individual.telephoneNumber", TelephoneNumberController.onPageLoad(NormalMode).url)
    ).flatten

    AnswerSection(
      None,
      rows
    )
  }

}
