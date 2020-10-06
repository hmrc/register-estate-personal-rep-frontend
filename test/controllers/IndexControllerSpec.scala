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

package controllers

import base.SpecBase
import connectors.EstateConnector
import models.NormalMode
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import play.api.inject.bind
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class IndexControllerSpec extends SpecBase {

  val indJson: String =
    """
      |{
      |   "name": {
      |      "firstName": "John",
      |      "lastName": "Doe"
      |   },
      |   "dateOfBirth": "2019-02-03",
      |   "identification": {
      |      "nino": "AA000000A",
      |      "address": {
      |         "line1": "Line 1",
      |         "line2": "Line 2",
      |         "postCode": "AB1 1AB",
      |         "country": "GB"
      |      }
      |   },
      |   "phoneNumber": "0987654321"
      |}
      |""".stripMargin

  val orgJson: String =
    """
      |{
      |   "orgName": "Name",
      |   "identification": {
      |      "utr": "1234567890",
      |      "address": {
      |         "line1": "Line 1",
      |         "line2": "Line 2",
      |         "postCode": "AB1 1AB",
      |         "country": "GB"
      |      }
      |   },
      |   "phoneNumber": "0987654321"
      |}
      |""".stripMargin

  val emptyJson: String =
    """
      |{}
      |""".stripMargin

  val mockEstateConnector: EstateConnector = mock[EstateConnector]

  "Index Controller" must {

    "redirect to check individual personal rep answers" when {

      "the most recent submission was an individual personal rep" in {

        val application =
          applicationBuilder(userAnswers = None)
            .overrides(bind[EstateConnector].toInstance(mockEstateConnector))
            .build()

        when(mockEstateConnector.getIndividualPersonalRep()(any(), any())).thenReturn(Future.successful(Json.parse(indJson)))
        when(mockEstateConnector.getBusinessPersonalRep()(any(), any())).thenReturn(Future.successful(Json.parse(emptyJson)))

        val request = FakeRequest(GET, routes.IndexController.onPageLoad().url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result) mustBe Some(controllers.individual.routes.CheckDetailsController.onPageLoad().url)

        application.stop()
      }
    }

    "redirect to check business personal rep answers" when {

      "the most recent submission was a business personal rep" in {

        val application =
          applicationBuilder(userAnswers = None)
            .overrides(bind[EstateConnector].toInstance(mockEstateConnector))
            .build()

        when(mockEstateConnector.getIndividualPersonalRep()(any(), any())).thenReturn(Future.successful(Json.parse(emptyJson)))
        when(mockEstateConnector.getBusinessPersonalRep()(any(), any())).thenReturn(Future.successful(Json.parse(orgJson)))

        val request = FakeRequest(GET, routes.IndexController.onPageLoad().url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result) mustBe Some(controllers.business.routes.CheckDetailsController.onPageLoad().url)

        application.stop()
      }
    }

    "redirect to individual or business page" when {

      "there have been no previous submissions" in {

        val application =
          applicationBuilder(userAnswers = None)
            .overrides(bind[EstateConnector].toInstance(mockEstateConnector))
            .build()

        when(mockEstateConnector.getIndividualPersonalRep()(any(), any())).thenReturn(Future.successful(Json.parse(emptyJson)))
        when(mockEstateConnector.getBusinessPersonalRep()(any(), any())).thenReturn(Future.successful(Json.parse(emptyJson)))

        val request = FakeRequest(GET, routes.IndexController.onPageLoad().url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result) mustBe Some(controllers.routes.IndividualOrBusinessController.onPageLoad(NormalMode).url)

        application.stop()
      }
    }
  }
}
