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

package navigation

import base.SpecBase
import models.{Mode, NormalMode, PassportOrIdCard, UserAnswers}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.individual._

class IndividualNavigatorSpec extends SpecBase with ScalaCheckPropertyChecks  {

  val navigator = new IndividualNavigator
  val mode: Mode = NormalMode

  "Individual navigator" when {

    "employment related trust" must {

      "Name page -> Date of birth page" in {
        navigator.nextPage(NamePage, mode, emptyUserAnswers)
          .mustBe(controllers.individual.routes.DateOfBirthController.onPageLoad(mode))
      }

      "Date of birth page -> NINO yes no page" in {
        navigator.nextPage(DateOfBirthPage, mode, emptyUserAnswers)
          .mustBe(controllers.individual.routes.NinoYesNoController.onPageLoad(mode))
      }

      "NINO yes no page -> YES -> NINO page" in {
        val userAnswers: UserAnswers = emptyUserAnswers
          .set(NinoYesNoPage, true).success.value

        navigator.nextPage(NinoYesNoPage, mode, userAnswers)
          .mustBe(controllers.individual.routes.NinoController.onPageLoad(mode))
      }

      "NINO page -> Lives in the UK yes no page" in {
        navigator.nextPage(NinoPage, mode, emptyUserAnswers)
          .mustBe(controllers.individual.routes.LivesInTheUkYesNoController.onPageLoad(mode))
      }

      "NINO yes no page -> NO -> Passport or ID card page" in {
        val userAnswers: UserAnswers = emptyUserAnswers
          .set(NinoYesNoPage, false).success.value

        navigator.nextPage(NinoYesNoPage, mode, userAnswers)
          .mustBe(controllers.individual.routes.PassportOrIdCardController.onPageLoad(mode))
      }

      "Passport or ID card page -> PASSPORT -> Passport page" in {
        val userAnswers: UserAnswers = emptyUserAnswers
          .set(PassportOrIdCardPage, PassportOrIdCard.Passport).success.value

        navigator.nextPage(PassportOrIdCardPage, mode, userAnswers)
          .mustBe(controllers.individual.routes.PassportController.onPageLoad(mode))
      }

      "Passport or ID card page -> ID CARD -> ID card page" in {
        val userAnswers: UserAnswers = emptyUserAnswers
          .set(PassportOrIdCardPage, PassportOrIdCard.IdCard).success.value

        navigator.nextPage(PassportOrIdCardPage, mode, userAnswers)
          .mustBe(controllers.individual.routes.IdCardController.onPageLoad(mode))
      }

      "Passport page -> Lives in the UK yes no page" in {
        navigator.nextPage(PassportPage, mode, emptyUserAnswers)
          .mustBe(controllers.individual.routes.LivesInTheUkYesNoController.onPageLoad(mode))
      }

      "ID card page -> Lives in the UK yes no page" in {
        navigator.nextPage(IdCardPage, mode, emptyUserAnswers)
          .mustBe(controllers.individual.routes.LivesInTheUkYesNoController.onPageLoad(mode))
      }

      "Lives in the UK yes no page -> YES -> UK address page" in {
        val userAnswers: UserAnswers = emptyUserAnswers
          .set(LivesInTheUkYesNoPage, true).success.value

        navigator.nextPage(LivesInTheUkYesNoPage, mode, userAnswers)
          .mustBe(controllers.individual.routes.UkAddressController.onPageLoad(mode))
      }

      "Lives in the UK yes no page -> NO -> Non-UK address page" in {
        val userAnswers: UserAnswers = emptyUserAnswers
          .set(LivesInTheUkYesNoPage, false).success.value

        navigator.nextPage(LivesInTheUkYesNoPage, mode, userAnswers)
          .mustBe(controllers.individual.routes.NonUkAddressController.onPageLoad(mode))
      }

      "UK address page -> Email address yes no page" in {
        navigator.nextPage(UkAddressPage, mode, emptyUserAnswers)
          .mustBe(controllers.individual.routes.EmailAddressYesNoController.onPageLoad(mode))
      }

      "Non-UK address page -> Email address yes no page" in {
        navigator.nextPage(NonUkAddressPage, mode, emptyUserAnswers)
          .mustBe(controllers.individual.routes.EmailAddressYesNoController.onPageLoad(mode))
      }

      "Email address yes no page -> YES -> Email page" in {
        val userAnswers: UserAnswers = emptyUserAnswers
          .set(EmailAddressYesNoPage, true).success.value

        navigator.nextPage(EmailAddressYesNoPage, mode, userAnswers)
          .mustBe(controllers.individual.routes.EmailAddressController.onPageLoad(mode))
      }

      "Email address yes no page -> NO -> Telephone number page" in {
        val userAnswers: UserAnswers = emptyUserAnswers
          .set(EmailAddressYesNoPage, false).success.value

        navigator.nextPage(EmailAddressYesNoPage, mode, userAnswers)
          .mustBe(controllers.individual.routes.TelephoneNumberController.onPageLoad(mode))
      }

      "Telephone number page -> Check details page" in {
        navigator.nextPage(TelephoneNumberPage, mode, emptyUserAnswers)
          .mustBe(controllers.individual.routes.CheckDetailsController.onPageLoad())
      }

    }
  }
}
