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

import controllers.individual.{routes => rts}
import javax.inject.Inject
import models.PassportOrIdCard._
import models.{Mode, UserAnswers}
import pages.Page
import pages.individual._
import play.api.mvc.Call

class IndividualNavigator @Inject()() extends Navigator {

  override def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = routes(mode)(page)(userAnswers)

  private def simpleNavigation(mode: Mode): PartialFunction[Page, Call] = {
    case NamePage => rts.DateOfBirthController.onPageLoad(mode)
    case DateOfBirthPage => rts.NinoYesNoController.onPageLoad(mode)
    case NinoPage | PassportPage | IdCardPage => rts.LivesInTheUkYesNoController.onPageLoad(mode)
    case UkAddressPage | NonUkAddressPage => rts.TelephoneNumberController.onPageLoad(mode)
    case TelephoneNumberPage => controllers.routes.FeatureNotAvailableController.onPageLoad()
  }

  private def conditionalNavigation(mode: Mode): PartialFunction[Page, UserAnswers => Call] = {
    case NinoYesNoPage => ua =>
      yesNoNav(ua, NinoYesNoPage, rts.NinoController.onPageLoad(mode), rts.PassportOrIdCardController.onPageLoad(mode))
    case PassportOrIdCardPage => ua =>
      passportIdCardNav(ua, mode)
    case LivesInTheUkYesNoPage => ua =>
      yesNoNav(ua, LivesInTheUkYesNoPage, rts.UkAddressController.onPageLoad(mode), rts.NonUkAddressController.onPageLoad(mode))
  }

  private def routes(mode: Mode): PartialFunction[Page, UserAnswers => Call] =
    simpleNavigation(mode) andThen (c => (_:UserAnswers) => c) orElse
      conditionalNavigation(mode)

  def passportIdCardNav(ua: UserAnswers, mode: Mode): Call = {
    ua.get(PassportOrIdCardPage) match {
      case Some(Passport) =>
        rts.PassportController.onPageLoad(mode)
      case Some(IdCard) =>
        rts.IdCardController.onPageLoad(mode)
      case _ =>
        controllers.routes.SessionExpiredController.onPageLoad()
    }
  }
}
