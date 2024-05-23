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

package utils.extractors

import models.IndividualOrBusiness.Business
import models._
import pages.IndividualOrBusinessPage
import pages.business._

import scala.util.Try

class BusinessExtractor {

  def apply(personalRep: BusinessPersonalRep, userAnswers: UserAnswers): Try[UserAnswers] = {
    userAnswers.set(IndividualOrBusinessPage, Business)
      .flatMap(answers => extractName(personalRep, answers))
      .flatMap(answers => extractAddress(personalRep.address, answers))
      .flatMap(answers => extractEmailAddress(personalRep.email, answers))
      .flatMap(_.set(TelephoneNumberPage, personalRep.phoneNumber))
  }

  private def extractName(personalRep: BusinessPersonalRep, userAnswers: UserAnswers): Try[UserAnswers] = {
    personalRep.utr match {
      case Some(utr) =>
        userAnswers.set(UkRegisteredYesNoPage, true)
        .flatMap(_.set(CompanyNamePage, personalRep.name))
        .flatMap(_.set(UtrPage, utr))
      case None =>
        userAnswers.set(UkRegisteredYesNoPage, false)
        .flatMap(_.set(CompanyNamePage, personalRep.name))
    }
  }

  private def extractAddress(address: Address, userAnswers: UserAnswers): Try[UserAnswers] = {
    address match {
      case ukAddress: UkAddress =>
        userAnswers.set(AddressUkYesNoPage, true)
          .flatMap(_.set(UkAddressPage, ukAddress))
      case nonUkAddress: NonUkAddress =>
        userAnswers.set(AddressUkYesNoPage, false)
          .flatMap(_.set(NonUkAddressPage, nonUkAddress))
    }
  }

  private def extractEmailAddress(emailAddress: Option[String], userAnswers: UserAnswers): Try[UserAnswers] = {
    emailAddress match {
      case Some(email) =>
        userAnswers.set(EmailAddressYesNoPage, true)
          .flatMap(_.set(EmailAddressPage, email))
      case None =>
        userAnswers.set(EmailAddressYesNoPage, false)
    }
  }

}
