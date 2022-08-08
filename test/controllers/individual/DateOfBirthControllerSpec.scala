/*
 * Copyright 2022 HM Revenue & Customs
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
import config.annotations.Individual
import forms.DateOfBirthFormProvider
import models.{Name, NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import pages.individual.{DateOfBirthPage, NamePage}
import play.api.data.Form
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.individual.DateOfBirthView

class DateOfBirthControllerSpec extends SpecBase {

  lazy val dateOfBirthRoute: String = routes.DateOfBirthController.onPageLoad(NormalMode).url

  val formProvider = new DateOfBirthFormProvider(frontendAppConfig)
  val form: Form[LocalDate] = formProvider.withPrefix("individual.dateOfBirth")
  val name: Name = Name("First", None, "Last")

  val validAnswer: LocalDate = LocalDate.parse("2019-02-03")

  val baseAnswers: UserAnswers = emptyUserAnswers
    .set(NamePage, name).success.value

  "DateOfBirth Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      val request = FakeRequest(GET, dateOfBirthRoute)

      val result = route(application, request).value

      val view = application.injector.instanceOf[DateOfBirthView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, NormalMode, name.displayName)(request, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = baseAnswers
        .set(DateOfBirthPage, validAnswer).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request = FakeRequest(GET, dateOfBirthRoute)

      val view = application.injector.instanceOf[DateOfBirthView]

      val result = route(application, request).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(validAnswer), NormalMode, name.displayName)(request, messages).toString

      application.stop()
    }

    "redirect to next page when valid data is submitted" in {

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers))
          .overrides(
            bind[Navigator].qualifiedWith(classOf[Individual]).toInstance(new FakeNavigator(onwardRoute))
          )
          .build()

      val request =
        FakeRequest(POST, dateOfBirthRoute)
          .withFormUrlEncodedBody(
            "value.day"   -> validAnswer.getDayOfMonth.toString,
            "value.month" -> validAnswer.getMonthValue.toString,
            "value.year"  -> validAnswer.getYear.toString
          )

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      val request =
        FakeRequest(POST, dateOfBirthRoute)
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val view = application.injector.instanceOf[DateOfBirthView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, NormalMode, name.displayName)(request, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, dateOfBirthRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad.url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, dateOfBirthRoute)
          .withFormUrlEncodedBody(
            "value.day"   -> validAnswer.getDayOfMonth.toString,
            "value.month" -> validAnswer.getMonthValue.toString,
            "value.year"  -> validAnswer.getYear.toString
          )

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad.url

      application.stop()
    }
  }
}
