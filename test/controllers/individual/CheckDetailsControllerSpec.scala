/*
 * Copyright 2021 HM Revenue & Customs
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

import java.time.LocalDate

import base.SpecBase
import connectors.{EstateConnector, EstatesStoreConnector}
import models.{Name, UkAddress}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import pages.individual._
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.http.HttpResponse
import utils.print.IndividualPrintHelper
import views.html.individual.CheckDetailsView

import scala.concurrent.Future

class CheckDetailsControllerSpec extends SpecBase with MockitoSugar with ScalaFutures {

  private lazy val checkDetailsRoute = routes.CheckDetailsController.onPageLoad().url
  private lazy val submitDetailsRoute = routes.CheckDetailsController.onSubmit().url
  private lazy val redirectRoute = "http://localhost:8822/register-an-estate/registration-progress"

  private val name: Name = Name("First", None, "Last")
  private val dateOfBirth: LocalDate = LocalDate.parse("2010-02-03")
  private val nino: String = "AA123456A"
  private val address: UkAddress = UkAddress("Line 1", "Line 2", None, None, "postcode")
  private val telephoneNumber: String  = "0987654321"

  private val userAnswers = emptyUserAnswers
    .set(NamePage, name).success.value
    .set(DateOfBirthPage, dateOfBirth).success.value
    .set(NinoYesNoPage, true).success.value
    .set(NinoPage, nino).success.value
    .set(LivesInTheUkYesNoPage, true).success.value
    .set(UkAddressPage, address).success.value
    .set(EmailAddressYesNoPage, false).success.value
    .set(TelephoneNumberPage, telephoneNumber).success.value

  "CheckDetails Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request = FakeRequest(GET, checkDetailsRoute)

      val result = route(application, request).value

      val view = application.injector.instanceOf[CheckDetailsView]
      val printHelper = application.injector.instanceOf[IndividualPrintHelper]
      val answerSection = printHelper(userAnswers, name.displayName)

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(answerSection)(request, messages).toString
    }

    "redirect to the hub when submitted" in {

      val mockEstateConnector = mock[EstateConnector]
      val mockEstatesStoreConnector = mock[EstatesStoreConnector]

      val application =
        applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(bind[EstateConnector].toInstance(mockEstateConnector))
          .overrides(bind[EstatesStoreConnector].toInstance(mockEstatesStoreConnector))
          .build()

      when(mockEstateConnector.addIndividualPersonalRep(any())(any(), any())).thenReturn(Future.successful(HttpResponse(OK, "")))
      when(mockEstatesStoreConnector.setTaskComplete()(any(), any())).thenReturn(Future.successful(HttpResponse(OK, "")))

      val request = FakeRequest(POST, submitDetailsRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual redirectRoute

      application.stop()
    }

  }
}
