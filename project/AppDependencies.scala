import sbt._

object AppDependencies {
  import play.core.PlayVersion

  val compile = Seq(
    play.sbt.PlayImport.ws,
    "org.reactivemongo"   %% "play2-reactivemongo"            % "0.20.13-play28",
    "uk.gov.hmrc"         %% "play-frontend-hmrc"             % "1.1.0-play-28",
    "uk.gov.hmrc"         %% "play-conditional-form-mapping"  % "1.9.0-play-28",
    "uk.gov.hmrc"         %% "bootstrap-frontend-play-28"     % "5.24.0",
    "com.typesafe.play"   %% "play-json-joda"                 % "2.7.4",
    "uk.gov.hmrc"         %% "domain"                         % "6.2.0-play-28"
  )

  val test = Seq(
    "org.scalatest"               %% "scalatest"                % "3.2.12",
    "org.scalatestplus.play"      %% "scalatestplus-play"       % "5.1.0",
    "org.scalatestplus"           %% "scalatestplus-scalacheck" % "3.1.0.0-RC2",
    "org.scalatestplus"           %% "mockito-3-12"             % "3.2.10.0",
    "org.pegdown"                 %  "pegdown"                  % "1.6.0",
    "org.jsoup"                   %  "jsoup"                    % "1.14.3",
    "com.typesafe.play"           %% "play-test"                % PlayVersion.current,
    "org.mockito"                 %  "mockito-all"              % "1.10.19",
    "org.scalacheck"              %% "scalacheck"               % "1.16.0",
    "com.github.tomakehurst"      % "wiremock-standalone"       % "2.27.2",
    "wolfendale"                  %% "scalacheck-gen-regexp"    % "0.1.2",
    "com.vladsch.flexmark"        % "flexmark-all"              % "0.62.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test

  val akkaVersion = "2.6.19"
  val akkaHttpVersion = "10.2.9"

  val overrides = Seq(
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-protobuf" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "commons-codec"     %  "commons-codec" % "1.12"
  )
}
