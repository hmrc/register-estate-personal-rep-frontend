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

import forms.NinoFormProvider
import models.{Name, NormalMode}
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.individual.NinoView

class NinoViewSpec extends QuestionViewBehaviours[String] {

  val prefix = "individual.nino"
  val name: Name = Name("First", Some("Middle"), "Last")

  override val form: Form[String] = new NinoFormProvider().withPrefix(prefix)

  val view: NinoView = viewFor[NinoView](Some(emptyUserAnswers))

  def applyView(form: Form[_]): HtmlFormat.Appendable =
    view.apply(form, NormalMode, name.displayName)(fakeRequest, messages)

  "Nino View" must {

    behave like dynamicTitlePage(applyView(form), prefix, name.displayName)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithTextFields(form, applyView,
      prefix,
      Some(name.displayName),
      "value"
    )

    behave like pageWithASubmitButton(applyView(form))

  }
}
