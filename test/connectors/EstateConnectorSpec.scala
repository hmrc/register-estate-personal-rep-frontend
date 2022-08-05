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

package connectors

import java.time.LocalDate

import base.SpecBase
import com.github.tomakehurst.wiremock.client.WireMock._
import generators.Generators
import models.{BusinessPersonalRep, IndividualPersonalRep, Name, NationalInsuranceNumber, UkAddress}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Inside}
import play.api.libs.json.JsSuccess
import play.api.test.Helpers._
import uk.gov.hmrc.http.{HeaderCarrier, UpstreamErrorResponse}
import utils.WireMockHelper

import scala.concurrent.ExecutionContext.Implicits.global

class EstateConnectorSpec extends SpecBase with Generators with WireMockHelper with ScalaFutures
  with Inside with BeforeAndAfterAll with BeforeAndAfterEach with IntegrationPatience {
  implicit lazy val hc: HeaderCarrier = HeaderCarrier()

  val utr = "1000000008"
  val index = 0
  val description = "description"
  val date: LocalDate = LocalDate.parse("2019-02-03")
  val address: UkAddress = UkAddress("Line 1", "Line 2", None, None, "AB1 1AB")
  val phoneNumber: String = "0987654321"
  val email: String = "email@example.com"

  "estate connector" when {

    "individual personal rep" when {

      val individualPersonalRepUrl: String = "/estates/personal-rep/individual"

      val personalRep = IndividualPersonalRep(
        name = Name("John", None, "Doe"),
        dateOfBirth = date,
        identification = NationalInsuranceNumber("AA000000A"),
        address = address,
        phoneNumber = phoneNumber,
        email = Some(email)
      )

      "posting" must {

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
            post(urlEqualTo(individualPersonalRepUrl))
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
            post(urlEqualTo(individualPersonalRepUrl))
              .willReturn(badRequest)
          )

          val result = connector.addIndividualPersonalRep(personalRep)

          whenReady(result.failed) {
            case UpstreamErrorResponse.Upstream4xxResponse(upstream) =>
              upstream.statusCode mustBe BAD_REQUEST
            case _ => fail()
          }

          application.stop()
        }
      }

      "getting" must {

        "Return populated JsValue when there is an individual personal rep in the backend" in {

          val json =
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
              |   "phoneNumber": "0987654321",
              |   "email": "email@example.com"
              |}
              |""".stripMargin

          val application = applicationBuilder()
            .configure(
              Seq(
                "microservice.services.estates.port" -> server.port(),
                "auditing.enabled" -> false
              ): _*
            ).build()

          val connector = application.injector.instanceOf[EstateConnector]

          server.stubFor(
            get(urlEqualTo(individualPersonalRepUrl))
              .willReturn(okJson(json))
          )

          val futureValue = connector.getIndividualPersonalRep()

          whenReady(futureValue) {
            result =>
              result.validate[IndividualPersonalRep].isSuccess mustBe true
              result.validate[IndividualPersonalRep] mustEqual JsSuccess(personalRep)
          }

          application.stop()
        }

        "return empty JsValue when there is not an individual personal rep in the backend" in {

          val json =
            """
              |{}
              |""".stripMargin

          val application = applicationBuilder()
            .configure(
              Seq(
                "microservice.services.estates.port" -> server.port(),
                "auditing.enabled" -> false
              ): _*
            ).build()

          val connector = application.injector.instanceOf[EstateConnector]

          server.stubFor(
            get(urlEqualTo(individualPersonalRepUrl))
              .willReturn(okJson(json))
          )

          val futureValue = connector.getIndividualPersonalRep()

          whenReady(futureValue) {
            result =>
              result.validate[IndividualPersonalRep].isError mustBe true
          }

          application.stop()
        }
      }
    }

    "business personal rep" when {

      val businessPersonalRepUrl: String = "/estates/personal-rep/organisation"

      val personalRep = BusinessPersonalRep(
        name = "Name",
        phoneNumber = phoneNumber,
        utr = Some("1234567890"),
        address = address,
        email = Some(email)
      )

      "posting" must {

        "return OK when the request is successful" in {

          val application = applicationBuilder()
            .configure(
              Seq(
                "microservice.services.estates.port" -> server.port(),
                "auditing.enabled" -> false
              ): _*
            ).build()

          val connector = application.injector.instanceOf[EstateConnector]

          server.stubFor(
            post(urlEqualTo(businessPersonalRepUrl))
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
            post(urlEqualTo(businessPersonalRepUrl))
              .willReturn(badRequest)
          )

          val result = connector.addBusinessPersonalRep(personalRep)

          result.map(response => response.status mustBe BAD_REQUEST)

          application.stop()
        }
      }

      "getting" must {

        "return populated JsValue when there is a business personal rep in the backend" in {

          val json =
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
              |   "phoneNumber": "0987654321",
              |   "email": "email@example.com"
              |}
              |""".stripMargin

          val application = applicationBuilder()
            .configure(
              Seq(
                "microservice.services.estates.port" -> server.port(),
                "auditing.enabled" -> false
              ): _*
            ).build()

          val connector = application.injector.instanceOf[EstateConnector]

          server.stubFor(
            get(urlEqualTo(businessPersonalRepUrl))
              .willReturn(okJson(json))
          )

          val futureValue = connector.getBusinessPersonalRep()

          whenReady(futureValue) {
            result =>
              result.validate[BusinessPersonalRep].isSuccess mustBe true
              result.validate[BusinessPersonalRep] mustEqual JsSuccess(personalRep)
          }

          application.stop()
        }

        "return empty JsValue when there is not a business personal rep in the backend" in {

          val json =
            """
              |{}
              |""".stripMargin

          val application = applicationBuilder()
            .configure(
              Seq(
                "microservice.services.estates.port" -> server.port(),
                "auditing.enabled" -> false
              ): _*
            ).build()

          val connector = application.injector.instanceOf[EstateConnector]

          server.stubFor(
            get(urlEqualTo(businessPersonalRepUrl))
              .willReturn(okJson(json))
          )

          val futureValue = connector.getBusinessPersonalRep()

          whenReady(futureValue) {
            result =>
              result.validate[BusinessPersonalRep].isError mustBe true
          }

          application.stop()
        }
      }
    }
  }
}
