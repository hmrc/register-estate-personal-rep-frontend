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

package connectors

import config.FrontendAppConfig

import javax.inject.Inject
import models.{BusinessPersonalRep, IndividualPersonalRep}
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.http.HttpReads.Implicits
import uk.gov.hmrc.http.HttpReads.Implicits.{readEitherOf, throwOnFailure}
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpResponse, StringContextOps}

import scala.concurrent.{ExecutionContext, Future}

class EstateConnector @Inject()(http: HttpClientV2, config : FrontendAppConfig) {

  implicit def httpResponse: HttpReads[HttpResponse] =
    throwOnFailure(readEitherOf[HttpResponse](Implicits.readRaw))

  private val individualPersonalRepUrl: String = s"${config.estatesUrl}/estates/personal-rep/individual"

  def addIndividualPersonalRep(personalRep: IndividualPersonalRep)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    http.post(url"$individualPersonalRepUrl")
      .withBody(Json.toJson(personalRep))
      .execute[HttpResponse]
  }

  def getIndividualPersonalRep()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[JsValue] = {
    http.get(url"$individualPersonalRepUrl").execute[JsValue]
  }

  private val businessPersonalRepUrl = s"${config.estatesUrl}/estates/personal-rep/organisation"

  def addBusinessPersonalRep(personalRep: BusinessPersonalRep)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    http.post(url"$businessPersonalRepUrl")
      .withBody(Json.toJson(personalRep))
      .execute[HttpResponse]
  }

  def getBusinessPersonalRep()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[JsValue] = {
    http.get(url"$businessPersonalRepUrl").execute[JsValue]
  }

}
