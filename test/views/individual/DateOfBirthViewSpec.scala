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

package views.individual

import java.time.LocalDate

import forms.DateOfBirthFormProvider
import models.{Name, NormalMode}
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.individual.DateOfBirthView

class DateOfBirthViewSpec extends QuestionViewBehaviours[LocalDate] {

  val prefix = "individual.dateOfBirth"
  val name: Name = Name("First", Some("Middle"), "Last")

  override val form: Form[LocalDate] = new DateOfBirthFormProvider(frontendAppConfig).withPrefix(prefix)

  val view: DateOfBirthView = viewFor[DateOfBirthView](Some(emptyUserAnswers))

  def applyView(form: Form[_]): HtmlFormat.Appendable =
    view.apply(form, NormalMode, name.displayName)(fakeRequest, messages)

  "DateOfBirth View" must {

    behave like dynamicTitlePage(applyView(form), prefix, name.displayName)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithDateFields(
      form,
      applyView,
      prefix,
      "value",
      name.displayName
    )

    behave like pageWithASubmitButton(applyView(form))
  }
}
