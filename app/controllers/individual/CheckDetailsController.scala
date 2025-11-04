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

package controllers.individual

import config.FrontendAppConfig
import connectors.{EstateConnector, EstatesStoreConnector}
import controllers.actions.Actions
import javax.inject.Inject
import play.api.Logging
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import utils.Session
import utils.mappers.IndividualMapper
import utils.print.IndividualPrintHelper
import viewmodels.AnswerSection
import views.html.individual.CheckDetailsView

import scala.concurrent.{ExecutionContext, Future}

class CheckDetailsController @Inject()(
                                        override val messagesApi: MessagesApi,
                                        actions: Actions,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: CheckDetailsView,
                                        val appConfig: FrontendAppConfig,
                                        printHelper: IndividualPrintHelper,
                                        mapper: IndividualMapper,
                                        connector: EstateConnector,
                                        estatesStoreConnector: EstatesStoreConnector
                                      )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with Logging {

  def onPageLoad(): Action[AnyContent] = actions.authWithIndividualName {
    implicit request =>

      val section: AnswerSection = printHelper(request.userAnswers, request.name)
      Ok(view(Seq(section)))
  }

  def onSubmit(): Action[AnyContent] = actions.authWithIndividualName.async {
    implicit request =>

      mapper(request.userAnswers) match {
        case None =>
          logger.error(s"[Session ID: ${Session.id(hc)}]" +
            s" unable to build Individual Personal Rep from user answers, cannot continue with submitting transform")
          Future.successful(InternalServerError)
        case Some(personalRep) =>
          for {
            _ <- connector.addIndividualPersonalRep(personalRep)
            _ <- estatesStoreConnector.setTaskComplete()
          } yield {
            Redirect(appConfig.registerEstateHubOverview)
          }
      }
  }

}
