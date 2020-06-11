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

package utils.mappers

import base.SpecBase
import models.{NonUkAddress, UkAddress}
import pages.business._

class BusinessMapperSpec extends SpecBase {

  private val name = "Name"
  private val utr = "1234567890"
  private val phone = "0987654321"
  private val ukAddress = UkAddress("line1", "line2", Some("line3"), Some("line4"), "POSTCODE")
  private val nonUkAddress = NonUkAddress("line1", "line2", Some("line3"), "country")

  "Business Mapper" when {

    val mapper = injector.instanceOf[BusinessMapper]

    "generate business personal rep model with non Uk Company name, Uk address and no utr" in {

      val userAnswers = emptyUserAnswers
        .set(UkRegisteredYesNoPage, false).success.value
        .set(NonUkCompanyNamePage, name).success.value
        .set(TelephoneNumberPage, phone).success.value
        .set(AddressUkYesNoPage, true).success.value
        .set(UkAddressPage, ukAddress).success.value

      val result = mapper(userAnswers).get

      result.name mustBe name
      result.phoneNumber mustBe phone
      result.utr mustBe None
      result.address mustBe ukAddress
    }

    "generate business personal rep model with uk company name, Uk address and a utr" in {

      val userAnswers = emptyUserAnswers
        .set(UkRegisteredYesNoPage, true).success.value
        .set(UkCompanyNamePage, name).success.value
        .set(TelephoneNumberPage, phone).success.value
        .set(UtrPage, utr).success.value
        .set(AddressUkYesNoPage, true).success.value
        .set(UkAddressPage, ukAddress).success.value

      val result = mapper(userAnswers).get

      result.name mustBe name
      result.phoneNumber mustBe phone
      result.utr mustBe Some(utr)
      result.address mustBe ukAddress
    }

    "generate business personal rep model with uk company name, non Uk address and a utr" in {

      val userAnswers = emptyUserAnswers
        .set(UkRegisteredYesNoPage, true).success.value
        .set(UkCompanyNamePage, name).success.value
        .set(TelephoneNumberPage, phone).success.value
        .set(UtrPage, utr).success.value
        .set(AddressUkYesNoPage, false).success.value
        .set(NonUkAddressPage, nonUkAddress).success.value

      val result = mapper(userAnswers).get

      result.name mustBe name
      result.phoneNumber mustBe phone
      result.utr mustBe Some(utr)
      result.address mustBe nonUkAddress
    }

    "generate business personal rep model with non Uk Company name, non Uk address and no utr" in {

      val userAnswers = emptyUserAnswers
        .set(UkRegisteredYesNoPage, false).success.value
        .set(NonUkCompanyNamePage, name).success.value
        .set(TelephoneNumberPage, phone).success.value
        .set(AddressUkYesNoPage, false).success.value
        .set(NonUkAddressPage, nonUkAddress).success.value

      val result = mapper(userAnswers).get

      result.name mustBe name
      result.phoneNumber mustBe phone
      result.utr mustBe None
      result.address mustBe nonUkAddress
    }
  }
}
