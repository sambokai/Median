name := "play_slick_rest"

version := "1.0"

lazy val `play_slick_rest` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq( ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"


// Slick
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",

  // MySQL Driver
  "mysql" % "mysql-connector-java" % "5.1.34"

)

// PlaySlick
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "3.0.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.1"
)


// Joda Time (nscala-time scala wrapper for joda time)
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.18.0"


// Slic Joda Mapper
libraryDependencies ++= Seq(
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.3.0",
  "org.joda" % "joda-convert" % "1.7"
)

libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.2.1"