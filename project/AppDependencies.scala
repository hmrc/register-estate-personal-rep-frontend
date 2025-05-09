import sbt.*

object AppDependencies {

  val boostrapVersion = "9.1.0"
  val mongoVersion = "2.6.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc.mongo"   %% "hmrc-mongo-play-30"                     % mongoVersion,
    "uk.gov.hmrc"         %% "play-frontend-hmrc-play-30"             % "10.5.0",
    "uk.gov.hmrc"         %% "play-conditional-form-mapping-play-30"  % "3.3.0",
    "uk.gov.hmrc"         %% "bootstrap-frontend-play-30"             % boostrapVersion,
    "uk.gov.hmrc"         %% "domain-play-30"                         % "10.0.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc.mongo"           %% "hmrc-mongo-test-play-30"  % mongoVersion,
    "uk.gov.hmrc"                 %% "bootstrap-test-play-30"   % boostrapVersion,
    "org.scalatest"               %% "scalatest"                % "3.2.19",
    "org.scalatestplus"           %% "scalacheck-1-17"          % "3.2.18.0",
    "org.jsoup"                   %  "jsoup"                    % "1.20.1",
    "org.mockito"                 %% "mockito-scala-scalatest"  % "1.17.37",
    "org.wiremock"                %  "wiremock-standalone"      % "3.13.0",
    "io.github.wolfendale"        %% "scalacheck-gen-regexp"    % "1.1.0",
    "com.vladsch.flexmark"        %  "flexmark-all"             % "0.64.8"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test

}
