import com.typesafe.config.Config
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._
import de.heikoseeberger.sbtheader._
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object CommonSettings extends AutoPlugin {
  object autoImport {
    lazy val versionProperties = taskKey[File]("Version property file")
    lazy val upgradeZip = taskKey[File]("Create upgrade zip")
    lazy val installerZip = taskKey[File]("Create the installer zip")
    lazy val majorVersion = settingKey[String]("The major equella version")
    lazy val oracleDriverJar = settingKey[Option[File]]("The oracle driver jar")
    lazy val buildConfig = settingKey[Config]("The build configuration settings")
  }

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = HeaderPlugin && JvmPlugin

  import autoImport._
  override def projectSettings = Seq(
    organization := "org.apereo.equella",
    majorVersion := "6.4",
    version := majorVersion.value + ".r1000",
    javacOptions ++= Seq("-source", "1.8"),
    compileOrder := CompileOrder.JavaThenScala,
    headerLicense := Some(HeaderLicense.ALv2("2015", "Apereo")),
    resolvers ++= Seq(
      "Local EQUELLA deps" at IO.toURI(file(Path.userHome.absolutePath) / "/equella-deps").toString,
      "EBI Nexus" at "http://www.ebi.ac.uk/intact/maven/nexus/content/repositories/ebi-repo/"
    ),
    libraryDependencies += "junit" % "junit" % "4.12" % Test
  )
}