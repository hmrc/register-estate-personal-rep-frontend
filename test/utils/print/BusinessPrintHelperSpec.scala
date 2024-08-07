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

package utils.print

import base.SpecBase
import models.{IndividualOrBusiness, NonUkAddress, NormalMode, UkAddress}
import pages.IndividualOrBusinessPage
import pages.business._
import play.twirl.api.Html
import viewmodels.{AnswerRow, AnswerSection}

class BusinessPrintHelperSpec extends SpecBase {

  private val name: String = "Name"
  private val utr: String = "1234567890"
  private val phone: String = "0987654321"
  private val ukAddress: UkAddress = UkAddress("value 1", "value 2", None, None, "AB1 1AB")
  private val nonUkAddress: NonUkAddress = NonUkAddress("value 1", "value 2", None, "DE")
  private val email: String = "email@example.com"

  "BusinessPrintHelper" must {

    val userAnswers = emptyUserAnswers
      .set(IndividualOrBusinessPage, IndividualOrBusiness.Business).success.value
      .set(UkRegisteredYesNoPage, true).success.value
      .set(CompanyNamePage, name).success.value
      .set(UtrPage, utr).success.value
      .set(AddressUkYesNoPage, true).success.value
      .set(UkAddressPage, ukAddress).success.value
      .set(NonUkAddressPage, nonUkAddress).success.value
      .set(EmailAddressYesNoPage, true).success.value
      .set(EmailAddressPage, email).success.value
      .set(TelephoneNumberPage, phone).success.value

    val helper = injector.instanceOf[BusinessPrintHelper]

    "generate business personal rep section for all possible data for UK company with email" in {

      val mode = NormalMode

      val result = helper(userAnswers, name)

      result mustBe AnswerSection(
        headingKey = None,
        rows = Seq(
          AnswerRow(label = messages("individualOrBusiness.checkYourAnswersLabel"), answer = Html("Business"), changeUrl = controllers.routes.IndividualOrBusinessController.onPageLoad(NormalMode).url),
          AnswerRow(label = messages("business.ukRegisteredYesNo.checkYourAnswersLabel"), answer = Html("Yes"), changeUrl = controllers.business.routes.UkRegisteredYesNoController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.ukCompany.name.checkYourAnswersLabel"), answer = Html("Name"), changeUrl = controllers.business.routes.UkCompanyNameController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.utr.checkYourAnswersLabel", name), answer = Html("1234567890"), changeUrl = controllers.business.routes.UtrController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.addressUkYesNo.checkYourAnswersLabel", name), answer = Html("Yes"), changeUrl = controllers.business.routes.AddressUkYesNoController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.ukAddress.checkYourAnswersLabel", name), answer = Html("value 1<br />value 2<br />AB1 1AB"), changeUrl = controllers.business.routes.UkAddressController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.nonUkAddress.checkYourAnswersLabel", name), answer = Html("value 1<br />value 2<br />Germany"), changeUrl = controllers.business.routes.NonUkAddressController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.emailYesNo.checkYourAnswersLabel", name), answer = Html("Yes"), changeUrl = controllers.business.routes.EmailAddressYesNoController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.email.checkYourAnswersLabel", name), answer = Html("email@example.com"), changeUrl = controllers.business.routes.EmailAddressController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.telephoneNumber.checkYourAnswersLabel", name), answer = Html("0987654321"), changeUrl = controllers.business.routes.TelephoneNumberController.onPageLoad(mode).url)
        )
      )
    }

    "generate business personal rep section for all possible data for non UK company without email" in {

      val userAnswers = emptyUserAnswers
        .set(IndividualOrBusinessPage, IndividualOrBusiness.Business).success.value
        .set(UkRegisteredYesNoPage, false).success.value
        .set(CompanyNamePage, name).success.value
        .set(AddressUkYesNoPage, true).success.value
        .set(UkAddressPage, ukAddress).success.value
        .set(NonUkAddressPage, nonUkAddress).success.value
        .set(EmailAddressYesNoPage, false).success.value
        .set(TelephoneNumberPage, phone).success.value

      val mode = NormalMode

      val result = helper(userAnswers, name)

      result mustBe AnswerSection(
        headingKey = None,
        rows = Seq(
          AnswerRow(label = messages("individualOrBusiness.checkYourAnswersLabel"), answer = Html("Business"), changeUrl = controllers.routes.IndividualOrBusinessController.onPageLoad(NormalMode).url),
          AnswerRow(label = messages("business.ukRegisteredYesNo.checkYourAnswersLabel"), answer = Html("No"), changeUrl = controllers.business.routes.UkRegisteredYesNoController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.nonUkCompany.name.checkYourAnswersLabel"), answer = Html("Name"), changeUrl = controllers.business.routes.NonUkCompanyNameController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.addressUkYesNo.checkYourAnswersLabel", name), answer = Html("Yes"), changeUrl = controllers.business.routes.AddressUkYesNoController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.ukAddress.checkYourAnswersLabel", name), answer = Html("value 1<br />value 2<br />AB1 1AB"), changeUrl = controllers.business.routes.UkAddressController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.nonUkAddress.checkYourAnswersLabel", name), answer = Html("value 1<br />value 2<br />Germany"), changeUrl = controllers.business.routes.NonUkAddressController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.emailYesNo.checkYourAnswersLabel", name), answer = Html("No"), changeUrl = controllers.business.routes.EmailAddressYesNoController.onPageLoad(mode).url),
          AnswerRow(label = messages("business.telephoneNumber.checkYourAnswersLabel", name), answer = Html("0987654321"), changeUrl = controllers.business.routes.TelephoneNumberController.onPageLoad(mode).url)
        )
      )
    }
  }
}
