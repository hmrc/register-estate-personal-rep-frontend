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
import models.{CheckMode, NonUkAddress, NormalMode, UkAddress}
import pages.business._
import play.twirl.api.Html
import viewmodels.{AnswerRow, AnswerSection}

class BusinessPrintHelperSpec extends SpecBase {

  private val name: String = "Name"
  private val utr: String = "1234567890"
  private val phone: String = "0987654321"
  private val ukAddress: UkAddress = UkAddress("value 1", "value 2", None, None, "AB1 1AB")
  private val nonUkAddress: NonUkAddress = NonUkAddress("value 1", "value 2", None, "DE")

  "BusinessProtectorPrintHelper" must {

    val userAnswers = emptyUserAnswers
      .set(UkRegisteredYesNoPage, true).success.value
      .set(UkCompanyNamePage, name).success.value
      .set(NonUkCompanyNamePage, name).success.value
      .set(UtrPage, utr).success.value
      .set(AddressUkYesNoPage, true).success.value
      .set(UkAddressPage, ukAddress).success.value
      .set(NonUkAddressPage, nonUkAddress).success.value
      .set(TelephoneNumberPage, phone).success.value

    val helper = injector.instanceOf[BusinessPrintHelper]

    "generate business personal rep section for all possible data" in {

      val mode = NormalMode

      val result = helper(userAnswers, name)

      result mustBe AnswerSection(
        headingKey = None,
        rows = Seq(
          AnswerRow(label = Html(messages("business.ukRegisteredYesNo.checkYourAnswersLabel")), answer = Html("Yes"), changeUrl = controllers.business.routes.UkRegisteredYesNoController.onPageLoad(mode).url),
          AnswerRow(label = Html(messages("business.ukCompany.name.checkYourAnswersLabel")), answer = Html("Name"), changeUrl = controllers.business.routes.UkCompanyNameController.onPageLoad(mode).url),
          AnswerRow(label = Html(messages("business.nonUkCompany.name.checkYourAnswersLabel")), answer = Html("Name"), changeUrl = controllers.business.routes.NonUkCompanyNameController.onPageLoad(mode).url),
          AnswerRow(label = Html(messages("business.utr.checkYourAnswersLabel", name)), answer = Html("1234567890"), changeUrl = controllers.business.routes.UtrController.onPageLoad(mode).url),
          AnswerRow(label = Html(messages("business.addressUkYesNo.checkYourAnswersLabel", name)), answer = Html("Yes"), changeUrl = controllers.business.routes.AddressUkYesNoController.onPageLoad(mode).url),
          AnswerRow(label = Html(messages("business.ukAddress.checkYourAnswersLabel", name)), answer = Html("value 1<br />value 2<br />AB1 1AB"), changeUrl = controllers.business.routes.UkAddressController.onPageLoad(mode).url),
          AnswerRow(label = Html(messages("business.nonUkAddress.checkYourAnswersLabel", name)), answer = Html("value 1<br />value 2<br />Germany"), changeUrl = controllers.business.routes.NonUkAddressController.onPageLoad(mode).url),
          AnswerRow(label = Html(messages("business.telephoneNumber.checkYourAnswersLabel", name)), answer = Html("0987654321"), changeUrl = controllers.business.routes.TelephoneNumberController.onPageLoad(mode).url)
        )
      )
    }
  }
}
