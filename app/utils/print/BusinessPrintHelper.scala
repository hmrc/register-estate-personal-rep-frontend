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
import controllers.business.{routes => rts}
import models.{NormalMode, UserAnswers}
import pages.business._
import play.api.i18n.Messages
import utils.countryOptions.CountryOptions
import viewmodels.AnswerSection

class BusinessPrintHelper @Inject()(countryOptions: CountryOptions) {

  def apply(userAnswers: UserAnswers, businessName: String)(implicit messages: Messages): AnswerSection = {

    val converter = AnswerRowConverter(userAnswers, businessName, countryOptions)

    AnswerSection(
      None,
      Seq(
        converter.yesNoQuestion(AddressUkYesNoPage, "business.ukRegisteredYesNo", rts.UkRegisteredYesNoController.onPageLoad(NormalMode).url),
        converter.stringQuestion(UkCompanyNamePage, "business.ukCompany.name", rts.UkCompanyNameController.onPageLoad(NormalMode).url),
        converter.stringQuestion(NonUkCompanyNamePage, "business.nonUkCompany.name", rts.NonUkCompanyNameController.onPageLoad(NormalMode).url),
        converter.stringQuestion(UtrPage, "business.utr", rts.UtrController.onPageLoad(NormalMode).url),
        converter.yesNoQuestion(AddressUkYesNoPage, "business.addressUkYesNo", rts.AddressUkYesNoController.onPageLoad(NormalMode).url),
        converter.addressQuestion(UkAddressPage, "business.ukAddress", rts.UkAddressController.onPageLoad(NormalMode).url),
        converter.addressQuestion(NonUkAddressPage, "business.nonUkAddress", rts.NonUkAddressController.onPageLoad(NormalMode).url),
        converter.stringQuestion(TelephoneNumberPage, "business.telephoneNumber", rts.TelephoneNumberController.onPageLoad(NormalMode).url)
      ).flatten
    )
  }
}
