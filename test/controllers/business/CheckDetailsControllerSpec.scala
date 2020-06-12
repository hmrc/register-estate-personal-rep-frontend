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

import base.SpecBase
import connectors.{EstateConnector, EstatesStoreConnector}
import models.UkAddress
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import pages.business._
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.http.HttpResponse
import utils.print.BusinessPrintHelper
import views.html.business.CheckDetailsView

import scala.concurrent.Future

class CheckDetailsControllerSpec extends SpecBase with MockitoSugar with ScalaFutures {

  private lazy val checkDetailsRoute = controllers.business.routes.CheckDetailsController.onPageLoad().url
  private lazy val submitDetailsRoute = controllers.business.routes.CheckDetailsController.onSubmit().url
  private lazy val completedRoute = "http://localhost:8822/register-an-estate/registration-progress"

  private val name = "Test"
  private val utr = "1234567890"
  private val phoneNumber = "0987654321"
  private val address = UkAddress("line 1", "line 2", None, None, "AB1 1AB")

  private val userAnswers = emptyUserAnswers
    .set(UkRegisteredYesNoPage, true).success.value
    .set(UkCompanyNamePage, name).success.value
    .set(UtrPage, utr).success.value
    .set(AddressUkYesNoPage, true).success.value
    .set(UkAddressPage, address).success.value
    .set(TelephoneNumberPage, phoneNumber).success.value

  "CheckDetails Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request = FakeRequest(GET, checkDetailsRoute)

      val result = route(application, request).value

      val view = application.injector.instanceOf[CheckDetailsView]
      val printHelper = application.injector.instanceOf[BusinessPrintHelper]
      val answerSection = printHelper(userAnswers, name)

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(answerSection)(fakeRequest, messages).toString
    }

    "redirect to the hub when submitted" in {

      val mockEstateConnector = mock[EstateConnector]
      val mockEstatesStoreConnector = mock[EstatesStoreConnector]

      val application =
        applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(bind[EstateConnector].toInstance(mockEstateConnector))
          .overrides(bind[EstatesStoreConnector].toInstance(mockEstatesStoreConnector))
          .build()

      when(mockEstateConnector.addBusinessPersonalRep(any())(any(), any())).thenReturn(Future.successful(HttpResponse(OK)))
      when(mockEstatesStoreConnector.setTaskComplete()(any(), any())).thenReturn(Future.successful(HttpResponse(OK)))

      val request = FakeRequest(POST, submitDetailsRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual completedRoute

      application.stop()
    }

  }
}
