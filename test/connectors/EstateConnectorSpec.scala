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

package connectors

import java.time.LocalDate

import base.SpecBase
import com.github.tomakehurst.wiremock.client.WireMock._
import generators.Generators
import models.{BusinessPersonalRep, IndividualPersonalRep, Name, NationalInsuranceNumber, UkAddress}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Inside}
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import utils.WireMockHelper

import scala.concurrent.ExecutionContext.Implicits.global

class EstateConnectorSpec extends SpecBase with Generators with WireMockHelper with ScalaFutures
  with Inside with BeforeAndAfterAll with BeforeAndAfterEach with IntegrationPatience {
  implicit lazy val hc: HeaderCarrier = HeaderCarrier()

  val utr = "1000000008"
  val index = 0
  val description = "description"
  val date: LocalDate = LocalDate.parse("2019-02-03")
  val address: UkAddress = UkAddress("line 1", "line 2", None, None, "AB1 1AB")

  "estate connector" when {

    "add individual personalRep" must {

      val addIndividualPersonalRepUrl: String = "/estates/personal-rep/individual"

      val personalRep = IndividualPersonalRep(
        name = Name("First", None, "Last"),
        dateOfBirth = date,
        identification = NationalInsuranceNumber("AA000000A"),
        address = address,
        phoneNumber = "0987654321"
      )

      "Return OK when the request is successful" in {

        val application = applicationBuilder()
          .configure(
            Seq(
              "microservice.services.estates.port" -> server.port(),
              "auditing.enabled" -> false
            ): _*
          ).build()

        val connector = application.injector.instanceOf[EstateConnector]

        server.stubFor(
          post(urlEqualTo(addIndividualPersonalRepUrl))
            .willReturn(ok)
        )

        val result = connector.addIndividualPersonalRep(personalRep)

        result.futureValue.status mustBe OK

        application.stop()
      }

      "return Bad Request when the request is unsuccessful" in {

        val application = applicationBuilder()
          .configure(
            Seq(
              "microservice.services.estates.port" -> server.port(),
              "auditing.enabled" -> false
            ): _*
          ).build()

        val connector = application.injector.instanceOf[EstateConnector]

        server.stubFor(
          post(urlEqualTo(addIndividualPersonalRepUrl))
            .willReturn(badRequest)
        )

        val result = connector.addIndividualPersonalRep(personalRep)

        result.map(response => response.status mustBe BAD_REQUEST)

        application.stop()
      }

    }

    "add business personalRep" must {

      def addBusinessPersonalRepUrl = "/estates/personal-rep/organisation"

      val personalRep = BusinessPersonalRep(
        name = "Name",
        phoneNumber = "0987654321",
        utr = Some("1234567890"),
        address = Some(address)
      )

      "Return OK when the request is successful" in {

        val application = applicationBuilder()
          .configure(
            Seq(
              "microservice.services.estates.port" -> server.port(),
              "auditing.enabled" -> false
            ): _*
          ).build()

        val connector = application.injector.instanceOf[EstateConnector]

        server.stubFor(
          post(urlEqualTo(addBusinessPersonalRepUrl))
            .willReturn(ok)
        )

        val result = connector.addBusinessPersonalRep(personalRep)

        result.futureValue.status mustBe OK

        application.stop()
      }

      "return Bad Request when the request is unsuccessful" in {

        val application = applicationBuilder()
          .configure(
            Seq(
              "microservice.services.estates.port" -> server.port(),
              "auditing.enabled" -> false
            ): _*
          ).build()

        val connector = application.injector.instanceOf[EstateConnector]

        server.stubFor(
          post(urlEqualTo(addBusinessPersonalRepUrl))
            .willReturn(badRequest)
        )

        val result = connector.addBusinessPersonalRep(personalRep)

        result.map(response => response.status mustBe BAD_REQUEST)

        application.stop()
      }

    }
  }
}
