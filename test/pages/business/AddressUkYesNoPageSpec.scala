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

package pages.business

import models.{NonUkAddress, UkAddress, UserAnswers}
import pages.behaviours.PageBehaviours


class AddressUkYesNoPageSpec extends PageBehaviours {

  "AddressUkYesNoPage" must {

    beRetrievable[Boolean](AddressUkYesNoPage)

    beSettable[Boolean](AddressUkYesNoPage)

    beRemovable[Boolean](AddressUkYesNoPage)

    "implement cleanup logic when NO selected" in {
      val userAnswers = UserAnswers("id")
        .set(UkAddressPage, UkAddress("line1", "line2", None, None, "postcode"))
        .flatMap(_.set(AddressUkYesNoPage, false))

      userAnswers.get.get(UkAddressPage) mustNot be(defined)
    }

    "implement cleanup logic when YES selected" in {
      val userAnswers = UserAnswers("id")
        .set(NonUkAddressPage, NonUkAddress("line1", "line2", None,"country"))
        .flatMap(_.set(AddressUkYesNoPage, true))

      userAnswers.get.get(NonUkAddressPage) mustNot be(defined)
    }
  }
}
