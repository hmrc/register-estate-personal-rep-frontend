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

package controllers.individual

import java.time.LocalDate

import base.SpecBase
import config.annotations.Individual
import forms.PassportFormProvider
import models.{Name, NormalMode, Passport, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import pages.individual.{NamePage, PassportPage}
import play.api.data.Form
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.InputOption
import utils.countryOptions.CountryOptions
import views.html.individual.PassportView

class PassportControllerSpec extends SpecBase {

  lazy val passportRoute: String = routes.PassportController.onPageLoad(NormalMode).url

  val formProvider = new PassportFormProvider(frontendAppConfig)
  val form: Form[Passport] = formProvider.withPrefix("individual.passport")
  val name: Name = Name("First", None, "Last")

  val validAnswer: Passport = Passport("country", "number", LocalDate.parse("2019-02-03"))

  val baseAnswers: UserAnswers = emptyUserAnswers
    .set(NamePage, name).success.value

  val countryOptions: Seq[InputOption] = app.injector.instanceOf[CountryOptions].options

  "Passport Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      val request = FakeRequest(GET, passportRoute)

      val result = route(application, request).value

      val view = application.injector.instanceOf[PassportView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, NormalMode, countryOptions, name.displayName)(request, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = baseAnswers
        .set(PassportPage, validAnswer).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request = FakeRequest(GET, passportRoute)

      val view = application.injector.instanceOf[PassportView]

      val result = route(application, request).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(validAnswer), NormalMode, countryOptions, name.displayName)(request, messages).toString

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
        FakeRequest(POST, passportRoute)
          .withFormUrlEncodedBody(
            "country" -> "country",
            "number" -> "123456",
            "expiryDate.day"   -> validAnswer.expirationDate.getDayOfMonth.toString,
            "expiryDate.month" -> validAnswer.expirationDate.getMonthValue.toString,
            "expiryDate.year"  -> validAnswer.expirationDate.getYear.toString
          )

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      val request =
        FakeRequest(POST, passportRoute)
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val view = application.injector.instanceOf[PassportView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, NormalMode, countryOptions, name.displayName)(request, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, passportRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, passportRoute)
          .withFormUrlEncodedBody(
            "country" -> "country",
            "number" -> "123456",
            "expiryDate.day"   -> validAnswer.expirationDate.getDayOfMonth.toString,
            "expiryDate.month" -> validAnswer.expirationDate.getMonthValue.toString,
            "expiryDate.year"  -> validAnswer.expirationDate.getYear.toString
          )

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
