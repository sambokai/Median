package domain

import com.github.nscala_time.time.Imports._



case class Article(id: Int, user: Int, body: String, created: DateTime)

object Article {
  //TODO: custom json conversion https://stackoverflow.com/questions/40406440/convert-json-to-joda-datetime-in-play-scala-2-5
  //  implicit val articleFormat: OFormat[Article] = Json.format[Article]
}