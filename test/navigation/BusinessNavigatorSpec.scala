/*
 * Copyright 2022 HM Revenue & Customs
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

package navigation

import base.SpecBase
import models.NormalMode
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.business._

class BusinessNavigatorSpec extends SpecBase with ScalaCheckPropertyChecks  {

  val navigator = new BusinessNavigator

  "Business navigator" when {

    "add journey navigation" must {

      val mode = NormalMode

      "Uk Registered Yes/No page -> Yes -> Uk Company Name page" in {
        val answers = emptyUserAnswers
          .set(UkRegisteredYesNoPage, true).success.value

        navigator.nextPage(UkRegisteredYesNoPage, mode, answers)
          .mustBe(controllers.business.routes.UkCompanyNameController.onPageLoad(mode))
      }

      "Uk Registered Yes/No page -> No -> Non Uk Company Name page" in {
        val answers = emptyUserAnswers
          .set(UkRegisteredYesNoPage, false).success.value

        navigator.nextPage(UkRegisteredYesNoPage, mode, answers)
          .mustBe(controllers.business.routes.NonUkCompanyNameController.onPageLoad(mode))
      }

      "Uk Company Name page -> UTR page" in {
        val answers = emptyUserAnswers
          .set(UkRegisteredYesNoPage, true).success.value

        navigator.nextPage(CompanyNamePage, mode, answers)
          .mustBe(controllers.business.routes.UtrController.onPageLoad(mode))
      }

      "Non Uk Company Name page -> Is address in UK page" in {
        val answers = emptyUserAnswers
          .set(UkRegisteredYesNoPage, false).success.value

        navigator.nextPage(CompanyNamePage, mode, answers)
          .mustBe(controllers.business.routes.AddressUkYesNoController.onPageLoad(mode))
      }

      "UTR page -> Is address in UK page" in {
        navigator.nextPage(UtrPage, mode, emptyUserAnswers)
          .mustBe(controllers.business.routes.AddressUkYesNoController.onPageLoad(mode))
      }

      "Is address in UK page -> Yes -> UK address page" in {
        val answers = emptyUserAnswers
          .set(AddressUkYesNoPage, true).success.value

        navigator.nextPage(AddressUkYesNoPage, mode, answers)
          .mustBe(controllers.business.routes.UkAddressController.onPageLoad(mode))
      }

      "Is address in UK page -> No -> Non-UK address page" in {
        val answers = emptyUserAnswers
          .set(AddressUkYesNoPage, false).success.value

        navigator.nextPage(AddressUkYesNoPage, mode, answers)
          .mustBe(controllers.business.routes.NonUkAddressController.onPageLoad(mode))
      }

      "UK address page -> Email address yes no page" in {
        navigator.nextPage(UkAddressPage, mode, emptyUserAnswers)
          .mustBe(controllers.business.routes.EmailAddressYesNoController.onPageLoad(mode))
      }

      "Non-UK address page -> Email address yes no page" in {
        navigator.nextPage(NonUkAddressPage, mode, emptyUserAnswers)
          .mustBe(controllers.business.routes.EmailAddressYesNoController.onPageLoad(mode))
      }

      "Email address yes no page -> YES -> Email address page" in {
        val userAnswers = emptyUserAnswers
          .set(EmailAddressYesNoPage, true).success.value

        navigator.nextPage(EmailAddressYesNoPage, mode, userAnswers)
          .mustBe(controllers.business.routes.EmailAddressController.onPageLoad(mode))
      }

      "Email address yes no page -> NO -> Telephone number page" in {
        val userAnswers = emptyUserAnswers
          .set(EmailAddressYesNoPage, false).success.value

        navigator.nextPage(EmailAddressYesNoPage, mode, userAnswers)
          .mustBe(controllers.business.routes.TelephoneNumberController.onPageLoad(mode))
      }

      "Email address page -> Telephone number page" in {
        navigator.nextPage(EmailAddressPage, mode, emptyUserAnswers)
          .mustBe(controllers.business.routes.TelephoneNumberController.onPageLoad(mode))
      }

      "Telephone number -> Check details page" in {
        navigator.nextPage(TelephoneNumberPage, mode, emptyUserAnswers)
          .mustBe(controllers.business.routes.CheckDetailsController.onPageLoad())
      }
    }
  }
}
