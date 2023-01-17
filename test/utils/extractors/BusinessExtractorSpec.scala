/*
 * Copyright 2023 HM Revenue & Customs
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

package utils.extractors

import base.SpecBase
import models.IndividualOrBusiness.Business
import models._
import pages.IndividualOrBusinessPage
import pages.business._

class BusinessExtractorSpec extends SpecBase {

  private val name = "Name"
  private val utr = "1234567890"
  private val phone = "0987654321"
  private val ukAddress = UkAddress("line1", "line2", Some("line3"), Some("line4"), "POSTCODE")
  private val nonUkAddress = NonUkAddress("line1", "line2", Some("line3"), "country")
  private val email: String = "email@example.com"

  "Business Extractor" when {

    val extractor = injector.instanceOf[BusinessExtractor]

    "extract answers for personal rep with UTR, UK address and email" in {

      val personalRep = BusinessPersonalRep(
        name = name,
        phoneNumber = phone,
        utr = Some(utr),
        address = ukAddress,
        email = Some(email)
      )

      val result = extractor(personalRep, emptyUserAnswers).success.value

      result.get(IndividualOrBusinessPage).get mustBe Business
      result.get(UkRegisteredYesNoPage).get mustBe true
      result.get(CompanyNamePage).get mustBe name
      result.get(UtrPage).get mustBe utr
      result.get(AddressUkYesNoPage).get mustBe true
      result.get(UkAddressPage).get mustBe ukAddress
      result.get(NonUkAddressPage) mustNot be(defined)
      result.get(EmailAddressYesNoPage).get mustBe true
      result.get(EmailAddressPage).get mustBe email
      result.get(TelephoneNumberPage).get mustBe phone
    }

    "extract answers for personal rep with no UTR and non-UK address" in {

      val personalRep = BusinessPersonalRep(
        name = name,
        phoneNumber = phone,
        utr = None,
        address = nonUkAddress,
        email = None
      )

      val result = extractor(personalRep, emptyUserAnswers).success.value

      result.get(IndividualOrBusinessPage).get mustBe Business
      result.get(UkRegisteredYesNoPage).get mustBe false
      result.get(CompanyNamePage).get mustBe name
      result.get(UtrPage) mustNot be(defined)
      result.get(AddressUkYesNoPage).get mustBe false
      result.get(UkAddressPage) mustNot be(defined)
      result.get(NonUkAddressPage).get mustBe nonUkAddress
      result.get(EmailAddressYesNoPage).get mustBe false
      result.get(EmailAddressPage) mustNot be(defined)
      result.get(TelephoneNumberPage).get mustBe phone
    }
  }
}
