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

package controllers

import connectors.EstateConnector
import controllers.actions.Actions
import javax.inject.Inject
import models.{BusinessPersonalRep, IndividualPersonalRep, NormalMode, UserAnswers}
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsError, JsSuccess, JsValue}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import utils.Session
import utils.extractors.{BusinessExtractor, IndividualExtractor}

import scala.concurrent.{ExecutionContext, Future}

class IndexController @Inject()(
                                 val controllerComponents: MessagesControllerComponents,
                                 actions: Actions,
                                 repository: SessionRepository,
                                 connector: EstateConnector,
                                 individualExtractor: IndividualExtractor,
                                 businessExtractor: BusinessExtractor
                               )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with Logging {

  def onPageLoad: Action[AnyContent] = actions.authWithSession.async {
    implicit request =>
      logger.info(s"[Session ID: ${Session.id(hc)}] user has started to register personal rep")
      val userAnswers: UserAnswers = request.userAnswers.getOrElse(UserAnswers(request.internalId))

      for {
        _ <- repository.set(userAnswers)
        ind <- connector.getIndividualPersonalRep()
        bus <- connector.getBusinessPersonalRep()
        redirect <- redirect(ind, bus, userAnswers)
      } yield {
        redirect
      }
  }

  private def redirect(ind: JsValue, bus: JsValue, userAnswers: UserAnswers): Future[Result] = {

    (ind.validate[IndividualPersonalRep], bus.validate[BusinessPersonalRep]) match {
      case (JsSuccess(personalRep, _), JsError(_)) =>
        populateIndividualUserAnswers(personalRep, userAnswers)
      case (JsError(_), JsSuccess(personalRep, _)) =>
        populateBusinessUserAnswers(personalRep, userAnswers)
      case _ =>
        Future.successful(Redirect(controllers.routes.IndividualOrBusinessController.onPageLoad(NormalMode)))
    }
  }

  private def populateIndividualUserAnswers(personalRep: IndividualPersonalRep,
                                            userAnswers: UserAnswers
                                           ): Future[Result] = {

    for {
      updatedAnswers <- Future.fromTry(individualExtractor(personalRep, userAnswers))
      _ <- repository.set(updatedAnswers)
    } yield {
      Redirect(controllers.individual.routes.CheckDetailsController.onPageLoad())
    }
  }

  private def populateBusinessUserAnswers(personalRep: BusinessPersonalRep,
                                          userAnswers: UserAnswers
                                         ): Future[Result] = {

    for {
      updatedAnswers <- Future.fromTry(businessExtractor(personalRep, userAnswers))
      _ <- repository.set(updatedAnswers)
    } yield {
      Redirect(controllers.business.routes.CheckDetailsController.onPageLoad())
    }
  }
}
