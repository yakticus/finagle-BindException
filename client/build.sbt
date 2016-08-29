lazy val root = (project in file(".")).
  settings(
    name := "finagle-BindException",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq("com.twitter" %% "finagle-http" % "6.37.0")
  )
