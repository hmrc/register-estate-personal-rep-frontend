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
import utils.countryOptions.CountryOptions
import viewmodels.{AnswerRow, AnswerSection}

class IndividualPrintHelper @Inject()(countryOptions: CountryOptions) {

  def apply(userAnswers: UserAnswers, name: String)(implicit messages: Messages): AnswerSection = {

    val converter = AnswerRowConverter(userAnswers, name, countryOptions)

    val rows: Seq[AnswerRow] = Seq(
      converter.enumQuestion(
        IndividualOrBusinessPage,
        "individualOrBusiness",
        controllers.routes.IndividualOrBusinessController.onPageLoad(NormalMode).url,
        "individualOrBusiness"
      ),
      converter.nameQuestion(NamePage, "individual.name", NameController.onPageLoad(NormalMode).url),
      converter.dateQuestion(DateOfBirthPage, "individual.dateOfBirth", DateOfBirthController.onPageLoad(NormalMode).url),
      converter.yesNoQuestion(NinoYesNoPage, "individual.ninoYesNo", NinoYesNoController.onPageLoad(NormalMode).url),
      converter.ninoQuestion(NinoPage, "individual.nino", NinoController.onPageLoad(NormalMode).url),
      converter.enumQuestion(
        PassportOrIdCardPage,
        "individual.passportOrIdCard",
        PassportOrIdCardController.onPageLoad(NormalMode).url,
        "passportOrIdCard"
      ),
      converter.passportOrIdCardDetailsQuestion(PassportPage, "individual.passport", PassportController.onPageLoad(NormalMode).url),
      converter.passportOrIdCardDetailsQuestion(IdCardPage, "individual.idCard", IdCardController.onPageLoad(NormalMode).url),
      converter.yesNoQuestion(LivesInTheUkYesNoPage, "individual.livesInTheUkYesNo", LivesInTheUkYesNoController.onPageLoad(NormalMode).url),
      converter.addressQuestion(UkAddressPage, "individual.ukAddress", UkAddressController.onPageLoad(NormalMode).url),
      converter.addressQuestion(NonUkAddressPage, "individual.nonUkAddress", NonUkAddressController.onPageLoad(NormalMode).url),
      converter.stringQuestion(TelephoneNumberPage, "individual.telephoneNumber", TelephoneNumberController.onPageLoad(NormalMode).url)
    ).flatten

    AnswerSection(
      None,
      rows
    )
  }

}
