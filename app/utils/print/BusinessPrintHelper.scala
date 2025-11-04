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

package utils.print

import com.google.inject.Inject
import controllers.business.{routes => rts}
import models.{NormalMode, UserAnswers}
import pages.IndividualOrBusinessPage
import pages.business._
import play.api.i18n.Messages
import viewmodels.AnswerSection

class BusinessPrintHelper @Inject()(checkAnswersFormatters: CheckAnswersFormatters) {

  def apply(userAnswers: UserAnswers, businessName: String)(implicit messages: Messages): AnswerSection = {

    val converter = AnswerRowConverter(userAnswers, businessName)(checkAnswersFormatters)

    val ukRegPrefix = userAnswers.get(UkRegisteredYesNoPage).map(if (_) {"uk"} else {"nonUk"}).getOrElse("uk")

    val companyNameChangeRoute = userAnswers.get(UkRegisteredYesNoPage).map{
      if (_) {rts.UkCompanyNameController.onPageLoad(NormalMode).url
      } else {
        rts.NonUkCompanyNameController.onPageLoad(NormalMode).url
      }
    }.getOrElse(rts.UkCompanyNameController.onPageLoad(NormalMode).url)

    AnswerSection(
      None,
      Seq(
        converter.enumQuestion(
          IndividualOrBusinessPage,
          "individualOrBusiness",
          controllers.routes.IndividualOrBusinessController.onPageLoad(NormalMode).url,
          "individualOrBusiness"
        ),
        converter.yesNoQuestion(UkRegisteredYesNoPage, "business.ukRegisteredYesNo", rts.UkRegisteredYesNoController.onPageLoad(NormalMode).url),
        converter.stringQuestion(CompanyNamePage, s"business.${ukRegPrefix}Company.name", companyNameChangeRoute),
        converter.stringQuestion(UtrPage, "business.utr", rts.UtrController.onPageLoad(NormalMode).url),
        converter.yesNoQuestion(AddressUkYesNoPage, "business.addressUkYesNo", rts.AddressUkYesNoController.onPageLoad(NormalMode).url),
        converter.addressQuestion(UkAddressPage, "business.ukAddress", rts.UkAddressController.onPageLoad(NormalMode).url),
        converter.addressQuestion(NonUkAddressPage, "business.nonUkAddress", rts.NonUkAddressController.onPageLoad(NormalMode).url),
        converter.yesNoQuestion(EmailAddressYesNoPage, "business.emailYesNo", rts.EmailAddressYesNoController.onPageLoad(NormalMode).url),
        converter.stringQuestion(EmailAddressPage, "business.email", rts.EmailAddressController.onPageLoad(NormalMode).url),
        converter.stringQuestion(TelephoneNumberPage, "business.telephoneNumber", rts.TelephoneNumberController.onPageLoad(NormalMode).url)
      ).flatten
    )
  }
}
