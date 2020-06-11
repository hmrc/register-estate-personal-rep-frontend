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

package views.individual

import forms.PassportOrIdCardDetailsFormProvider
import models.{CombinedPassportOrIdCard, Name, NormalMode}
import play.api.data.Form
import play.twirl.api.HtmlFormat
import utils.InputOption
import utils.countryOptions.CountryOptions
import views.behaviours.QuestionViewBehaviours
import views.html.individual.PassportView

class PassportViewSpec extends QuestionViewBehaviours[CombinedPassportOrIdCard] {

  val prefix = "individual.passport"
  val name: Name = Name("First", Some("Middle"), "Last")

  override val form: Form[CombinedPassportOrIdCard] = new PassportOrIdCardDetailsFormProvider(frontendAppConfig).withPrefix(prefix)

  val view: PassportView = viewFor[PassportView](Some(emptyUserAnswers))

  val countryOptions: Seq[InputOption] = app.injector.instanceOf[CountryOptions].options

  def applyView(form: Form[_]): HtmlFormat.Appendable =
    view.apply(form, NormalMode, countryOptions, name.displayName)(fakeRequest, messages)

  val applyViewF: Form[_] => HtmlFormat.Appendable = (form: Form[_]) => applyView(form)

  "Passport View" must {

    behave like dynamicTitlePage(applyView(form), prefix, name.displayName)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithPassportOrIDCardDetailsFields(
      form,
      applyView,
      prefix,
      Seq(("country", None), ("number", None)),
      "expiryDate",
      name.displayName
    )

    behave like pageWithASubmitButton(applyView(form))
  }
}
