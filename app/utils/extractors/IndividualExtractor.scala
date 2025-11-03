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

package utils.extractors

import models.IndividualOrBusiness.Individual
import models._
import pages.IndividualOrBusinessPage
import pages.individual._

import scala.util.Try

class IndividualExtractor {

  def apply(personalRep: IndividualPersonalRep, userAnswers: UserAnswers): Try[UserAnswers] = {
    userAnswers.set(IndividualOrBusinessPage, Individual)
      .flatMap(_.set(NamePage, personalRep.name))
      .flatMap(_.set(DateOfBirthPage, personalRep.dateOfBirth))
      .flatMap(answers => extractIdentification(personalRep.identification, answers))
      .flatMap(answers => extractAddress(personalRep.address, answers))
      .flatMap(answers => extractEmailAddress(personalRep.email, answers))
      .flatMap(_.set(TelephoneNumberPage, personalRep.phoneNumber))
  }

  private def extractIdentification(identification: IndividualIdentification, userAnswers: UserAnswers): Try[UserAnswers] = {
    identification match {
      case NationalInsuranceNumber(nino) =>
        userAnswers.set(NinoYesNoPage, true)
          .flatMap(_.set(NinoPage, nino))
      case Passport(countryOfIssue, number, expirationDate) =>
        userAnswers.set(NinoYesNoPage, false)
          .flatMap(_.set(PassportOrIdCardPage, PassportOrIdCard.Passport))
          .flatMap(_.set(PassportPage, Passport(countryOfIssue, number, expirationDate)))
      case IdCard(countryOfIssue, number, expirationDate) =>
        userAnswers.set(NinoYesNoPage, false)
          .flatMap(_.set(PassportOrIdCardPage, PassportOrIdCard.IdCard))
          .flatMap(_.set(IdCardPage, IdCard(countryOfIssue, number, expirationDate)))
    }
  }

  private def extractAddress(address: Address, userAnswers: UserAnswers): Try[UserAnswers] = {
    address match {
      case ukAddress: UkAddress =>
        userAnswers.set(LivesInTheUkYesNoPage, true)
          .flatMap(_.set(UkAddressPage, ukAddress))
      case nonUkAddress: NonUkAddress =>
        userAnswers.set(LivesInTheUkYesNoPage, false)
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
