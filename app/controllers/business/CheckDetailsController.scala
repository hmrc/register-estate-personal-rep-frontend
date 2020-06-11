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

package controllers.business

import config.FrontendAppConfig
import connectors.{EstateConnector, EstatesStoreConnector}
import controllers.actions.Actions
import controllers.actions.business.NameRequiredAction
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import utils.mappers.BusinessMapper
import utils.print.BusinessPrintHelper
import viewmodels.AnswerSection
import views.html.business.CheckDetailsView

import scala.concurrent.{ExecutionContext, Future}

class CheckDetailsController @Inject()(
                                        override val messagesApi: MessagesApi,
                                        val controllerComponents: MessagesControllerComponents,
                                        val appConfig: FrontendAppConfig,
                                        actions: Actions,
                                        nameAction: NameRequiredAction,
                                        connector: EstateConnector,
                                        estatesStoreConnector: EstatesStoreConnector,
                                        printHelper: BusinessPrintHelper,
                                        mapper: BusinessMapper,
                                        view: CheckDetailsView
)(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  def onPageLoad(): Action[AnyContent] = actions.authWithData.andThen(nameAction) {
    implicit request =>

      val section: AnswerSection = printHelper(request.userAnswers, request.businessName)
      Ok(view(section))
  }

  def onSubmit(): Action[AnyContent] = actions.authWithData.async {
    implicit request =>

      mapper(request.userAnswers) match {
        case None =>
          Future.successful(InternalServerError)
        case Some(personalRep) =>
          for {
            _ <- connector.addBusinessPersonalRep(personalRep)
            _ <- estatesStoreConnector.setTaskComplete()
          } yield {
            Redirect(appConfig.registerEstateHubOverview)
          }
      }
  }
}
