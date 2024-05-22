import sbt.*

object AppDependencies {

  val boostrapVersion = "8.6.0"
  val mongoVersion = "1.9.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc.mongo"   %% "hmrc-mongo-play-30"                     % mongoVersion,
    "uk.gov.hmrc"         %% "play-frontend-hmrc-play-30"             % "8.5.0",
    "uk.gov.hmrc"         %% "play-conditional-form-mapping-play-30"  % "2.0.0",
    "uk.gov.hmrc"         %% "bootstrap-frontend-play-30"             % boostrapVersion,
    "uk.gov.hmrc"         %% "domain-play-30"                         % "9.0.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc.mongo"           %% "hmrc-mongo-test-play-28"  % mongoVersion,
    "uk.gov.hmrc"                 %% "bootstrap-test-play-28"   % boostrapVersion,
    "org.scalatest"               %% "scalatest"                % "3.2.18",
    "org.scalatestplus"           %% "scalacheck-1-17"          % "3.2.18.0",
    "org.jsoup"                   %  "jsoup"                    % "1.17.2",
    "org.mockito"                 %% "mockito-scala-scalatest"  % "1.17.31",
    "org.wiremock"                %  "wiremock-standalone"      % "3.5.4",
    "io.github.wolfendale"        %% "scalacheck-gen-regexp"    % "1.1.0",
    "com.vladsch.flexmark"        %  "flexmark-all"             % "0.64.8"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test

}
