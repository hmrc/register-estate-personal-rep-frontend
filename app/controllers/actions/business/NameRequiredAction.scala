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

package controllers.actions.business

import controllers.actions.BusinessNameRequest
import javax.inject.Inject
import models.NormalMode
import models.requests.DataRequest
import pages.business.{NonUkCompanyNamePage, UkCompanyNamePage}
import play.api.mvc.Results.Redirect
import play.api.mvc.{ActionRefiner, Result}

import scala.concurrent.{ExecutionContext, Future}

class NameRequiredAction @Inject()(implicit val executionContext: ExecutionContext)
  extends ActionRefiner[DataRequest, BusinessNameRequest] {

  override protected def refine[A](request: DataRequest[A]): Future[Either[Result, BusinessNameRequest[A]]] = {

    Future.successful(
      request.userAnswers.get(UkCompanyNamePage)
        .orElse(request.userAnswers.get(NonUkCompanyNamePage)) match {
        case None =>
          Left(Redirect(controllers.business.routes.UkRegisteredYesNoController.onPageLoad(NormalMode)))
        case Some(name) =>
          Right(BusinessNameRequest(request, name))
      }
    )
  }
}